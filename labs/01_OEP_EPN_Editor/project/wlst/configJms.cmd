REM call ..\setEnv.cmd

set WLS_HOME=D:\11gR1PS3\SOA\wlserver_10.3
set JAVA_HOME=D:\11gR1PS3\SOA\jdk160_21
set CLASSPATH=%WLS_HOME%\server\lib\weblogic.jar

%JAVA_HOME%\bin\java weblogic.WLST initJms.py
