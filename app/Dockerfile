FROM amazoncorretto:11
RUN mkdir -p /opt/application
WORKDIR /opt/application

COPY /target/sample-rest-api-1.0.0-SNAPSHOT-runner.jar app.jar

ENTRYPOINT ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-XshowSettings:vm", "-showversion", "-jar", "app.jar"]
CMD []
