# Use the official OpenJDK image as a base image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the Gradle build directory to the container
COPY build/libs/userService-1.0-SNAPSHOT.jar .
 
# Specify the command to run your application
CMD ["java", "-jar", "userService-1.0-SNAPSHOT.jar"]