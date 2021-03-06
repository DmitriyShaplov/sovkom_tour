FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11

ENV ws.endpoint=http://localhost:9080/ws
ENV rs.endpoint=http://localhost:9080
ENV server.port=9081

RUN mkdir /app
WORKDIR /app
COPY --from=build /home/app/target/starter-kit.jar /app/starter-kit.jar
EXPOSE 9081
HEALTHCHECK --interval=10s --timeout=12s --retries=3 CMD curl -sS 127.0.0.1:9081 || exit 1
ENTRYPOINT ["java", "-jar", "/app/starter-kit.jar"]