FROM jenkinsci/blueocean
VOLUME ./jenkins/var/jenkins_home:/var/jenkins_home
VOLUME ./jenkins/var/home:/home
VOLUME ./jenkins/var/certs/client:/certs/client:ro
ENV DOCKER_HOST=tcp://docker:2376
ENV DOCKER_CERT_PATH=/certs/client
ENV DOCKER_TLS_VERIFY=1
EXPOSE 50000 8080