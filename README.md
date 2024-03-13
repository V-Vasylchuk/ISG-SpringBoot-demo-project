# Simple-CAR-SHARING-Service

Car-Sharing-Service is a web application designed to solve car rental tasks.
The application is built on Spring Boot and utilizes Java,
adhering to the principles of the REST architectural style.

## Getting Started
* Clone the project repository to your local machine.
* Configure the database connection(your credentials) in the [resources/application.yaml](src/main/resources/application.yaml)  file.

Local build:
* Build the project: mvn compile
* Run application and visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.

Docker:
* Ensure Docker is installed and running
* Set your credentials in [.env](.env) file
* Build the project using the command: `mvn clean package`
* Run the command: `docker-compose up`
* Visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.
----------------------------------------------------------------------------------------------------

## Liquibase
By following these steps, you can manually perform migration and rollback using Liquibase in your Spring Boot application.

### Manual Migration:
* Make sure you have Liquibase installed on your computer. 
You can install it from the official website or use dependency management tools like Maven.
* Create a changelog file for each type of database migration. 
For example in my project it is: [create-cars-table.sql](src/main/resources/db/changelog/changes/create-cars-table.sql)
* Update the [db.changelog-master.yaml](src/main/resources/db/changelog/db.changelog-master.yaml) file to include your change log files.
* Execute Migration: Run the Liquibase command to execute the database migration. 
For example, using the command line or a script: `liquibase --changeLogFile=db.changelog-master.yaml update` .
This will execute all changes from the change log files in the specified order.

### Manual Rollback
* Run the Liquibase command to perform a rollback of the database migration.
Specify the number of steps to roll back. 
For example: `liquibase --changeLogFile=db.changelog-master.yaml rollbackCount 1` .
This will roll back the last applied change.
* Confirm Rollback: Confirm that the migration has been rolled back to the desired state by checking the database state.

------------------------------------------------------------------------------------------------------------------------

## Email notification

#### Configuration
1. First you need to create AppPassword in your gmail account using the following path:
> `Login to Gmail` > `Manage your Google Account` > `Security` > `App Passwords` > `Provide your login password` > `Select app with a custom name` > `Click on Generate`
2. Copy generated password and paste that to [application.yaml](src/main/resources/application.yaml) in a required field, don't forget about `username` too
3. In [AuthenticationController](src/main/java/com/vvs/demo/project/controller/AuthenticationController.java) you can change `RECIPIENT_EMAIL` 
or use [EmailTestController](src/main/java/com/vvs/demo/project/controller/EmailTestController.java) for testing