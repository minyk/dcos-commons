FROM centos:7.3.1611

RUN yum install -y unzip tar && mkdir -p /usr/share/dcos

COPY redis-scheduler.zip /usr/local/dcos/redis-scheduler.zip
COPY executor.zip /usr/local/dcos/executor.zip
COPY bootstrap.zip /usr/local/dcos/bootstrap.zip
COPY libmesos-bundle-1.10-1.4-63e0814.tar.gz /usr/local/dcos/libmesos-bundle-1.10-1.4-63e0814.tar.gz
COPY jre-8u144-linux-x64.tar.gz /usr/local/dcos/jre-8u144-linux-x64.tar.gz
COPY stadis-linux-amd64 /usr/local/dcos/stadis-linux-amd64
COPY entrypoint.sh /entrypoint.sh

ENV JAVA_URI http://api.redis.marathon.l4lb.thisdcos.directory/v1/files/jre-8u144-linux-x64.tar.gz
ENV LIBMESOS_URI http://api.redis.marathon.l4lb.thisdcos.directory/v1/files/libmesos-bundle-1.10-1.4-63e0814.tar.gz
ENV EXECUTOR_URI http://api.redis.marathon.l4lb.thisdcos.directory/v1/files/executor.zip
ENV BOOTSTRAP_URI http://api.redis.marathon.l4lb.thisdcos.directory/v1/files/bootstrap.zip

ENTRYPOINT ["/entrypoint.sh"]