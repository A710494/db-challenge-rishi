# db-challenge-rishi
   
INTRODUCTION
------------   
   
The DB Challenge displays a simple REST service with some very basic functionality - to add and read an account.
It is a standard gradle project, running on Spring Boot. 
There are functionality for a transfer of money between accounts.
Transfers are specified by providing accountFrom id,accountTo id and amount to transfer between accounts.

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
 
