Payment Settlement System API
A robust backend service built with Spring Boot that simulates a payment settlement system. This API allows a single payer to settle debts with multiple payees in a single, atomic transaction, mirroring the functionality of split payment features in modern financial apps.

Features
Split Payments: Process a single payment from one user that is split and distributed among multiple other users.

Transactional Integrity: All settlements are fully transactional. If any part of the payment fails (e.g., an invalid payee or insufficient funds), the entire operation is rolled back to prevent data inconsistency.

Balance Inquiry: A dedicated endpoint to check the current balance of any user account.

SQL Database Integration: Uses MySQL for persistent data storage.

Clear API Structure: Built with a clean, layered architecture (Controller, Service, Repository).

Technologies Used
Backend: Java 17, Spring Boot 3

Database: MySQL

Data Persistence: Spring Data JPA (Hibernate)

Build Tool: Apache Maven

API Testing: Postman

Setup and Installation
Follow these steps to get the application running on your local machine.

Prerequisites
JDK 17 or newer

Apache Maven

MySQL Server

An API client like Postman

1. Clone the Repository
git clone [https://github.com/SABHYA18/settlement-system-application.git](https://github.com/SABHYA18/settlement-system-application.git)
cd settlement-system-application

2. Database Setup
Connect to your local MySQL server.

Create a new database for the project.

CREATE DATABASE settlementsdb;

The application is configured to automatically create the necessary tables (user_accounts) and insert the initial data on the first run using the schema.sql and data.sql files.

3. Configure the Application
Open the src/main/resources/application.properties file.

Update the spring.datasource.username and spring.datasource.password properties with your MySQL credentials.

spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

4. Run the Application
You can run the application using the Spring Boot Maven plugin:

mvn spring-boot:run

The API will start up and be available at http://localhost:8080.

API Endpoints
1. Process a Settlement
Processes a split payment from a single payer to multiple payees.

URL: /api/settlements

Method: POST

Request Body:

{
  "payerId": "A",
  "payees": [
    {
      "userId": "B",
      "amount": 300
    },
    {
      "userId": "C",
      "amount": 500
    },
    {
      "userId": "D",
      "amount": 400
    }
  ]
}

Success Response:

Code: 200 OK

Body: Settlement processed successfully.

Error Response:

Code: 400 Bad Request

Body: Insufficient funds in payer account A

2. Get User Balance
Retrieves the account details and balance for a specific user.

URL: /api/settlements/balance/{userId}

Method: GET

URL Parameter: userId (e.g., A)

Example Request:
GET http://localhost:8080/api/settlements/balance/A

Success Response:

Code: 200 OK

Body:

{
    "userId": "A",
    "balance": 800.00
}

Error Response:

Code: 404 Not Found

Body: User account with ID Z not found.
