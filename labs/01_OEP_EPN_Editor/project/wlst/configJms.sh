#!/bin/bash

export FMW_HOME=/oracle/fmwhome

export JAVA_HOME=/oracle/javahome/sun_jvm

export WLS_HOME=$FMW_HOME/wlserver_10.3

export CLASSPATH=$WLS_HOME/server/lib/weblogic.jar

$JAVA_HOME/bin/java weblogic.WLST initJms.py
