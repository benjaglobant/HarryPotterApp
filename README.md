# HarryPotterApp
## Android Onboarding Tandil

### Kotlin + MVVM + Clean Architecture + Koin + Coroutines + Room

To do the project Kotlin was used as a programming language and MVVM (Model - View - ViewModel) as software architectural patter.

### MVVM (Model - View - ViewModel):
The Model View ViewModel pattern **helps with the separation of concerns, dividing the user interface with the logic behind**. The decision to use this pattern is mainly based on the support Google has been giving to it. 
Moreover, MVVM is vastly used in today’s Android development, and combines very well with Android Architecture Components like LiveData and DataBindings.
 - Model: As we are implementing MVVM with Clean Architecture, we decided not to have a Model class, the ViewModel interacts directly with the domain, utilizing the use cases. This kind of implementation is named "ViewModel centric".
 - View: The view in our implementation of MVVM is actually a **Fragment or an Activity**. The views enclose everything needed to **handle the user interface**. **They observe the ViewModel**, using LiveData components, **and react to its changes as they need to**.
 - ViewModel: The **responsable of the relationship between the data and the user interface of the application**. The ViewModel **has the logic to convert what the use cases provide into information that the view can understand and present**. Furthermore, it **has the responsability and the logic to react to the user’s input, and call the necessary use cases**. The most useful of the Android’s ViewModel class is its lifecycle consciousness. It only **communicates to the View with LiveData components, so it does not need a reference to a contexts or an activity**: it can keep the information alive even against configuration changes like screen rotations or calls to background.

![alt text](https://github.com/benjaglobant/HarryPotterApp/blob/master/mvvm.png "MVVM")

If you want to read and understad at all MVVM, just visit: https://docs.google.com/presentation/d/1JkepsjABKhOmQr7bc62mL8vd-3UqbyklceHfJn4K0rU/edit?ts=5daa0000#slide=id.g61baf37ef4_1_33

### Clean Architecture:
Clean architecture attempts to **provide a methodology to use in coding in order to make it easier to develop quality code that performs better is easier to change, update and has fewer dependencies**.
An important goal of clean architecture is to provide developers with a way to **organize code in such a way that it encapsulates the business logic but keeps it separate from the delivery mechanism**. 

Layers of Clean Architecture used in this project:
 - Domain: Would execute **business logic which is independent of any layer and is just a pure kotlin package with no android specific dependency**.
 - Data: Would **dispense the required data** for the application to the domain layer by **implementing interface exposed by the domain**.
 - Presentation: Would **include both domain and data layer** and is android specific which **executes the UI logic**.
 
 ![alt text](https://github.com/benjaglobant/HarryPotterApp/blob/master/cleanarchitecture.png "CleanArchitecture")

To learn about different ways of implement Clean Architecture, visit: https://docs.google.com/presentation/d/1aBtccGraTyggnIP6Nn7m8uGfBgreKWIk-2JuLafKAds/edit#slide=id.p4

### Dependency Injection - Koin
 - Dependency injection is a programming **technique that makes a class independent of its dependencies by decoupling the usage of an object from its creation**. It’s derived from the fifth principle of the famous object oriented programming principles S.O.L.I.D (**Dependency Inversion**: class should concentrate on fulfilling its own responsibilities and should not be concerned with creating objects to fulfill those responsibilities).
  This layer was implemented using Koin, that is a lightweight dependency injection framework written purely in Kotlin that uses neither code generation, nor proxies, nor reflections.

1) Declare a module: Defines those **classes which will be injected** at some point in the app.
```
val useCaseModule = module { single { UseCaseClass }
```

2) Start Koin: A single line, **startKoin(this, listOf(applicationModule)), allows you to launch the DI process and indicate which modules will be available when needed**, in this case, only useCaseModule.
```
class BaseApplication : Application() {  
  override fun onCreate() {  
    super.onCreate()  
    startKoin(this, listOf(useCaseModule))  
  }  
}  
```

3) Perform an injection: In consonance with Kotlin features, **Koin allows to perform lazy injections in a very convenient way**.
```
class FeatureActivity : AppCompatActivity() {
  private val useCase: UseCaseClass by inject()
  ...
}
```

### Coroutines:
Coroutines are a new way of managing background threads that can simplify code by reducing the need for callbacks. They convert async callbacks for long-running tasks, such as database or network access, into sequential code. We use coroutines to do tasks in a background thread. The guideline should be that every task executed in a coroutine should be done in a background thread and the UI doesn’t get blocked.

Job: a job is a cancellable task with a life-cycle that culminates in its completion. By default, a failure of any of the job’s children leads to an immediate failure of its parent and cancellation of the rest of its children.

Dispatchers:  
  - Dispatchers.Default – is used by all standard builders by default. It uses a common pool of shared background threads. This is an appropriate choice for compute-intensive coroutines that consume CPU resources.
  - Dispatchers.IO – uses a shared pool of on-demand created threads and is designed for offloading of IO-intensive blocking operations (like file I/O and blocking socket I/O).

### Room:
Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite. Apps that handle non-trivial amounts of structured data can benefit greatly from persisting that data locally. The most common use case is to cache relevant pieces of data. That way, when the device cannot access the network, the user can still browse that content while they are offline. Any user-initiated content changes are then synced to the server after the device is back online.
There are 3 major components in Room:
 - Database: Contains the database holder and serves as the main access point for the underlying connection to your app's persisted, relational data.  
   The class that's annotated with @Database should satisfy the following conditions:
   - Be an abstract class that extends **RoomDatabase**.
   - Include the list of **entities associated** with the database within the annotation.
   - Contain an **abstract method that has 0 arguments and returns the class that is annotated with @Dao**.
 - Entity: **Represents a table** within the database.
 - DAO: **Data Access Object**, contains the methods used for accessing the database.  
 ![alt text](https://github.com/benjaglobant/HarryPotterApp/blob/master/roomdatabase.png "Room Database")
 If you want to know how to implement a database using Room in your project, visit:  
https://developer.android.com/training/data-storage/room/index.html
 
 
 
