FROM amazoncorretto:17
WORKDIR /app
#RUN curl -Lo dd-java-agent.jar https://dtdg.co/latest-java-tracer
COPY target/test-dd-web-1.0-SNAPSHOT.jar app.jar
COPY dd-java-agent.jar dd-java-agent.jar

LABEL com.datadoghq.tags.service="dd-web"
LABEL com.datadoghq.tags.env="cos1"
LABEL com.datadoghq.tags.version="0.0.1"

ENV DD_SERVICE="dd-web"
ENV DD_ENV="cos1"
ENV DD_VERSION="0.0.1"
ENV DD_AGENT_HOST="dd-agent"
ENV DD_RUNTIME_METRICS_ENABLED=true
ENV DD_PROFILING_ENABLED=true
ENV DD_APM_ENABLED=true
ENV DD_APM_NON_LOCAL_TRAFFIC=true
ENV DD_TRACE_MESSAGING_ENABLED=true
ENV DD_TRACE_OTEL_ENABLED=true
ENV DD_TRACE_METHODS=com.dd.web.service.TigerService[tigerEat];com.dd.web.service.LionService[lionRun];com.dd.web.service.OtherService[otherEat,otherRun]

ENTRYPOINT ["java" , "-javaagent:./dd-java-agent.jar", "-Ddd.trace.sample.rate=1", "-jar" , "./app.jar"]
# ENTRYPOINT ["java", "-jar", "/app.jar"]
