FROM openjdk:11
COPY web.jar /opt/myapp/
WORKDIR /opt/myapp
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "web.jar"]