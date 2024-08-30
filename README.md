# Customer Management Application Overview

This Customer Management Application serves as a powerful tool for businesses looking to efficiently manage customer data with secure access and seamless integration capabilities. The application utilizes modern technologies such as **Spring Boot**, **MySQL**, and **JWT authentication**, along with a user-friendly frontend built using **HTML/CSS/JavaScript**. This makes it a comprehensive solution for organizations aiming to enhance their customer relationship management processes. It also includes a synchronization feature to fetch and update customer data from an external remote API using **RestTemplate**.


## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Frontend Functionality](#frontend-functionality)
- [Images](#images)

## Features

- **Role-Based Authentication**: Supports admin and user roles with appropriate access controls.
- **Admin Login**: log in securely.
- **View Customers**: Display a list of all customers.
- **Add Customer**: Allows admin to create new customer records with role "User".
- **Edit Customer**: Update existing customer information.
- **Delete Customer**: Remove customer records.
- **Search**: Filter customers by various criteria.
- **Data Synchronization**: Sync customer data with an external API.

## Technologies Used

- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Spring Boot, Java
- **Database**: MySQL 
- **Authentication**: JWT (JSON Web Tokens)

## Installation

### Prerequisites

- Java 8 or above
- Maven
- Spring
- MySql

### Backend Setup

1. Clone the repository:

    ```sh
    git clone https://github.com/Akash1yadavv/Sunbase-Data-Assignment.git
    ```

2. Navigate to the backend directory:
    ```sh
    cd Sunbase_Assignment_Backend
    ```
3. Run the command for creation database on mysql command line

    ```sh
    "create database sunbase"
    ```

4. Build the project with Maven:

    ```sh
    mvn clean install
    ```

5. Run the application:

    ```sh
    mvn spring-boot:run
    ```
     ```sh
    Running server on port 8080
    ```

### Frontend 

1. HTML

2. CSS

3. JavaScript

## Usage

### Login

1. Open the application in your web browser.
2. Navigate to the login page.
    ```sh
    https://jovial-dolphin-b5052d.netlify.app/
    ```

3. Enter These login credentials because whenever we run application then Admin credentials stored in database if admin not exist. 
    ```sh
    User Name:- admin@sunbasedata.com
    Password:- admin@123
    ```
    
   
5. Upon successful login, you will be redirected to the customer list page.

### Managing Customers

- **Add Customer**: Click the "Add Customer" button and fill out the popup form.
- **Edit Customer**: Click the edit button next to the customer you want to edit.
- **Delete Customer**: Click the delete button next to the customer you want to delete.
- **Search Customers**: Use the search bar to filter customers by keyword.

## API Endpoints

### Authentication

- `POST /api/sunbasedata/auth/login` - Login endpoint (Basic Auth)

### Customers

- `GET http://localhost:8080/api/sunbasedata/customers-list?page=0&size=1&sort=string` - Get all customers with pageble 
- `POST http://localhost:8080/api/sunbasedata/register-customer` - Add a new customer
- `DELETE http://localhost:8080/api/sunbasedata/delete-customer/{id}` - Delete a customer by ID
### Search and Filter Customers

**Endpoint:** `/search`  
**Method:** `GET`  
**Description:** Search and filter customers by various criteria.

**Query Parameters:**
- `searchTerm`: Filter by name (applies to both first and last name).
- `city`: Filter by city.
- `state`: Filter by state.
- `email`: Filter by email.
- `page`: Page number (default is 0).
- `size`: Page size (default is 10).
- `sort`: Sort by field (default is `first_name`).
- `dir`: Sort direction (`asc` or `desc`, default is `asc`).

**Example Request:**
     ``` GET http://localhost:8080/api/sunbasedata/search-customers?page=0&size=10&sort=firstName&dir=asc
     ```
### Sync

- `POST http://localhost:8080/api/sunbasedata/sync-customers` - Sync customers with the backend

## Frontend Functionality

### Event Listeners

- **Login Form**: Handles user login and stores JWT token In DOM.
- **Add Customer Button**: Open the register page and  submits new customer data to the backend.
- **Sync Button**: Syncs customer data with the backend.
- **Search Input**: Filters customer list based on search keyword.
- **Sort section**: sort customer list asc and desc order

### Functions

- **fetchCustomer**: Fetches and displays customer data and renders customer data in the table
- **displayCustomers**: Renders customer data in the table.
- **updateCustomer**: Update the customer data and reloads the customer list.
- **deleteCustomer**: Deletes a customer and reloads the customer list.
- **registerCustomer**: Register the customer and reloads the customer list.

## Images

## Screenshots

### Admin :-login

![logo](https://github.com/Akash1yadavv/Sunbase-Data-Assignment/blob/main/images/Login_Page.png)

### Customer list 

![logo](https://github.com/Akash1yadavv/Sunbase-Data-Assignment/blob/main/images/customer_list_page.png)

### Add Customer

![logo](https://github.com/Akash1yadavv/Sunbase-Data-Assignment/blob/main/images/Register_Cutomer_Page.png)

### update

![logo](https://github.com/Akash1yadavv/Sunbase-Data-Assignment/blob/main/images/Update_customer_page.png)

### Database schema

![logo](https://github.com/Akash1yadavv/Sunbase-Data-Assignment/blob/main/images/Database_Schema.png)

