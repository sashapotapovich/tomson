FROM ibmcom/mq:9
MAINTAINER sashapotpovich@gmail.com
HEALTHCHECK --timeout=1s --retries=60 CMD cat /.initialized || exit 1

ENV LICENSE accept
ENV MQ_QMGR_NAME QM1

COPY setup/config.mqsc /etc/mqm/config.mqsc
COPY setup/mq.sh /usr/local/bin/mq.sh

RUN chmod +x /usr/local/bin/mq.sh && \
useradd sci -G mqm && \
    echo sci:sci | chpasswd

EXPOSE 1414