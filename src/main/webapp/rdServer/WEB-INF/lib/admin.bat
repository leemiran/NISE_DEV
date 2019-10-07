@echo off
rem ---------------------------------------------------------
rem            Report Designer5.0  Server Manager
rem ---------------------------------------------------------

:: checking java path admin.bat
@ECHO off

SET JAVA_PATH=java.exe
IF "%rec%"=="01" GOTO recurs
SET rec=01
SET fil=%JAVA_PATH%
%0 %path%

:recurs
SET rec=
IF "%1"=="" ECHO. %fil% not in current directory or path
IF "%1"=="" GOTO notfound
IF exist %1.\%JAVA_PATH% GOTO found
SHIFT
GOTO recurs
 
:found
IF exist %1%JAVA_PATH% ECHO %1%JAVA_PATH% 
IF exist %1\%JAVA_PATH% ECHO %1\%JAVA_PATH% 
rem %1\%JAVA_PATH% -version

set MIN_MEMORY=128
set MAX_MEMORY=128
set JAVA_OPTION=
set JAVA_OPTION=%JAVA_OPTION% -Xms%MIN_MEMORY%m -Xmx%MAX_MEMORY%m

set RDMANAGER_HOME=..
set RDMANAGER_LIB=.
set RDMANAGER_LIB=%RDMANAGER_LIB%;rdmaster.jar
set RDMANAGER_LIB=%RDMANAGER_LIB%;rdmanager.jar
set RDMANAGER_LIB=%RDMANAGER_LIB%;jhbasic.jar

rem //korean,english,japanese
set LANGUAGE=Korean

start javaw %JAVA_OPTION% -cp %RDMANAGER_LIB% m2soft.rdsystem.manager4.RDManager language=%LANGUAGE%

exit

:notfound
explorer http://m2soft.co.kr/report/RDServerUpdate/server5downloadjre.htm
:end
::
