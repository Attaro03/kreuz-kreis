@echo off
setlocal
cd /d "%~dp0"

call build.bat
if errorlevel 1 exit /b 1

if exist out-test rmdir /s /q out-test
mkdir out-test

javac --release 17 -encoding UTF-8 -cp out -d out-test ^
    src\test\java\de\kreuzkreis\GameTest.java ^
    src\test\java\de\kreuzkreis\UiTest.java
if errorlevel 1 exit /b 1

java -cp "out;out-test" de.kreuzkreis.GameTest
if errorlevel 1 exit /b 1

java -Djava.awt.headless=true -cp "out;out-test" de.kreuzkreis.UiTest
if errorlevel 1 exit /b 1

echo.
echo Alle Tests erfolgreich.
