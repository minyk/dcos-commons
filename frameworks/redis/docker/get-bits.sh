#!/usr/bin/env bash
rm -f *.zip
rm -f *.tar.gz

echo "Get compiled bits"
cp ../build/distributions/*.zip ./
cp ../../../sdk/bootstrap/bootstrap.zip ./

echo "Download JRE and libmesos"
curl -L https://downloads.mesosphere.com/java/jre-8u131-linux-x64-jce-unlimited.tar.gz -o jre-8u131-linux-x64-jce-unlimited.tar.gz
curl -L https://downloads.mesosphere.io/libmesos-bundle/libmesos-bundle-1.10-1.4-63e0814.tar.gz -o libmesos-bundle-1.10-1.4-63e0814.tar.gz