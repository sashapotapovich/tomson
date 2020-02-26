FROM openjdk:11
COPY server.jar /opt/myapp/
WORKDIR /opt/myapp
EXPOSE 2005
ENTRYPOINT ["java", "-jar", "server.jar"]