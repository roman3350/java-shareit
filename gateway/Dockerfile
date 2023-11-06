FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ShareIt-gateway.jar
ENTRYPOINT ["java","-jar","/ShareIt-gateway.jar"]