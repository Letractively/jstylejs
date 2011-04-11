#!/bin/bash

CURRENT_DIR=$PWD
cd ..

for (( i=6660; i<6661; i++));
do
	sh ./start.sh $i &
	sleep 5;
done;

cd $CURRENT_DIR

