@echo off
setlocal
echo ===================================================================
echo   Pushing Smart Patient Health Monitoring System to GitHub
echo   Target: https://github.com/kit25csecdheekshanada-arch/Smart-Health-Monitoring.git
echo ===================================================================
echo.

cd /d "%~dp0"
"%~dp0..\mingit\cmd\git.exe" push -u origin main

echo.
pause
