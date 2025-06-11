ARG DEPENDENCY=extracted

FROM maven:3.9.9-eclipse-temurin-21 AS builder
ARG DEPENDENCY
WORKDIR ${DEPENDENCY}
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM builder AS launcher
WORKDIR application
ARG DEPENDENCY
COPY --from=builder ${DEPENDENCY}/dependencies/ ./
COPY --from=builder ${DEPENDENCY}/spring-boot-loader/ ./
COPY --from=builder ${DEPENDENCY}/snapshot-dependencies/ ./
COPY --from=builder ${DEPENDENCY}/application/ ./
EXPOSE 8761
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]