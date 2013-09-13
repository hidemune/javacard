#!/bin/bash

#if [ $$ != `pgrep -fo $0`  ]; then
#	echo `basename $0` is already running.
#	exit 1
#fi

CURDIR=`dirname $0`
echo $CURDIR

cd $CURDIR

java -jar $CURDIR/Card.jar
