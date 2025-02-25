FROM frolvlad/alpine-glibc:alpine-3.8

ENV JAVA_VERSION=8 \
    JAVA_HOME="/usr/lib/jvm/default-jvm"

RUN ls

RUN apk add --no-cache openjdk8
RUN apk add alpine-sdk


COPY target/moka-search-0.1.0.jar /moka-search.jar

ENTRYPOINT ["java","-Xms3g", "-Xmx8g", "-jar", "/thoth.jar"]
