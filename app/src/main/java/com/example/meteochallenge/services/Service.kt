package com.example.meteochallenge.services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Base64
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object Service {

    private var httpClient: OkHttpClient.Builder? = null
    private var builder: Retrofit.Builder? = null
    private var retrofit: Retrofit? = null
    private val clients = HashMap<Class<*>, Any>()
    private var authOkHTTP: AuthOkHttp? = null
    private var loadAsset: LoadAsset? = null

    var context: Context? = null

    // region APIError

    private var listener: OnServiceListener? = null

    // endregion

    init {
        this.authOkHTTP = AuthOkHttp()
    }

    private fun setContext() {
        if (context == null) {
            context = App.instance.baseContext
        }
    }

    private fun configRetrofit() {
        // MOCK
        setContext()

        //config retrofit
        val interceptor = HttpLoggingInterceptor().setLevel(Level.BODY)

        this.httpClient = Builder()
            .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val original = chain.request()

                chain.proceed(
                    original
                        .newBuilder()
                        .header("Accept", "application/json; version=" + Constants.API_VERSION)
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build()
                )
            }

        authOkHTTP?.let {
            this.httpClient?.authenticator(it)
        }

        when {
            BuildConfig.FLAVOR == "mock" -> {
                loadAsset = LoadAsset()
                authOkHTTP = AuthOkHttp()
                context?.let { httpClient?.addInterceptor(MockInterceptor(it, loadAsset!!)) }
            }
            loadAsset != null ->
                context?.let { httpClient?.addInterceptor(MockInterceptor(it, loadAsset!!)) }
        }

        this.builder = Retrofit.Builder()
            .baseUrl(context?.getString(R.string.baseURL) ?: "")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )

        retrofit = builder?.client(httpClient!!.build())?.build()

        SharedPrefsManager.getToken(context!!)?.let {
            setAuthenticationHeaders(it)
        }
    }

    fun <S> createClient(serviceClass: Class<S>): S {
        var client: Any? = clients[serviceClass]

        if (client == null) {
            if (retrofit == null) {
                configRetrofit()
            }
            client = retrofit?.create(serviceClass)
            retrofit?.create(serviceClass)
            clients[serviceClass] = client!!
        }
        return client as S
    }

    fun enqueue(call: Call<*>, callback: ((obj: Any?, success: Boolean) -> Unit)?) {
        val showFailureMessage = true

        if (isNetworkAvailable()) {
            (call as Call<Any>).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
                    if (response.isSuccessful) {
                        callback?.let { it(response.body(), true) }
                    } else if (response.raw().code() == 404) {
                        callback?.let {
                            it(
                                ApiError(
                                    response.raw().code(),
                                    response.errorBody().toString()
                                ), false
                            )
                        }
                    } else {
                        val path = call.request().url().encodedPath()

                        if (!path.contains(Routes.tourLastChange) && !path.contains(Routes.location)) {
                            manageApiError(response, showFailureMessage, callback)
                        }
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    if (!call.isCanceled) {
                        val path = call.request().url().encodedPath()
                        //we need manage errors
                        if (listener != null && showFailureMessage && !path.contains(Routes.tourLastChange) && !path.contains(
                                Routes.location
                            )
                        ) {
                            listener?.onApiError(Constants.INTERNAL_ERROR, "")
                        }
                        callback?.let { it(null, false) }
                    }
                }
            })
        } else {
            val path = call.request().url().encodedPath()

            if (listener != null && showFailureMessage && !path.contains(Routes.tourLastChange) && !path.contains(
                    Routes.location
                )
            ) {
                listener?.onApiError(Constants.NETWORK_ERROR, "")
            }
            callback?.let { it(null, false) }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun setAuthenticationHeaders(tokenKey: String?) {
        clients.clear()

        try {
            val token = tokenKey?.toByteArray(charset("UTF-8"))
            val base64Token = Base64.encodeToString(token, Base64.NO_WRAP)

            //add interceptor to set headers with token
            httpClient?.addInterceptor { chain ->
                val original = chain.request()

                val customAnnotations = original.headers().values("@")

                if (customAnnotations.isNotEmpty() && customAnnotations[0].equals(
                        Constants.NO_AUTH,
                        ignoreCase = true
                    )
                ) {
                    chain.proceed(original)
                } else {
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("Authorization", "Bearer $base64Token")
                            .build()
                    )
                }
            }
            httpClient?.build()
            retrofit = builder?.client(httpClient!!.build())?.build()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun cleanAuthentication() {
        clients.clear()
        configRetrofit()
    }

    private fun manageApiError(
        response: retrofit2.Response<*>,
        showFailureMessage: Boolean,
        callback: ((obj: Any?, success: Boolean) -> Unit)?
    ) {
        val errorConverter = retrofit?.responseBodyConverter<APIError.APIErrorBody>(
            APIError.APIErrorBody::class.java,
            arrayOfNulls<Annotation>(0)
        )

        try {
            response.errorBody()?.let {
                val error = errorConverter?.convert(it)?.error

                if (error != null) {
                    val code = error.code
                    if (listener != null && showFailureMessage) {
                        listener?.onApiError(code, error.message)
                    }
                } else {
                    if (listener != null && showFailureMessage) {
                        listener?.onApiError(Constants.INTERNAL_ERROR, "")
                    }
                }
                callback?.let { it(error, false) }
            }
        } catch (e: IOException) {
            callback?.let { it(null, false) }

            if (listener != null && showFailureMessage) {
                listener?.onApiError(Constants.INTERNAL_ERROR, "")
            }
        }
    }

    fun setAuthOkHttp(auth: AuthOkHttp?) {
        this.authOkHTTP = auth
    }

    fun setLoadAsset(loadAsset: LoadAsset?) {
        this.loadAsset = loadAsset
    }

    fun getListener(): OnServiceListener? {
        return listener
    }

    fun setListener(listener: OnServiceListener?) {
        this.listener = listener
    }
}
}