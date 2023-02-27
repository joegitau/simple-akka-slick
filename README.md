# Simple AKKA & SLICK API
This is a simplified - one entity api - that exemplifies how once can architect an app using Akka typed Actors and 
Slick ORM (Object-relational Mapping).

Basically, lets you perform CRUD operations for an entity `Player`. Data is saved in a dockerized postgres database.

The api features these layers:
1. DAO (Data Access Object) - provides an abstraction layer that separates the database operations from the application logic.
2. Database layer - `Postgres`
3. Service layer
4. Akka typed Actors - act as Controllers
5. Akka HTTP
6. Webserver

### Benefits of DAOs
1. Encapsulation: encapsulate the persistence logic of the application, which means that the details of how the data is stored and retrieved are hidden from the rest of the application code.
2. Modularity: This makes it easier to test, maintain, and extend the application
3. Scalability: By separating the database access code from the application logic, you can focus on optimizing each layer independently.

### Benefits of using Actors instead of Controllers as intermediary between the Service layer & Routes
1. Concurrency: actors process messages asynchronously, allowing for a high degree of parallelism.
2. Fault tolerance: improves the reliability and resilience of the application
3. State management: actors can maintain state internally, which can simplify the implementation of complex business logic. 

*Visit `_notes/summary.md` for more in depth explanations*
