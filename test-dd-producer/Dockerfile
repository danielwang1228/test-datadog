FROM amazoncorretto:17
WORKDIR /app
#RUN curl -Lo dd-java-agent.jar https://dtdg.co/latest-java-tracer
COPY target/test-dd-producer-1.0-SNAPSHOT.jar app.jar
COPY dd-java-agent.jar dd-java-agent.jar

LABEL com.datadoghq.tags.service="dd-producer"
LABEL com.datadoghq.tags.env="cos1"
LABEL com.datadoghq.tags.version="0.0.1"

ENV DD_SERVICE="dd-producer"
ENV DD_ENV="cos1"
ENV DD_VERSION="0.0.1"
ENV DD_AGENT_HOST="dd-agent"
ENV DD_RUNTIME_METRICS_ENABLED=true
ENV DD_PROFILING_ENABLED=true
ENV DD_APM_ENABLED=true
ENV DD_APM_NON_LOCAL_TRAFFIC=true

ENTRYPOINT ["java" , "-javaagent:./dd-java-agent.jar", "-Ddd.trace.sample.rate=1", "-jar" , "./app.jar"]
# ENTRYPOINT ["java", "-jar", "/app.jar"]
