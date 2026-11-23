@echo off
setlocal
cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo Java wurde nicht gefunden. Bitte ein JDK 17 oder neuer installieren.
    exit /b 1
)
where jar >nul 2>nul
if errorlevel 1 (
    echo Das JDK-Werkzeug jar wurde nicht gefunden.
    exit /b 1
)

if exist out rmdir /s /q out
mkdir out

javac --release 17 -encoding UTF-8 -sourcepath src\main\java -d out src\main\java\de\kreuzkreis\Main.java
if errorlevel 1 goto :fail

xcopy /e /i /y "src\main\resources\*" "out\" >nul
if errorlevel 1 goto :fail
if not exist "out\fonts\Chillax-Medium.ttf" goto :fail
if not exist "out\fonts\Chillax-Semibold.ttf" goto :fail

if exist KreuzKreis.tmp.jar del /f /q KreuzKreis.tmp.jar >nul 2>nul
jar --create --file KreuzKreis.tmp.jar --main-class de.kreuzkreis.Main -C out .
if errorlevel 1 goto :fail

move /y KreuzKreis.tmp.jar KreuzKreis.jar >nul 2>nul
if errorlevel 1 (
    echo.
    echo Die Datei KreuzKreis.jar wird gerade benutzt.
    echo Bitte das laufende Spiel schliessen und erneut bauen.
    del /f /q KreuzKreis.tmp.jar >nul 2>nul
    exit /b 1
)

echo.
echo Build erfolgreich: KreuzKreis.jar
exit /b 0

:fail
if exist KreuzKreis.tmp.jar del /f /q KreuzKreis.tmp.jar >nul 2>nul
echo.
echo Build fehlgeschlagen.
exit /b 1
