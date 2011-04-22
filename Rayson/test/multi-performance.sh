#!/bin/bash
HELP_INFO="usage: "$0" process_count performance_paramters";
if [ "$#" == 0  ]; then
   echo  $HELP_INFO
    exit -1
fi
PROCESS_COUNT=$1
shift 1
for (( i=0; i<$PROCESS_COUNT; i++));
do
   sh ./performance.sh $* &
done;
