FROM openjdk:21

WORKDIR /app
COPY target/candy_shop-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]