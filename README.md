# db-challenge-rishi
   
INTRODUCTION
------------   
   
The DB Challenge displays a simple REST service with some very basic functionality - to add and read an account.
It is a standard gradle project, running on Spring Boot. 
There are functionality for a transfer of money between accounts.
Transfers are specified by providing accountFrom id,accountTo id and amount to transfer between accounts.

KEY FEATURES INTRODUCED
------------------------
* Implemented the code as per the SOLID principles so that existing code do not get impacted
* Added functionality to transfer of money between accounts
* Written custom exception for all the error conditions required to transfer the amount
* NotificationService is called for both source and target account holders so as to notify the users
* Written test cases using Junit for the implemented functionalities
* Implemented Concurrency using lock and written test cases for concurrency
* Coverage of the code has been taken care and is above 90 percent
* Used annotations for validation, tests structure, layers, logging etc.
* Used constants rather than hardcoded values
* Properties files written for DEV, UAT,PROD


ADDITIONAL WORK THAT COULD BE IMPORTANT BEFORE PRODUCTION
----------------------------------------------------------

 * SonarQube could be used to improve quality of code
 * Some Integration tests could be written for the end points
 * Local openshift could be used to run the Integration tests
 * Scripts can be written to run the tests on local openshift
 * status.sh can be written to check the status of application each time
 * User may also need to provide credential to run status.sh
 * health.sh can be used to check the health of application
 * Simultaneaously push.sh,deploy.sh,applicationTest.sh could be written
 * Swagger could be written for the end point
 * Swagger validation could be done in API gateway
 * ISAM policies could be written for the endpoint
 
  IMPORTANT COMPONENTS OF THIS APPLICATION
 --------------------------------------
* DevChallengeApplication is used as a SpringBootApplication class having the main method  so as to run the application.
* AccountsRepository contains three methods:
    1)createAccount->  It takes basically account as the parameter where account id should be unique to create a new account
                   ->  If the account id already exists then it will throw DuplicateAccountIdException and account will not be created
    2)getAccount   ->  It  basically fetches the account based on account id
    3)clearAccounts->  It is used to clear the accounts

* AccountsService : Public method amountTransfer  uses private methods debit, credit and notifyCustomer to implement amount transfer logic
* EmailNotificationService : Logic is yet to be implemented by another developer


System requirements:
-------------------------
Gradle version = 4.1 
Java version = 1.8
springBootVersion = 1.5.4


