#!/bin/bash
SERVER_JAR=server.jar
SERVER_DOWNLOAD_URL=https://api.papermc.io/v2/projects/paper/versions/1.20.1/builds/102/downloads/paper-mojmap-1.20.1-102.jar

cd test

if [ ! -f "$SERVER_JAR" ]; then
     curl -o server.jar "$SERVER_DOWNLOAD_URL"
fi

if [ ! -f eula.txt ]; then
    echo "eula=true" >> eula.txt
fi

mkdir -p plugins
cp ../build/libs/tpa*.jar plugins

java -Xmx2G -jar server.jar nogui
