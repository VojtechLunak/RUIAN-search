FROM maven:3-eclipse-temurin-11 as build

WORKDIR /ruian-search

COPY pom.xml pom.xml

RUN mvn -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

COPY src src
RUN mvn package -B -DskipTests=true

FROM eclipse-temurin:11-jdk-alpine as runtime
COPY --from=build  /ruian-search/target/ruian-search-*.jar ruian-search.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/ruian-search.jar"]
