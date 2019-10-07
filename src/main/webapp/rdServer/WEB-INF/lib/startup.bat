@echo off
rem --------------------------------------------------------------------------
rem         Start Script Report Designer Server5.0
rem  This batch file written and tested under Windows 2000
rem --------------------------------------------------------------------------
rem 
rem  Environment Variable Prequisites
rem
rem  RDSERVER_HOME           May point at your RDServer "installation" directory.
rem
rem  JAVA_EXE                Excution path of your Java Development Kit.
rem
rem  JAVA_OPTION             (Optional) Java runtime options used when the "start"
rem                          command is executed.
rem  MIN_MEMORY              Minimum memory used by RD Server (MB)
rem
rem  MAX_MEMORY              Maximum memory used by RD Server.(MB)
rem   
rem  RDSERVER_USERLIB        Use this variable  user library directories
rem
rem  JDBC_CLASSPATH          User jar files or directories to CLASSPATH  (etc. jdbc drivers)
rem
rem  RDSERVER_LIB            RDServer jar files 
rem
rem  RDSERVER_SCHEDULER      If the RDServer uses scheduling, set this true. The default is false
rem
rem  RDSERVER_PORT           RDServer options used when RDServer starting the listening port. The default is 8282
rem  
rem  RDSERVER_MANAGER_PORT   RDServer options used when RDServer Manager starting listening port. The default is 8089
rem 
rem  RDSERVER_SESSIONSERVER  RDServer for one - time report session (true or false)


rem $Id: startup.bat,v 5.0 2006/01/30
rem ---------------------------------------------------------------------------


:: checking java path startup.bat
@ECHO off

set RDSERVER_HOME=..

SET JAVA_EXE=java.exe
IF "%rec%"=="01" GOTO recurs
SET rec=01
SET javaexe=%JAVA_EXE%
%0 %path%

:recurs
SET rec=
IF "%1"=="" ECHO %javaexe% not found JAVA EXE in current directory or path
IF "%1"=="" GOTO notfound
IF exist %1.\%JAVA_EXE% GOTO found
SHIFT
GOTO recurs

:found
IF exist %1%JAVA_EXE% ECHO %1%JAVA_EXE% 
IF exist %1\%JAVA_EXE% ECHO %1\%JAVA_EXE% 
rem %1\%JAVA_EXE% -version


if not RDSERVER_HOME== "" goto start
echo Unable to determine the value of RDSERVER_HOME.
goto eof

:start

cls

set MIN_MEMORY=128
set MAX_MEMORY=256
set JAVA_OPTIONS=
set JAVA_OPTIONS=%JAVA_OPTIONS% -Xms%MIN_MEMORY%m -Xmx%MAX_MEMORY%m -Dfile.encoding=EUC_KR

set RDSERVER_LIB=.
set RDSERVER_USERLIB=%RDSERVER_HOME%\lib
set RDSERVER_LIB=%RDSERVER_USERLIB%\j2ee.jar
set RDSERVER_LIB=%RDSERVER_LIB%;rdserver.jar
set RDSERVER_LIB=%RDSERVER_LIB%;rdmaster.jar
set JDBC_CLASSPATH=


set RDSERVER_PORT=8282
set RDSERVER_MANAGER_PORT=8089
set RDSERVER_SCHEDULER=false
set RDSERVER_SESSIONSERVER=false


echo Loading RDServer...
echo %RDSERVER_LIB%%JDBC_CLASSPATH%
%JAVA_EXE% %JAVA_OPTIONS% -cp %RDSERVER_LIB%;%JDBC_CLASSPATH% m2soft.rdsystem.server.Server server.port=%RDSERVER_PORT% manager.port=%RDSERVER_MANAGER_PORT% oencoding=false iencoding=false userpool=false runschedule=%RDSERVER_SCHEDULER% usesessionkey=%RDSERVER_SESSIONSERVER%
:eof

exit

:notfound
explorer http://m2soft.co.kr/report/RDServerUpdate/server5downloadjre.htm
:end
::
