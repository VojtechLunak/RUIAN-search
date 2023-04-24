#FROM solr:9.2
FROM openjdk:8-jdk-alpine AS csv-modifier-build
WORKDIR /workspace/app

RUN apk add --no-cache maven
RUN apk add --no-cache zip
COPY ./CSVModifier/pom.xml .
COPY ./CSVModifier/src src
RUN mvn install
RUN zip -d ./target/CSVModifier-1.0-SNAPSHOT.jar  'META-INF/*.SF' 'META-INF/*.RSA' 'META-INF/*SF'

FROM solr:8.3

USER root
RUN mkdir -p /opt/csv-modifier/
COPY --from=csv-modifier-build /workspace/app/target/CSVModifier-1.0-SNAPSHOT.jar /opt/csv-modifier/csv-modifier.jar

COPY ./coreConfig  /opt/solr/server/solr/configsets/ruianConfig

RUN mkdir -p /opt/ruian-solr-scripts/
COPY ./initial-update.sh /opt/ruian-solr-scripts/
RUN chmod +x /opt/ruian-solr-scripts/initial-update.sh
COPY ./update.sh /opt/ruian-solr-scripts/
RUN chmod +x /opt/ruian-solr-scripts/update.sh
COPY ./ruian-solr.sh /opt/ruian-solr-scripts/
RUN chmod +x /opt/ruian-solr-scripts/ruian-solr.sh
RUN ln -s /opt/ruian-solr-scripts/ruian-solr.sh  /opt/solr/bin/ruian-solr

#RUN test -z "$SOLR_UID" && exit -1
#USER $SOLR_UID

#ENTRYPOINT ["java","-jar", "/opt/csv-modifier/csv-modifier.jar"]
#WORKDIR /opt/solr
EXPOSE 8983
ENTRYPOINT ["docker-entrypoint.sh"]
#CMD ["solr-foreground"]

USER root
CMD ["ruian-solr" ]
#CMD ["solr-precreate", "ruian", "/opt/solr/server/solr/configsets/ruianConfig"]


