# Transaction Analyser Service

## Overview
Transaction Analyser Service is a Demo Project to demonstrate:

1. Simple Spring Boot Microservice to analyse financial transaction records and calculates **relative account balance** for an account for a given time frame.
2. Use of In-memory Database (H2).
3. Use of Bucket4J (Rate limiting library)

## Running the Service
Clone the project and run the following command:

mvn clean package

The executable Jar file is now available in the target directory. Start the service using following command:

java -jar TransactionAnalyser-0.0.1-SNAPSHOT.jar

#End Points
Use following end points to access the service

1. Accounts (http://localhost:8080/api/accounts)
   Request Type: Get

2. Transactions (http://localhost:8080/api/transactions)
   Request Type: Get

3. Transactions By Account (http://localhost:8080/api/transactionsByAccount?accountId=ACC334455)
   Request Type: Get

4. Relative Balance (http://localhost:8080/api/relativeBalance)
   Request Type: Post
   Request Body Type: JSON
   Request Body:
   {
	"accountId": "ACC334455",
	"fromDate": "2020-10-20 12:00:00",
	"toDate": "2020-10-20 19:00:00"
   }

## Model
Model for Transaction Analyser service consists of following Entities:
1. Account - Represents Account table in Database
2. Transaction - Represents Transaction table in Database
3. TransactionAnalyserRequest - Represents request object for relativeBalance request
4. TransactionAnalyserResponse - Represents response object for relativeBalance response

## Rate Limiter
Bucket4J is used to implement the Rate Limitation with bucket size of 10 requests per minute for endpoint /relativeBalance. Endpoint /relativeBalance will process 10 requests per minute. Service will respond with status 429 - Too many requests and a header 'X-Rate-Limit-Retry-After-Seconds'  if there will be more than 10 requests per minute 

## Exception Handling
A custom Global Exception Handler controller is being used for custom error messages.
