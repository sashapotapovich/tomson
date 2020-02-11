FROM sonarqube:6.7
MAINTAINER sashapotapovich@gmail.com

ENV SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonar

VOLUME c:/sonar/conf:/opt/sonarqube/conf
VOLUME c:/sonar/data:/opt/sonarqube/data
VOLUME c:/sonar/extensions:/opt/sonarqube/extensions
VOLUME c:/sonar/lib/bundled-plugins:/opt/sonarqube/lib/bundled-plugins

EXPOSE 9000