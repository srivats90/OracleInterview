# Tasker Application
---

Tasker is a tasks management system.
It helps you to organize your tasks with dates

Overview
---

This application is comprised of the following classes:

The TaskDAO uses the Data Access Object pattern with assisting of Hibernate to connect to a MySQL database.

The Task class shows the entity and its properties which maps to database tables with the help of JPA annotations.

The TaskResource is the REST resource which uses the TaskDAO to retrieve data from the database. Task DAO is injected in the constructor of TaskResource

The database is wired up in the initialize function of the TaskerApplication.

TaskerConfiguration reads the config file and provides it to the application.


Compile and run the tasker application
---

1. Run `mvn clean install` to build your application

2. Start application with `java -jar target/java-simple-0.0.1-SNAPSHOT.jar server config.yml`

3. To check that your application is running enter url `http://localhost:8080/api/task`


Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


