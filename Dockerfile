FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/scala-2.13/*assembly*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.datasource.url=${DB_HOST}","--spring.datasource.username=${DB_USER}","--spring.datasource.password=${DB_PASSWORD}"]