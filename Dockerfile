# Fetching latest version of Java
FROM openjdk:17
 
# Setting up work directory
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

# Copy the jar file into our app
# COPY ./mpapi-0.0.1-SNAPSHOT.jar /app

# Exposing port 8080
# EXPOSE 8080

# Starting the application
# CMD ["java", "-jar", "mpapi-0.0.1-SNAPSHOT.jar"]
CMD ["./mvnw", "spring-boot:run"]