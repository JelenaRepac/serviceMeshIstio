# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY ./target/notification-api-0.0.1-SNAPSHOT.jar notification.jar

# Expose port 8080 (since your app runs on 8080)
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "notification.jar"]
