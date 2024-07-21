# Mail Bridge Archive

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Configuration](#configuration)
- [Endpoints](#endpoints)
- [Usage](#usage)
- [Contributing](#contributing)

## Introduction

- Mail Bridge Archive is a Spring Boot application designed to process emails and extract attachments, then parse and store the extracted data into a database. It supports multiple attachment types, including **CSV, Excel, PDF, and TXT**. Additionally, it provides endpoints for manual data insertion with authentication.

## Features

- Scheduled email checking and processing.
- Support for parsing **CSV, Excel, PDF, and TXT** attachments.
- Manual data insertion via **REST API** with authentication.
- Logging of processed data.
- Moving processed emails to specific folders based on success or failure.

## Technologies

- Java
- Spring Boot
- Spring Data JPA
- Swagger
- Logback
- Javax Mail
- Apache PDFBox
- Apache Commons CSV
- H2 Database (for development)
- MySQL (for production)
- Maven
- Test cases

## Prerequisites

- Java 11 or higher
- Maven 2.6.5 or higher
- MySQL database (for production)

## Setup

****1.Clone the repository:****

   ```bash
   git clone https://github.com/your-username/mail-bridge-archive.git
   cd mail-bridge-archive
   ```
##### Build the project:

* __`mvn clean install`__
##### Run the application:

* __`mvn spring-boot:run`__
## Configuration
##### Application Properties
Configure your email and database settings in `src/main/resources/application.properties`.

### properties
```java
# Email configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password

# Database configuration (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Authentication key
app.auth.key=your_auth_key_here
```
# MySQL Configuration
#### For production, configure MySQL settings:

#### properties

```java
spring.datasource.url=jdbc:mysql://localhost:3306/mail_archive_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Endpoints
##### Process Mail Endpoint
* **URL:**`/api/mail/process`
* **Method:** POST

****Request Body:****
~~~json
{
  "subject": "Mail Archive"
}
~~~

****Response:****

* **200 OK:** Mail processed successfully.
* **500 Internal Server Error:** Error processing mail.
##### Manual Insert Endpoint
* **URL:** `/api/mail/manual-insert`
* **Method:** `POST`
* **Headers:** Auth-Key: ` your_auth_key_here`
##### Request Body:


```json   
    {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "mobile": "1234567890",
      "location": "New York",
      "description": "Sample description",
      "status": "active",
      "startDate": "2024-07-01 10:00:00",
      "endDate": "2024-07-01 12:00:00",
      "assignedTo": "Admin",
      "comments": "No comments",
      "lastUpdated": "2024-07-01 10:00:00",
      "type": "Type A",
      "hold": "false"
     } 
```
##### Response:
- **200 OK:** Data inserted successfully.
- **403 Forbidden:** Invalid authentication key.
- **500 Internal Server Error:** Error inserting data.

## Usage

##### 1.Scheduled Mail Processing:

- The application will automatically check and process emails at the interval specified in the `@Scheduled` annotation in the MailService class (currently set to every 10 minutes).

##### 2.Manual Mail Processing:

- Use the `/api/mail/process` endpoint to trigger mail processing manually.

##### 3.Manual Data Insertion:

- Use the `/api/mail/manual-insert` endpoint to insert data manually with the proper authentication key.
 
### Email Notification Sender

- #### The application will send email notifications for the following cases:

- **Failed Mail Processing:** An email will be sent to the recipient with the subject `"Failed Mail Processing"` and the body containing the error message.
- **Unauthorized Access:** An email will be sent to the recipient with the subject `"Unauthorized Access Attempt"` and the body containing the authentication key used.
- **Exception Occurred:** An email will be sent to the recipient with the subject `"Exception Occurred"` and the body containing the exception message.

### Swagger Integration
- Swagger UI is integrated to provide API documentation. Access Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

### Test Cases
- Unit test cases are implemented to verify the functionality of service methods, repository operations, and controller endpoints.

### Logging Configuration (Logback)
- Logback is configured to provide live logging and daily log rotation. Logs are stored in `logs/mail-bridge-archive.log` and rotated daily.
## Contributing:
- Fork the repository.
- **Create a feature branch:** `git checkout -b` feature-name
- **Commit your changes:** `git commit -m` 'Add feature'
- **Push to the branch:** `git push origin` feature-name
- Open a pull request.

### Notes:
- Replace `your-username` and `your-repo-name` with your actual GitHub username and repository name.
- Ensure all configurations, such as the `application.properties`, match your actual setup.
- Update the sections to better reflect any additional features or specific instructions for your project.