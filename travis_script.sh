#!/bin/bash
set -e # exit if return != 0
if [ "$TARGET" = "lab6" ]; then
    # download jdk-14
    if[["$OSTYPE" == "linux-gnu"]]; then
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-linux.tar.gz
        tar xvf jdk-14-loom-linux.tar.gz
    elif [["$OSTYPE" == "darwin17"]]; then
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-osx.tar.gz
        tar xvf jdk-14-loom-osx.tar.gz
    fi
    # export new environment variable JAVA_HOME
    export JAVA_HOME=$(pwd)/jdk-14-loom/
fi

# for all lab
cd $TARGET
mvn package