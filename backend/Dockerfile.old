FROM openjdk:8-jdk-alpine AS build
ARG RUIAN_SOLR_URL
WORKDIR /workspace/app

RUN apk add --no-cache maven
COPY pom.xml .
COPY src src
RUN mvn install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/app/target/dependency/META-INF /app/META-INF
COPY --from=build /workspace/app/target/dependency/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*", "cz.cvut.fit.ruiansearch.RuianSearchApplication"]