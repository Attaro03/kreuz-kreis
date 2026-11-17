@echo off
setlocal
cd /d "%~dp0"

call build.bat
if errorlevel 1 (
    echo.
    echo Das Spiel konnte nicht gebaut werden.
    pause
    exit /b 1
)

start "" javaw -jar KreuzKreis.jar
