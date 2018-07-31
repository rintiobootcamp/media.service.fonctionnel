FROM openjdk:8-jdk-alpine
ADD target/mediaRestServices.jar ws_mediaRestServices_sf.jar
EXPOSE 8086
RUN mkdir -p /home/media/location
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap  -XX:MaxRAMFraction=1 -XshowSettings:vm "
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ws_mediaRestServices_sf.jar" ]