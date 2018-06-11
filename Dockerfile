FROM ibrahim/alpine
ADD target/mediaRestServices.jar ws_mediaRestServices_sf.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","ws_mediaRestServices_sf.jar"]