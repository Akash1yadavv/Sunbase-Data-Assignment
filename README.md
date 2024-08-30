# Customer Management Application Overview

This Customer Management Application serves as a powerful tool for businesses looking to efficiently manage customer data with secure access and seamless integration capabilities. The use of modern technologies like Spring Boot, MySQL, and JWT authentication, coupled with a user-friendly frontend built with HTML/CSS/JavaScript, makes it a comprehensive solution for any organization aiming to enhance its customer relationship management processes and synchronization feature to fetch and update customer data from an external remote API using RestTemplate.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Frontend Functionality](#frontend-functionality)
- [Images](#images)

## Features

- User authentication with JWT tokens and session expired after one hour
- Add, edit, delete customer records
- Search customer records
- Sync customer records from remote api
- Responsive design

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

3. Build the project with Maven:

    ```sh
    mvn clean install
    ```

4. Run the application:

    ```sh
    mvn spring-boot:run
    ```

### Frontend 

1. HTML

2. CSS

3. JavaScript

## Usage

### Login

1. Open the application in your web browser.
2. Navigate to the login page.
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

- `GET /api/sunbasedata/customers-list?page=0&size=1&sort=string` - Get all customers 
- `POST /api/sunbasedata/register-customer` - Add a new customer
- `GET /api/sunbasedata/search-customers?page=0&size=10&sort=firstName&dir=asc` - Search customers by keyword
- `DELETE /api/sunbasedata/delete-customer/{id}` - Delete a customer by ID

### Sync

- `POST /api/sunbasedata/sync-customers` - Sync customers with the backend

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


