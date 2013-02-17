#!/bin/bash
BASEDIR=$(dirname $0)
cd "$BASEDIR"
JAVA=java
CLASSPATH=
for f in jMP3TagEditor*.jar;
do
	CLASSPATH="$CLASSPATH:$f"
done
for f in lib/*.jar;
do
	CLASSPATH="$CLASSPATH:$f"
done
"$JAVA" -classpath $CLASSPATH com.mscg.jmp3.main.AppLaunch $@
