#!/bin/bash
set -e # exit if return != 0
if [ "$FOLDER" = "lab6" ]; then
    if[[ "$OSTYPE" == "darwin17" ]]; then   # OS = osx
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-osx.tar.gz
        tar xvf jdk-14-loom-osx.tar.gz
    else
        # download jdk-14
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-linux.tar.gz
        # extract
        tar xvf jdk-14-loom-linux.tar.gz
    fi
    # export new environment variable JAVA_HOME
    export JAVA_HOME=$(pwd)/jdk-14-loom/
fi

# for all lab
cd $FOLDER
mvn package