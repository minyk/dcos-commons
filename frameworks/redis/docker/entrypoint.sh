#!/usr/bin/env bash

cd ${MESOS_SANDBOX}

# Copy bits to $MESOS_SANDBOX
cp /usr/local/dcos/* .
unzip redis-scheduler.zip -d .
unzip bootstrap.zip -d .
tar zxf jre-8u144-linux-x64.tar.gz
tar zxf libmesos-bundle-1.10-1.4-63e0814.tar.gz

export LD_LIBRARY_PATH=$MESOS_SANDBOX/libmesos-bundle/lib:$LD_LIBRARY_PATH;
export MESOS_NATIVE_JAVA_LIBRARY=$(ls $MESOS_SANDBOX/libmesos-bundle/lib/libmesos-*.so);
export JAVA_HOME=$(ls -d $MESOS_SANDBOX/jre*/);
export JAVA_HOME=${JAVA_HOME%/};
export PATH=$(ls -d $JAVA_HOME/bin):$PATH
export JAVA_OPTS="-Xms256M -Xmx512M -XX:-HeapDumpOnOutOfMemoryError"

./bootstrap -resolve=false -template=false && ./redis-scheduler/bin/redis ./redis-scheduler/svc.yml