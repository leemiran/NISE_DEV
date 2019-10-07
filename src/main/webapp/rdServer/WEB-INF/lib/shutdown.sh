#!/bin/sh
#
# -------------------- Stop RDAgentDemon --------------------
echo "Shutdown RDServer Daemon ...: `cat $PWD/demon.pid`"
kill -9 `cat $PWD/demon.pid`
echo "" > $PWD/demon.pid
