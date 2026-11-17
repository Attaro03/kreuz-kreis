@echo off
setlocal
cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo Java wurde nicht gefunden. Bitte ein JDK 17 oder neuer installieren.
    exit /b 1
)

if exist out rmdir /s /q out
mkdir out

dir /s /b src\main\java\*.java > sources.txt
javac --release 17 -encoding UTF-8 -d out @sources.txt
if errorlevel 1 goto :fail
del sources.txt

if exist src\main\resources (
    xcopy /e /i /y "src\main\resources\*" "out\" >nul
)

jar --create --file KreuzKreis.jar --main-class de.kreuzkreis.Main -C out .
if errorlevel 1 goto :fail

echo.
echo Build erfolgreich: KreuzKreis.jar
exit /b 0

:fail
if exist sources.txt del sources.txt
echo.
echo Build fehlgeschlagen.
exit /b 1
