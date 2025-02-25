FROM  registry.mokahr.com/ops/base:maven as builder
ENV PROJECT_ROOT=/project
WORKDIR $PROJECT_ROOT
COPY . $PROJECT_ROOT
RUN mvn clean package -Dmaven.test.skip=true -U
RUN ls 
RUN ls ././target

FROM registry.mokahr.com/ops/base:java
RUN java -version

RUN  mkdir -p /data/logs/ai-search-platform
COPY --from=builder /project/target/ai-search-platform-0.0.1.jar ./java_app.jar

COPY ops/entrypoint.sh /entrypoint.sh

ENTRYPOINT  ["/bin/bash","-x","/entrypoint.sh"]

