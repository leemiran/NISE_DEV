#!/bin/sh
#---------------------------------------------------------
# Start Script for  the Report Designer Server 5.0
#---------------------------------------------------------
# Environment Variables
#
# RDSERVER_HOME           May point at your RDServer installation directory
# RDSERVER_LIB            jar files or directories to the RD Server
# RDSERVER_PORT           RD Server options used when server starting the listening port. The default is 8282
# RDSERVER_MANAGER_PORT   RD Server options used when server starting manager listening port. The default is 8089
# JAVA_HOME               Must point at your Java Development Kit installation. (jdk1.4 or later version) 
# JAVA_OPTIONS            (Optional) Java runtime options used when the java(start) command is executed
# JDBC_CLASSPATH          User jar files or directories to CLASSPATH  (etc. jdbc drivers)
# RDSERVER_SCHEDULER      If the RDServer uses scheduling, set this true. The default is false
# RDSERVER_SESSIONSERVER  if the RDServer uses session server, set this true, The default is false (one-time session report)

JAVA_HOME=$JAVA_HOME
RDSERVER_HOME=`cd "../";pwd`

RDSERVER_LIB="$RDSERVER_HOME"/lib/j2ee.jar:"$RDSERVER_HOME"/bin/rdserver.jar:"$RDSERVER_HOME"/bin/rdmaster.jar

RDSERVER_PORT=8282
RDSERVER_MANAGER_PORT=8089
RDSERVER_SCHEDULER=false
RDSERVER_SESSIONSERVER=false

#Add on jdbc driver jar files to JDBC_CLASSPATH
JDBC_CLASSPATH=../lib

#Add on java options 
MAX_MEMORY=128
MIN_MEMORY=128
JAVA_OPTIONS="-Xms"$MIN_MEMORY"m -Xmx"$MAX_MEMORY"m -Dfile.encoding=EUC_KR -Dconfig.dir="$RDSERVER_HOME""

#-------------------- Find JAVA_HOME --------------------
#echo JAVA_HOME=$JAVA_HOME
#if [ -z "$JAVA_HOME" ] ;  then
#  echo "Cannot find JAVA_HOME. Please set your PATH."
#  echo "ex> JAVA_HOME='/usr/jdk1.4.2/bin'"
#  echo " export JAVA_HOME"
#  exit 1
#fi

pid=`cat $PWD/demon.pid`

if [ "$pid" != "" ] ; then
kill -9 `cat $PWD/demon.pid`
echo Shutting down RDServer Daemon ...:$pid
echo "" > $PWD/demon.pid
fi

# -------------------- Start RDServer Daemon --------------------
echo "Loading RDServer...."
echo "Using RDSERVER_HOME: $RDSERVER_HOME"
echo "Using JAVA_HOME:     $JAVA_HOME"
#echo "Using JAVA_OPTIONS:  $JAVA_OPTIONS"
echo "Using CLASSPATH:     $RDSERVER_LIB:$JDBC_CLASSPATH"

"$JAVA_HOME"/bin/java $JAVA_OPTIONS -classpath .:"$RDSERVER_LIB":"$JDBC_CLASSPATH" m2soft.rdsystem.server.Server server.port="$RDSERVER_PORT" manager.port="$RDSERVER_MANAGER_PORT" oencoding=false iencoding=false userpool=false runschedule="$RDSERVER_SCHEDULER" usesessionkey="$RDSERVER_SESSIONSERVER" & 
JAVA_PID=$!
echo $JAVA_PID > $PWD/demon.pid
