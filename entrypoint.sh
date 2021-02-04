#!/bin/bash
export JAVA_OPTS="-server \
          -Xms512m \
          -XX:MaxGCPauseMillis=500 \
          -XX:+UseStringDeduplication \
          -Xmx3072m \
          -XX:+UseG1GC \
          -XX:ConcGCThreads=4 \
          -XX:ParallelGCThreads=4 \
          -Dpidfile.path=/dev/null \
          -Dhttp.address=0.0.0.0 \
          -Dhttp.port=80 \
          -Dlogger.url=file:///$APP_BASE/target/core-kafka-service/conf/logback.xml \
          -Denvironment=${ENVIRONMENT_NAME}"

unzip -n /core/target/universal/core-kafka-service.zip -d /core/target
/core/target/core-kafka-service/bin/core-kafka-service