# Sử dụng một image cơ sở có sẵn, ví dụ OpenJDK 11
FROM openjdk:21-jdk
WORKDIR /app
COPY target/BestPlaces-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
