# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY ./target/product-0.0.1-SNAPSHOT.jar product.jar

# Expose port 9090 for the application
EXPOSE 9090

# Specify the command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "product.jar"]
