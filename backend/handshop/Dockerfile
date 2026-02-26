# ---------- STAGE 1: BUILD ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# копируем pom.xml отдельно (кэширование зависимостей)
COPY pom.xml .
RUN mvn dependency:go-offline

# копируем весь проект
COPY src ./src

# собираем jar
RUN mvn clean package -DskipTests


# ---------- STAGE 2: RUN ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# копируем jar из builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]