#!/bin/sh

## -------------------- Find JAVA_HOME --------------------
#echo JAVA_HOME=$JAVA_HOME
#if [ -z "$JAVA_HOME" ] ;  then
#  echo "Cannot find JAVA_HOME. Please set your PATH."
#  echo "ex> JAVA_HOME='/usr/jdk1.4.2/bin'"
#  echo "    export JAVA_HOME"
#  exit 1
#fi

# -------------------- Start RDServer Daemon --------------------
start javaw -Dfile.encoding=EUC_KR -classpath .;./rdmaster.jar;./rdmanager.jar;../bin/rdmaster.jar;../bin/rdmanager.jar;./jhbasic.jar;../lib/javahelp/jhbasic.jar m2soft.rdsystem.manager4.RDManager
JAVA_PID=$!
echo $JAVA_PID > $PWD/admin.pid