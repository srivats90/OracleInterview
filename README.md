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



Run with Docker locally
---

1. Build the docker image. Dockerfile is available in the project

docker  build -t tasker-application .

2. Create docker networking to enable communication between the database and the application containers

 docker network create demo
 
3. Pull the mysql docker image and run the mysql docker container with database tasker
 
 docker pull mysql:8.0.32
 docker run -d --name mysql_0 --network demo --network-alias mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=tasker mysql:8.0.32
 
4. You can check the container status and stop container using below commands
 
docker ps
docker stop mysql_0
docker rm mysql_0

5. Run the tasker application in the same network
docker run --name tasker_app -p 8080-8081:8080-8081 -t tasker-application --network demo


Deploy in OCI

1. 




