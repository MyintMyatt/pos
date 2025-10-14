FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

COPY src src

RUN ./mvnw clean package -DskipTests

# create imatge
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/pos-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
