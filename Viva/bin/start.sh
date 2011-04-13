#!/bin/bash
CURRENT_DIR=$PWD
cd ..
PATENT_PATH=$PWD
CLASS_PATH=$PATENT_PATH/build/
for f in $PATENT_PATH/lib/*.jar;do
CLASS_PATH=$CLASS_PATH:$f;
done
java -cp $CLASS_PATH  org.creativor.viva.Viva $*
cd $CURRENT_DIR
