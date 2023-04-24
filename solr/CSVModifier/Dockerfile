FROM openjdk:8-jdk-alpine AS build
WORKDIR /workspace/app

RUN apk add --no-cache maven
RUN apk add --no-cache zip
COPY pom.xml .
COPY src src
RUN mvn install
RUN zip -d ./target/CSVModifier-1.0-SNAPSHOT.jar  'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*SF'

FROM openjdk:8-jdk-alpine
COPY --from=build /workspace/app/target/CSVModifier-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]