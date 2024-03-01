# Builder stage
FROM maven:3.8.1-openjdk-17-slim AS builder

COPY . /usr/src/trip
WORKDIR /usr/src/trip

# Build your application
RUN mvn clean package

# Final stage
FROM openjdk:17-slim
VOLUME /tmp

# Install Dockerize
ENV DOCKERIZE_VERSION v0.6.1
RUN apt-get update && apt-get install -y wget \
    && wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy the built application from the builder stage
ARG JAR_FILE=/usr/src/trip/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar

# The ENTRYPOINT runs your application
ENTRYPOINT ["dockerize", "-wait", "tcp://discovery:8761", "-timeout", "60s", "java","-jar","/app.jar"]
