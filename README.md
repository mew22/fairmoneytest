# "FairMoneyTest" Android App

This application has been developed as part of a technical test for a job opportunity at FairMoney.

Here is the skill test statement:

- Create an Android App displaying a list items and their detail when clicking on it
- Data needs to come from an API, you can use https://dummyapi.io/ (or choose one of your choice)
- Display list of users (https://dummyapi.io/data/api/user?limit=100)
- Display details of user when clicking on a user (https://dummyapi.io/data/api/user/{userId})
- Add cache or offline mode.

### State of the application

First of all, I would like to point out that the application is incomplete in its current state. 
I did not manage to write all the tests I wanted (mostly missing UI test) nor to fully implement the paging data from the api. 
Enough of what is missing, let's talk about what has been done so far.

The application in its current state can display the list of users fetched from the dummyapi API. 
In addition to the list feature, it is possible to click on a user and fetch its details through another API.

An offline mode is supported, the application have a cache first strategy so it will show cached data until a refresh has been manually triggered by the user. 
I used this caching strategy in order to both have most of the feature working in an offline mode and also to enforce a lower data consumption.

### Technical choices

I choose to use Gradle KTS to manage the project configuration, while it is a powerful tool that allow autocompletion, it also provide all the features of Kotlin language

I used Koin to manage the dependency injection. This library has its pros (simplicity, kotlin-based, reduce verbosity) and of course its cons (runtime dependency resolution)

I used Kotlin Flow to communicate the data between the different layers because it is a native Kotlin API and offers better integration with "reactive" operators.
It also avoid to have android objects like LiveData in the domain and make testing easier.

I used the navigation library combined with the SafeArgs plugin which makes the transition between the user list Fragment and the user detail Fragment really convenient and offers safety at compile time when serializing the user information through the bundle.

I used a Pull to Refresh layout to get user interaction for refreshing data from the API.

I implement the new SplashScreen API, which is backward compatible with previous android version.

Retrofit makes it really easy and scalable to interact with REST APIs.

I chose Glide to download the user images to the ImageView as it is reliable and reduces the complexity of such operations.

Room is the most popular and supported ORM and integrates perfectly with the rest of the Jetpack components.

Finally, I like using JUnit5 and Mockk as testing libraries. JUnit5 offers a lot of functions (@Nested test classes, customizable @ParameterizedTest...) and Mockk gracefully replaces Mockito for Kotlin projects.

### Architectural decisions

I tried to implement the Clean Architecture following the SOLID principles.
It makes the applications scalable in different way. For instance, there is no business logic within the presentation layer and the code is, as much as possible, decoupled from the Android framework (which makes it testable).

Here I choose to split my folder structure by feature, and then in subfolder by layer (domain, data, presentation, di) in order to be more explicit and make the architecture scream !! (Hello Uncle Bob)
There are also following reasons that convince me to drive my architecture by feature: 

- Higher Modularity
  Package-by-feature has packages with high cohesion, high modularity, and low coupling between packages.

- Easier Code Navigation
  Maintenance programmers need to do a lot less searching for items, since all items needed for a given task are usually in the same directory. Some tools that encourage package-by-layer use package naming conventions to ease the problem of tedious code navigation. However, package-by-feature transcends the need for such conventions in the first place, by greatly reducing the need to navigate between directories.

- Higher Level of Abstraction
  Staying at a high level of abstraction is one of programming's guiding principles of lasting value. It makes it easier to think about a problem, and emphasizes fundamental services over implementation details. As a direct benefit of being at a high level of abstraction, the application becomes more self-documenting: the overall size of the application is communicated by the number of packages, and the basic features are communicated by the package names. The fundamental flaw with package-by-layer style, on the other hand, is that it puts implementation details ahead of high level abstractions - which is backwards.
  
- Separates Both Features and Layers
  The package-by-feature style still honors the idea of separating layers, but that separation is implemented using separate classes. The package-by-layer style, on the other hand, implements that separation using both separate classes and separate packages, which doesn't seem necessary or desirable.

- Minimizes Scope
  Minimizing scope is another guiding principle of lasting value. Here, package-by-feature allows some classes to decrease their scope from public to package-private. This is a significant change, and will help to minimize ripple effects. The package-by-layer style, on the other hand, effectively abandons package-private scope, and forces you to implement nearly all items as public. This is a fundamental flaw, since it doesn't allow you to minimize ripple effects by keeping secrets.

- Better Growth Style
  In the package-by-feature style, the number of classes within each package remains limited to the items related to a specific feature. If a package becomes too large, it may be refactored in a natural way into two or more packages. The package-by-layer style, on the other hand, is monolithic. As an application grows in size, the number of packages remains roughly the same, while the number of classes in each package will increase without bound.


### Quality measurements

In order to implement quality standards, I added to the project some Gradle task for:

- Documentation, using Dokka plugin (Gradle Tasks/documentation/dokkaX)
- Linter and code smell checker, using KtLint and DeteKt (Gradle Tasks/verification/ktlintCheck and detekt)
- Coverage, using Jacoco (Gradle Tasks/verification/coverageReport and coverageVerification)

### Real work environment and improvements reflexion

I tried to split my commits so they are reviewable but it is possible to split in even smaller chunks of code.
In a real work environment, we probably would have groomed the work to split it into small subtasks with some acceptance criteria for each.

There are some improvements that I would like to add later: 
- As I said previously, the implementation of a pagination mechanism would reduce even more the data consumption.
- Another good approach would be to add dynamic feature modules for managing custom deliveries in order to improve scalability and performance. (I mean to not load useless feature)

### Source of inspiration

- Multiple way of defining Clean Architecture Layers: https://proandroiddev.com/multiple-ways-of-defining-clean-architecture-layers-bbb70afa5d4a
- Dynamic feature delivery: https://developer.android.com/guide/playcore/feature-delivery
- How to handle exception with Retrofit: https://www.ackee.agency/blog/retrofit-exceptions-with-retrofit
- Uncle bob screaming architecture: https://levelup.gitconnected.com/what-is-screaming-architecture-f7c327af9bb2
- Modular patterns: https://hackernoon.com/applying-clean-architecture-on-web-application-with-modular-pattern-7b11f1b89011
- Package by features with bounded contexts: https://reflectoring.io/java-components-clean-boundaries/ <3
- 5 most populars package structures: https://www.techyourchance.com/popular-package-structures/ <3

### Source code description

```yaml

# All the common, global and shared material and configuration
- /common
    # Dependencies injection configuration
    - /di
        - Module.kt
    # Global (shared) data sources, device apis services...
    - /data
        - /local
        - /remote
        - /database
        - /mapper
    # Global (shared) domain objects
    - /domain
        - /entity
        - /failure
        - /usecase
          # Base usecase class
          - UseCase.kt

# Feature package corresponding to an epic
- /feature1
    # Dependencies injection configuration
    - /di
        - Module.kt
    # Directory responsible for containing everything related to external services like databases, remote services, device apis, data providers
    - /data
        - /local
        - /remote
            # Source data model
            - XModel.kt
            # Contract service interface
            - IXService.kt
            # Service implementation
            - XService.kt
        - /mapper
    # The enterprise business rules that contains entities, failures, value objects and repositories abstractions
    - /domain
        # The entity (User, Activity...) and value objects (Password, Emails, Date...)
        - /model
        # The failures to handles exceptions
        - /failure
        # The abstraction part of the repository to return either a failure or an entity the the above layer
        - /repository
        # Contains use cases
        - /usecase
    # Directory responsible for containing activities, fragments, views, viewmodels
    - /presentation
        # The ui model used in the view
        - /model
        # The viewmodel of the view, controller / presenter
        - /viewmodel
        # The view to display activities, fragments, views
        - /view
        # Map in 2ways from entity to ui model
        - /mapper
# Main activity
- MainActivity.kt
- # Application class
- XApplication.kt
```