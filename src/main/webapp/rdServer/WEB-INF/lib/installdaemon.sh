#!/bin/sh
#
if [ -z "$JAVA_HOME" ] ; then
  echo You must set JAVA_HOME to point at your Java Development Kit installation
  exit 1
fi
$JAVA_HOME/bin/java -jar daemonsetup.jar