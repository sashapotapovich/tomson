FROM openjdk:11
COPY server.jar /opt/myapp/
WORKDIR /opt/myapp
EXPOSE 2005
CMD ["java", "-jar", "-Djava.rmi.server.hostname=127.0.0.1", "-Djava.security.debug=acess,failure", "-Djava.security.policy=permission=java.security.AllPermission", "server.jar"]