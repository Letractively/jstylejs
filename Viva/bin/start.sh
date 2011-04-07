#!/bin/bash
cd ..
CURRENT_DIR=$PWD
CLASS_PATH=$CURRENT_DIR/build/
for f in $CURRENT_DIR/lib/*.jar;do
CLASS_PATH=$CLASS_PATH:$f;
done
java -cp $CLASS_PATH  org.creativor.viva.Viva $*
