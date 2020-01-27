FROM openjdk:8
MAINTAINER sashapotapovich@gmail.com
COPY ./build/libs/ /tmp
COPY startup.sh /tmp
RUN cd /tmp
WORKDIR /tmp
RUN ls -a
RUN chmod u+x startup.sh
ENTRYPOINT ./startup.sh