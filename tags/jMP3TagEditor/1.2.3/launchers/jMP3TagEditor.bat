@echo off
setlocal enabledelayedexpansion
if "x%JAVA_HOME%" == "x" (
  set JAVA=javaw
) else (
  set "JAVA=%JAVA_HOME%\bin\javaw"
)
set BASEFOLDER=%~dp0%
cd %BASEFOLDER%
set CLASSPATH=
for %%f IN (jMP3TagEditor*.jar) DO set CLASSPATH=!CLASSPATH!;%%f
for %%f IN (lib\*.jar) DO set CLASSPATH=!CLASSPATH!;%%f
start "jMP3 Tag Editor" "%JAVA%" -classpath %CLASSPATH% com.mscg.jmp3.main.AppLaunch %*