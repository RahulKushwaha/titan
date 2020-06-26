#!/bin/bash
export JAVA_HOME=/opt/java

cd /opt/api/
./bin/titan-distribution server var/conf/server.yml
