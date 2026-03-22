@echo off
setlocal
set "MVN_DIR=%~dp0"
if "%MVN_DIR:~-1%"=="\" set "MVN_DIR=%MVN_DIR:~0,-1%"
set "MVN_JAR=%MVN_DIR%\.mvn\wrapper\maven-wrapper.jar"
set "MVN_MAIN=org.apache.maven.wrapper.MavenWrapperMain"

REM Prioritize JDK 17 path
set "JDK17_PATH=C:\Program Files\Java\jdk-17"

if exist "%JDK17_PATH%" (
    set "JAVA_HOME=%JDK17_PATH%"
    set "MVN_JAVA=%JDK17_PATH%\bin\java.exe"
) else (
    set "MVN_JAVA=%JAVA_HOME%\bin\java.exe"
)

if not exist "%MVN_JAVA%" set "MVN_JAVA=java"

"%MVN_JAVA%" -Xmx512m -classpath "%MVN_JAR%" "-Dmaven.multiModuleProjectDirectory=%MVN_DIR%" %MVN_MAIN% %*

endlocal
