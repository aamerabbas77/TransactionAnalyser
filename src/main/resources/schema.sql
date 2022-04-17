/**
 * Author:  Aamer Abbas
 */
Create table Accounts(
account_id Varchar(32) PRIMARY KEY,
first_name Varchar(50),
last_name Varchar(50),
address Varchar(100),
balance float(8)
);

Create table Transactions(
 transaction_id Varchar(32) PRIMARY KEY,
 from_account Varchar(50),
 to_account Varchar(50),
 creation_time TIMESTAMP,
 amount float(8),
 transaction_type Varchar(50),
 related_transaction Varchar(32)
);