
### Latest news 
This app shows a list of news articles from NewsApi for US.
* A title, image, short description, and relative publish date is being displayed for every article.
* The user have the ability to **search** for a specific article using the search view.
* **Room local database** was used to save the loaded articles so they will be available offline.
* When a user clicks on an article, article URL will be opened showing all article details in an in-app tab.
This will let the user befit from the browser experience including showing videos. 
In the other hand, the user is still inside the app.
* When the user opens the app, local articles saved in the DB will be loaded and a server request will be initiated in the same time.
The user will be able to see the loaded articles offline.
* The DB is the single source of truth
* When the user rotate the screen, the user will stay loading the same data at the same scroll position.
* The app targets Android API 29 with minSDK 21

Some SDKs used in the app:

- LiveData
- ViewModel
- Databinding
- Retrofit
- Dagger 2
- Rxjava
- Room
- Picasso 
- Mockito


 The app was built with a clean MVVM architecture following Android Architecture components and Android Jetpack.
 This is very important to make the code base more robust, testable, and maintainable in the long run. 

https://developer.android.com/jetpack/docs/guide#best-practices

**Base architecture**. I added a **UseCase layer** before the Repository layer.
![alt text](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


I used a generic class NetworkBoundResource that can provide a resource backed by both the database and the network.
![alt text](https://developer.android.com/topic/libraries/architecture/images/network-bound-resource.png)


# TO improve in future
- A delay before sending a search request for around 300 ms
- More test cases