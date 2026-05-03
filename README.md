Personal Fincnce API 

A RESTful backend API for managing personal finances, including accounts, transactions, budgets, and analytics.

Built with Java and Spring Boot, this project demonstrates backend architecture, authentication, database design, and financial data processing.

Features 
- JWT-based authentication and authorization
- Account management (create, update, delete, view)
- Transaction tracking (income and expenses)
- Category orginization
- Budget creation and enforcement
- Analytics endpoints (e.g., budget usage)
- Integration testing using MockMvc

Tech Stack
Backend: Java, Spring Boot
Database: MySQL, H2
Data Access: JDBC
Authentication: JSON Web Tockens (JWT)
Testing: MockMvc

Architecture
This project follows a layered architecture: controller, service, repositry and database.
Controller: Handles HTTP requests and responses
Service: Contains business logic
Repository: Excecutes SQL queries

Authentication
All protected endpoints require a JWT token

Future Improvements
- Monthly spending summaries
- Budget ovverrun alerts
- Dashboard-style analytics
- Frontend integration

What I Learned
- Designing RESTfull APIs with the proper layering
- Impelemmnting secure authentication using JWT
- Writing SQL queries for filtering and aggregation
- Managing data consistency across related entites
- Writing integration tests for real-world backend systems

