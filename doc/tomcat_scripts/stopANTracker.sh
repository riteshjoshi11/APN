#!/bin/sh
PID=`ps -ef | grep ANTracker-0.0.1 | awk '{ print $2 }'`
echo $PID
kill -9 $PID


