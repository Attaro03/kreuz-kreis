@echo off
setlocal
cd /d "%~dp0"

call build.bat
if errorlevel 1 exit /b 1

if exist out-test rmdir /s /q out-test
mkdir out-test

javac --release 17 -encoding UTF-8 -cp out -d out-test src\test\java\de\kreuzkreis\Preview.java
if errorlevel 1 exit /b 1

java -Djava.awt.headless=true -cp "out;out-test" de.kreuzkreis.Preview
