# 1. Koristimo laganu verziju Jave (OpenJDK 17)
FROM eclipse-temurin:17-jdk-alpine

# 2. Postavljamo radni direktorijum unutar kontejnera
WORKDIR /app

# 3. Kopiramo onaj .jar fajl iz target foldera u kontejner pod imenom app.jar
COPY ./target/*.jar app.jar

# 4. Kažemo kontejneru na kom portu aplikacija sluša (Spring Boot je obično 8080)
EXPOSE 8445

# 5. Komanda koja pokreće aplikaciju
ENTRYPOINT ["java", "-jar", "app.jar"]