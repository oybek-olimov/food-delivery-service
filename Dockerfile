FROM openjdk:21
EXPOSE 8080
ARG JAR_FILE=target/delivery-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} my-app
ENTRYPOINT ["java", "-jar", "my-app"]
