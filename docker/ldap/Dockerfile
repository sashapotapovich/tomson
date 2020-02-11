FROM kwart/ldap-server:2017-09-04

COPY setup /setup

RUN cp /setup/custom.ldif /mnt/custom.ldif

EXPOSE 10389 10636

CMD ["java", "-jar", "/ldap-server.jar", "-p", "10389", "/mnt/custom.ldif"]
