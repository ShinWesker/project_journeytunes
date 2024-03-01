FROM maven:3.8.1-openjdk-17-slim AS builder

COPY . /usr/src/user
WORKDIR /usr/src/user

RUN mvn clean package

FROM openjdk:17-slim
VOLUME /tmp
ARG JAR_FILE=/usr/src/user/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

WORKDIR /usr/app
CMD ["java", "-jar", "app.jar"]