# Latest News App

![Alt Text](https://upload.wikimedia.org/wikipedia/commons/2/2c/Rotating_earth_%28large%29.gif)


This app shows a list of top headlines news from NewsApi. 

### Main features:
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

### API info (NewsApi):
GET "https://newsapi.org/v2/top-headlines"

@Query apiKey "ADD_YOUR_API_KEY"

@Query country "us"


### SDKs used:
- LiveData
- ViewModel
- Databinding
- Retrofit
- Dagger 2
- Rxjava
- Room
- Picasso 
- Mockito


### APK 
Please find the **LatestNews.apk** in the apk folder. Screenshots available below.

### Architecture 
 The app was built with a clean MVVM architecture following Android Architecture components and Android Jetpack.
 This is very important to make the code base more **robust**, **testable**, and **maintainable** in the long run.

https://developer.android.com/jetpack/docs/guide#best-practices

![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
A **UseCase layer** layer was added before the Repository layer.


I used a generic class NetworkBoundResource for loading a resource. 
This class can provide a resource backed by both the database and the network.

![alt text](https://developer.android.com/topic/libraries/architecture/images/network-bound-resource.png)


### Build/Run the app:
For successfully running the app, you need to add an API key for the NewsAPi on the Constants.kt file.

## Screenshots

<p align="center">
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/1.jpg?raw=true" width="500" >
 
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/3.jpg?raw=true" width="500" >
 
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/5.jpg?raw=true" width="500" >
 
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/2.jpg?raw=true" width="500" >
 
  <img src="https://github.com/islamassi/NewsApi/blob/master/screenshots/7.jpg?raw=true" width="750" >
</p>


### To improve in future
- A delay before sending a search request for around 300 ms
- More test cases

### Feedback
Your Feedback is highly appreciated.
