# Stock Manager

Stock Manager is a robust **Java-based application** developed to efficiently manage stock inventories and customer data within a simplified interface.  
The system provides functionalities for **stock management, customer management, and stock transactions** while ensuring persistent data storage through **Oracle database integration**.

## Key Features

### Stock Management
**Add New Stock**: Add stocks with details like name, symbol, price, and quantity.  
**Remove Stock**: Delete stock entries using their unique symbol.  
**Update Stock**: Modify stock details such as price and quantity.  
**Retrieve Stock**: Fetch existing stock records.

### Customer Management
**Add New Customer**: Register customers with details like name, customer ID, and email.  
**Remove Customer**: Delete a customer using their unique ID.  
**Update Customer**: Modify customer records.  
**Retrieve Customer**: Fetch customer records.

### Stock Transactions
**Purchase Stocks**: Customers can buy stocks. Updates inventory and logs the transaction in the customer’s history.  
**Sell Stocks**: Customers can sell purchased stocks. Adjusts inventory and records the sale in the customer’s history.  


## 💾 Data Persistence
The application integrates with an **Oracle Database** to store stock, customer, transaction and users data persistently.  
This ensures data integrity and availability across sessions.

## 🛠️ Technologies Used
**Programming Language**: Java 17  
**Platform**: Java EE 7, Spring Boot 3.5.5  
**Server**: Apache Tomcat 10  
**Database**: Oracle 19c
**Architecture**: Object-Oriented Design (OOD)

## Optional Reports
The application can generate the following reports :  
1. List of customers, stock details, and their total assets including transaction type  
2. Customer with maximum asset value  
3. Customer with minimum asset value  
4. Stock transacted the most  
5. Stock transacted the least  
6. Stock with the highest price  
7. All stock details  
8. All customer details  
9. All transaction details  
10. Transaction type summary (buy vs sell)  
11. Total assets of all customers  


### The application is also enhanced with Role-based access for Admin and Customers

## Author - Rachita J
Developed as a Java-based project with focus on **scalability, maintainability, and robust Oracle DB integration**.  
