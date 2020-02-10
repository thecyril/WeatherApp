# WeatherApp Kotlin Clean Architecture (MVVM)

## Overview
This Repository contains a Detailed Weather Sample app that Implements MVVM clean Architecture in Kotlin using
Retorfit2, Koin, RxKotlin, Lottie, AndroidX, Android Jetpack, RxBinding 

## Preview
![Preview](https://i.imgur.com/IbQTkLR.png)

### The app has following packages
 
 - **App.kt** its include Dependencies Injections Modules using **Koin**
 
 - **model** its include the data payload from the repository, the formatted use case model
   
 - **utils** Caintains utils classes and specialy a Mapper class used to map images to a data
 
 - **view** View Classes Fragments/Activities/Adapters 
 
 - **view_model** ViewModels Classes that provide data to View 
 
 - **interfaces** Contains all the necessary interfaces
 
 - **core** Contains the Api and repositories classes, and also some specific translation class like Translator
 
 
 
## MVVM (Model View ViewModel Reperesentation) Flow

- **View** 
  - >Subscribe to data from viewModel
  
  - >Observe viewmodel Publish State for response  


- **ViewModel**  
  - >Having all Publisher of Models
  
  - >Call getWeather() from Repository
  
  - >Send requested param to repository

- **Repository** 
  - > Get RequestData as Param from ViewModel (still in progress)
  
  - > Fetch data from DB/Api 
  
  - > Fetch data as Observable that is observed by viewmodel
            
  
### Libraries Used   

- [RxKotlin](https://github.com/ReactiveX/RxKotlin)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Koin](https://github.com/InsertKoinIO/koin)

- [Retrofit2](https://github.com/square/retrofit)
- [Lottie](https://github.com/airbnb/lottie-android)
- [Android architecture components](https://developer.android.com/topic/libraries/architecture/index.html)



