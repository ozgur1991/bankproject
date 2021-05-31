# bankproject
A simple Bank Application

This is a simple Bank Application. It consists of almost all banking operations such as: money transfer, balance , deposit,bank statement  etc.
It connects to a Customer database which is already created on DB SQL .
 The customer's personal information and account info is recorded in customer table and account table. Those tables are connected to each other with account number foreign key.
Customer username and password is also recorded in DB. Once logged in, customer menu is seen on the screen.

Each banking operation is given a transaction number right after the operation is completed. Therefore , each transaction has a unique transcation number which is also the primary key in transcations table in DB.

