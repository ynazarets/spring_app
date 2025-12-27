# Online Bookstore API

The Online Bookstore API is a robust solution designed to digitize  
the book purchasing experience. It addresses the challenge of managing   
a large inventory of books, providing a seamless shopping cart experience  
for customers and a secure management dashboard for administrators.

## 🛠 Technologies & Tools
* Framework: Spring Boot 3.x
* Security: Spring Security & JWT (JSON Web Token)
* Data Access: Spring Data JPA
* Database: MySQL (Production/Dev), H2 (Testing)
* Migration: Liquibase
* Documentation: Swagger UI (SpringDoc)
* Mapping & Utils: MapStruct, Lombok
* Containerization: Docker & Docker Compose
* Testing: JUnit 5, Mockito, Testcontainers

## 🟢️ Running the Project
1. Clone the repository
2. Run `mvn clean package`
3. Run via Docker: `docker-compose up`

## 📖 API Documentation
The Swagger UI is available at: http://localhost:8081/swagger-ui/index.html.

## 📦 Key Features
* Authentication & Authorization: Secure login/registration via JWT. Role-based access control  
  (USER for customers, ADMIN for inventory management).
* Book Inventory: Full CRUD operations for books with pagination, sorting, and advanced searching capabilities.
* Shopping Cart System: Persistent shopping carts where users can add, update, or remove items.
* Order Processing: Automated conversion of cart items to orders with shipping information and status tracking.
* Category Management: Organize books into logical categories for better navigation.
  

## 🗺️ Simplified project diagram
![Project Diagram](https://github.com/user-attachments/assets/e9635b2c-2237-49aa-9de6-139308f7a7be)

## 🛣️ EndPoints List

> **Note:** Once the application is running, you can access the interactive UI at: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

![Swagger UI Screenshot](https://github.com/user-attachments/assets/35f561a6-5686-4c08-bb95-5fdb0aaf8f37)

## 📬 Postman Collection
The collection file is located in the `postman/Book Store API.postman_collection.json` folder.

### Instructions:
1. Import the collection into Postman.
2. In the **Authentication** folder, select the **Login as ADMIN** or **Login as USER** request.
3. Click **Send**. The script in the **Scripts / Post-response** tab will automatically update the token in the collection variables.
4. Now you can make any requests—the token will be inserted automatically.  
(The token is valid for 5 minutes. After receiving a 403 error, you must resend the request.)

### 🔑 Test data:
* **Admin:** `administrator@library.com` / `123456789`
* **User:** `user@library.com` / `user1234`

## 🧠 Challenges & Solutions
* Foreign Key Constraint Management:
    * ❌ Challenge: Faced issues with database cleanup during  
  integration testing due to strict referential integrity.
    * ✅ Solution: Optimized SQL cleanup scripts to delete data in a   
  specific hierarchical order (child tables before parent tables) ensuring consistent test environments.

* Dynamic Security Configurations:
    * ❌ Challenge: Correctly mapping Swagger UI resources within Spring Security  
  filter chains to prevent 403 Forbidden errors.
    * ✅ Solution: Implemented precise requestMatchers to permit   
  access to API documentation while keeping business logic endpoints strictly authenticated.
* Automated Token Management:
    * ❌ **Challenge:** Manually updating JWT tokens every 5 minutes (due to `jwt.expiration` settings)  
  was inefficient for testing and recording demos.
    * ✅ **Solution:** Created a Postman collection with a **Post-response script**   
  that automatically captures the token from the login response and updates a collection-wide variable `{{jwt_token}}`.
