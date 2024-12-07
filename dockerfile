# Стадия сборки
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /workspace/app

COPY pom.xml .
COPY src src

RUN mvn package -DskipTests

# Стадия выполнения
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/app/target/*.jar app.jar

# ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/nutrition_db
# ENV SPRING_DATASOURCE_USERNAME=root
# ENV SPRING_DATASOURCE_PASSWORD=root


ENTRYPOINT ["java","-jar","app.jar"]