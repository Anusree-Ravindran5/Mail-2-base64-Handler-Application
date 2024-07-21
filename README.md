# Mail 2 Base64 Handler

## Introduction

- Mail 2 Base64 Handler is a Spring Boot application designed to process incoming emails, convert their contents to Base64 encoding, and handle various data processing tasks. The application aims to streamline email handling by converting email content into Base64 format, facilitating easier storage and transmission. It also provides endpoints for managing and processing email data.

## Features

- Email Processing: Reads incoming emails and converts their content into Base64 format.
- Email Processing: Reads incoming emails and converts their content into Base64 format.
- Error Handling: Provides detailed logging and notification for errors encountered during email processing.
- Logging of processed data.
- Notification System: Sends notifications for errors and exceptions encountered during processing.

## Technologies

- Java
- Spring Boot
- Spring Data JPA
- Logback
- Javax Mail
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
   https://github.com/Anusree-Ravindran5/Mail-2-base64-Handler-Application.git
   cd Mail-2-base64-Handler-Application
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
```
# MySQL Configuration
#### For production, configure MySQL settings:

#### properties

```java
spring.datasource.url=jdbc:mysql://localhost:3306/Your_DB
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Endpoints
##### Process Mail Endpoint
* **URL:**`/api/emails/processUnread`
* **Method:** GET


****Response:****

* **200 OK:** Successfully read and processed unread emails.
* **500 Internal Server Error:** "Failed to read and process unread emails: [error message].

## Usage

##### 1.Scheduled Mail Processing:

- The application will automatically check and process emails at the interval specified in the @Scheduled annotation in the MailService class (currently set to every 10 Seconds).

##### 2.Manual Mail Processing:

- Use the /api/emails/processUnread endpoint to trigger mail processing manually. This endpoint reads and processes unread emails from the mailbox.

### Email Notification Sender

- #### The application will send email notifications for the following cases:

- **Failed Mail Processing:** Error During Mail Processing: An email will be sent to the recipient with the subject `"Error Notification"` and the body containing the error message and email body.
- **Exception Occurred:** An email will be sent to the recipient with the subject `"Exception Occurred"` and the body containing the exception message.

### Test Cases
- Unit test cases are implemented to verify the functionality of service methods, repository operations, and controller endpoints.

### Logging Configuration (Logback)
- Logback is configured to provide live logging and daily log rotation. Logs are stored in `logs/Mail_2_base64_Handler.log` and rotated daily.
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
