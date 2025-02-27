# 1. OpenJDK 21 기반으로 빌드
FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY . .
RUN ./mvnw clean package -DskipTests

# 2. 실행 컨테이너 설정
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 3. 환경 변수 설정 (MySQL RDS 연결)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://clothcast-db.cnokm2w0uwtf.us-east-1.rds.amazonaws.com:3306/clothcast_db
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=admin1234!!
ENV SPRING_PROFILES_ACTIVE=production

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
