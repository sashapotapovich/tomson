FROM docker:dind
ENV DOCKER_TLS_CERTDIR=/certs
VOLUME ./jenkins/var/certs/client:/certs/client
VOLUME ./jenkins/var/jenkins_home:/var/jenkins_home
VOLUME ./jenkins/var/home:/home
EXPOSE 2376