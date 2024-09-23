
# Github Repos App

This app leverages the GitHub Repositories Search API to allow users to search for repositories created in the past day, week, or month. Users can apply filters and view repository details by clicking on any search result. The app includes a search bar to refine results further and enables users to mark repositories as favorites for offline access. Built using the latest Android tech stack with MVVM architecture, the app demonstrates efficient handling of network requests, offline caching, and state management to deliver a seamless user experience.

# Features

![image (2)](https://github.com/user-attachments/assets/53ac811e-8bed-46f7-bab4-7f70046251c8)

![image (7)](https://github.com/user-attachments/assets/126f6a8f-5dc9-4750-a9cd-77f241c7588c)

![image (8)](https://github.com/user-attachments/assets/b1a82740-ba5a-473e-9c0c-f054ef756b23)

# Architicture

# MVVM Architecture Overview

This app follows the **Model-View-ViewModel (MVVM)** architecture pattern, leveraging Android's latest tech stack to ensure a clear separation of concerns, testability, and maintainability. Below is a detailed explanation of each layer and the libraries used to implement this architecture.

## Architecture Diagram
```
+---------------------------+
|         View (UI)          |
| Activities / Fragments     |
| StateFlow, ViewModel       |
+---------------------------+
            |
            V
+---------------------------+
|    ViewModel (Logic)       |
| Android ViewModel          |
| Fetch data from Repo       |
| Manage UI states           |
+---------------------------+
            |
            V
+---------------------------+
|  Repository (Data Source)  |
| Single source of truth     |
| Coordinates Data & Network |
| Flow for data streaming    |
+---------------------------+
            |
            V
+---------------------------+
|   Data Layer               |
| Remote (Retrofit, OkHttp)  |
| Local (Room Database)      |
| Pagination (Paging 3)      |
+---------------------------+
```

## Components Breakdown

### 1. **View (UI Layer)**
The **View layer** comprises `Activities` and `Fragments`. It observes data exposed via `StateFlow` from the `ViewModel` and reflects any UI changes. The UI layer is completely unaware of the data sources or business logic. It only interacts with the `ViewModel`.

- **StateFlow**: Used to handle state-driven UI updates, ensuring a reactive UI that responds to state changes.
- **ViewModel**: Manages the UI-related data and communicates with the repository to fetch and prepare data for the UI.

### 2. **ViewModel**
The **ViewModel** acts as a bridge between the `View` and the `Repository`. It holds and manages UI-related data, which can survive configuration changes. The `ViewModel` uses **coroutines** and **Flow** to request data from the repository asynchronously, without blocking the UI thread.

- **Android ViewModel**: Lifecycles aware and helps to load and expose data for the UI.
- **Flow**: Used to stream data between the `Repository` and the `ViewModel`. Data changes are observed and pushed to the UI efficiently.

### 3. **Repository**
The **Repository layer** serves as the single source of truth, coordinating data from both remote (network) and local (database) sources. It abstracts the data sources and provides clean APIs for the `ViewModel` to interact with.

- **Paging 3**: The **Paging 3** library simplifies pagination of large data sets from the network, automatically handling paging, data loading, and retries. It manages data efficiently, fetching it on demand and caching it.
  
### 4. **Data Layer**
This layer consists of two parts: **Remote data source** and **Local data source**.

- **Remote (Retrofit + OkHttp)**: 
  - **Retrofit**: A type-safe HTTP client for making network requests to the GitHub API.
  - **OkHttp**: A network client used for making efficient requests, with built-in support for logging, caching, and connection pooling.
  - **Coroutines (suspend functions)**: Used for making network requests asynchronously to avoid blocking the main thread.
  
- **Local (Room Database)**: 
  - **Room Database**: A SQLite abstraction used to store favorite repositories for offline access. This enables users to mark repositories as favorites and access them without an internet connection.

### 5. **Dependency Injection (Hilt)**
The app uses **Hilt**, a DI library built on top of Dagger, to provide dependencies like the `Repository`, `ViewModel`, `Retrofit`, and `Room` instances across the app efficiently. Hilt simplifies dependency management and ensures scalability, making it easier to inject dependencies and improve testability.

## Key Libraries and Concepts

1. **Coroutines & Flow**
   - **Flow**: Used for asynchronous data streams in the data layer.
   - **StateFlow**: For state management in the UI layer, ensuring the app reacts to state changes.
   - **Suspend functions**: Used for making non-blocking network requests with Retrofit.
   - **Scopes**: Lifecycle-aware coroutine scopes are used in `ViewModel` and `Repository` to manage coroutine execution.

2. **Paging 3**
   - Built-in support for **pagination**, handling data loading in chunks from both the network and the local database.

3. **Retrofit & OkHttp**
   - **Retrofit**: For making API requests to GitHub.
   - **OkHttp**: Manages network traffic, connection pooling, and caching.

4. **Room**
   - Handles **local data storage**, providing an easy way to save and retrieve favorite repositories for offline access.

5. **Hilt**
   - Provides a simplified DI solution, integrating with Android architecture components like `ViewModel` and `Room`.

---

## References:
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Flow](https://kotlinlang.org/docs/flow.html)
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Retrofit](https://square.github.io/retrofit/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
