#!/bin/bash
HELP_INFO="usage: "$0" fromport toport";

if [ "$#" != 2 ]; then
   echo  $HELP_INFO
    exit -1
else
	FROM_PORT=$1
	TO_PORT=$2
fi

CURRENT_DIR=$PWD
cd ../bin

for (( i=$FROM_PORT; i<$TO_PORT; i++));
do
        sh ./start.sh $i &
	echo sleep 5 seconds to start a new staff...
        sleep 5;
done;

cd $CURRENT_DIR
