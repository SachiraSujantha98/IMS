# Image Management System (IMS)

A Spring Boot application for managing medical imaging workflow, from image upload to diagnosis and financial processing.

## Features

- **User Management**
    - Multiple user roles: Admin, Doctor, Patient, Radiologist, Finance
    - Secure login and session management
    - User registration (Admin only)

- **Patient Management**
    - Patient registration with doctor assignment
    - Patient medical history tracking
    - View test results and prescriptions

- **Doctor Features**
    - View assigned patients
    - Recommend medical tests
    - View test results
    - Add prescriptions

- **Radiologist Features**
    - View pending tests
    - Upload test images
    - Mark tests as completed

- **Finance Features**
    - View completed tests
    - Assign costs to tests

## Technical Stack

- Java 17
- Spring Boot 3.4.1
- MySQL Database
- Thymeleaf Template Engine
- Bootstrap 5.3
- Maven

## Prerequisites

- JDK 17 or later
- MySQL 8.0 or later
- Maven 3.6 or later
- IntelliJ IDEA (Ultimate Edition recommended)

## Database Configuration

The application uses MySQL. You need to:

1. Install MySQL
2. Create a database (optional, application will create it)
3. Update `application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Installation & Setup

- Click the Run button (Play icon) in the top of IntelliJ IDEA

or

1. Navigate to project directory:
```bash
cd hospital-management-system
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the application at: `http://localhost:8080`

## Initial Setup

1. On first run, the system creates an admin user:
    - Username: admin
    - Password: admin123

2. Use these credentials to log in and create other users

## Project Structure

```
src/main/java/com/hospital/
├── config/
│   └── WebConfig.java
|   └── ApplicationConfig.java
├── controller/
│   ├── AdminController.java
│   ├── DoctorController.java
│   ├── FinanceController.java
│   ├── LoginController.java
│   ├── PatientController.java
│   └── RadiologistController.java
├── model/
│   ├── MedicalTest.java
│   ├── Patient.java
│   ├── TestType.java
│   ├── User.java
│   └── UserRole.java
├── repository/
│   ├── MedicalTestRepository.java
│   ├── PatientRepository.java
│   └── UserRepository.java
├── service/
│   ├── MedicalTestService.java
│   ├── PatientService.java
│   └── UserService.java
└── HospitalApplication.java
```

## Medical Test Workflow

1. Doctor recommends a test
2. Radiologist sees the pending test
3. Radiologist uploads test image
4. Doctor views test and adds prescription
5. Finance department adds cost
6. Patient can view complete test details

## Image Storage

The system stores medical test images in the database as BLOB data:
- Supports various image formats
- Images are served through a dedicated endpoint
- Maximum file size: 10MB

## Security Considerations

- Session-based authentication
- Role-based access control
- Secure password handling
- Protected endpoints
- Input validation



