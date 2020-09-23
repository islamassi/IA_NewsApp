# I.A News App

This app shows a list of top headlines news from NewsApi. 

- [I.A News App](#i.a-news-app)
    + [Main features](#main-features)
    + [APK](#apk)
    + [Demo Video](#demo-video)
    + [SDKs used(Not for animation, transitions, and custom UI)](#sdks-used)
    + [API info (NewsApi)](#api-info--newsapi-)
    + [Architecture](#architecture)
    + [Build/Run the app](#build-run-the-app)

<p align="center">
  <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/scroll.gif?raw=true" width="350" >
 </p>

<p align="center">
   <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/live_drag.gif?raw=true" width="350" >
   <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/read_options.gif?raw=true" width="350" >
 </p>
 
 <p align="center">
  <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/live_view.gif?raw=true" width="350" >
  <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/transition.gif?raw=true" width="350" >
 </p>
 
 <p align="center">
  <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/search.gif?raw=true" width="350" >
  <img src="https://github.com/islamassi/IA_NewsApp/blob/feature/articleDetails/screenshots/collapse2.gif?raw=true" width="350" >
 </p>
 
### Main features
* Showing a list of news articles
* A title, image, short description, and relative publish date is being displayed for every article.
* Article Details screen shows more details about an article.
* Draggable live streaming view (Just design without implementation)
* Read options view (Just design without functionality)
* Smooth animations and transitions
* The user have the ability to **search** for a specific article using the search view.
* **Room database** was used to save the loaded articles locally so they will be available offline.
* Open article url in an in-app browser
* Local articles saved in the database will be loaded when the user opens the app. a server request will be also initiated in the same time.
The user will be able to see the loaded articles offline.
* The database is the single source of truth
* When the user rotate the screen, the user will see the same data at the same scroll position.
* The app targets Android API 30 with minSDK 23 and using androidX

### APK 
<a href="https://github.com/islamassi/IA_NewsApp/raw/master/apk/IA_News.apk">**IA_News.apk**</a>

### Demo Video 
<a href="https://drive.google.com/file/d/1poveEf-fp0C2P9gqjA1s9UOPhoQb17V1/view?usp=sharing">Click here</a>

### SDKs used
- LiveData
- ViewModel
- Databinding
- Retrofit
- Dagger 2
- Rxjava
- Room
- Picasso 
- Mockito

### API info (NewsApi)
GET "https://newsapi.org/v2/top-headlines"

@Query apiKey "ADD_YOUR_API_KEY"

@Query country "us"


### Architecture 
 The app was built with a clean MVVM architecture following Android Architecture components and Android Jetpack.
 This is very important to make the code base more **robust**, **testable**, and **maintainable** in the long run.

https://developer.android.com/jetpack/docs/guide#best-practices

![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
A **UseCase layer** layer was added before the Repository layer.


I used a generic class NetworkBoundResource for loading a resource. 
This class can provide a resource backed by both the database and the network.

![alt text](https://developer.android.com/topic/libraries/architecture/images/network-bound-resource.png)


### Build/Run the app
For successfully running the app, you need to add an API key for the NewsAPi on the Constants.kt file.

<p align="center">
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/1.jpg?raw=true" width="500" >
 </p>
