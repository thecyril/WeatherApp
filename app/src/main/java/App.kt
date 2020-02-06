import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    val serviceModule = module {
        single { Service }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Koin

        startKoin {
            androidContext(this@App)
            printLogger(Level.DEBUG)

            modules(
                listOf(
                    serviceModule
                )
            )
        }
    }

    companion object {
        lateinit var instance: App
    }
}