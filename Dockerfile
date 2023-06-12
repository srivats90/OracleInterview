FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY target/TaskerApp-0.0.1-SNAPSHOT.jar /opt/app
COPY config.yml /opt/app
WORKDIR /opt/app
RUN java -version
CMD ["java", "-jar", "/opt/app/TaskerApp-0.0.1-SNAPSHOT.jar", "server", "/opt/app/config.yml"]
EXPOSE 8080-8081