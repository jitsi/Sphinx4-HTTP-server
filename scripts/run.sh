#!/usr/bin/env bash
if [ $# -eq 0 ]
  then
    mvn exec:java -Dexec.mainClass="server.HttpServer" -Dexec.args="8081"
  else
    mvn exec:java -Dexec.mainClass="server.HttpServer" -Dexec.args="$1"
fi





