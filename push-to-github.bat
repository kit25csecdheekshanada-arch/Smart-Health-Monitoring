@echo off
setlocal
title GitHub Push - Smart Patient Health Monitoring

echo ===================================================================
echo   Pushing Smart Patient Health Monitoring System to GitHub
echo   Target: https://github.com/kit25csecdheekshanada-arch/Smart-Health-Monitoring.git
echo ===================================================================
echo.

cd /d "%~dp0"
set "PATH=%~dp0..\mingit\cmd;%~dp0..\mingit\clangarm64\bin;%PATH%"

echo Running git push to GitHub...
echo (A browser sign-in tab will open to authorize GitHub if needed)
echo.

git.exe push -u origin main

if %ERRORLEVEL% equ 0 (
    echo.
    echo ===================================================================
    echo   SUCCESS: Pushed successfully to GitHub!
    echo ===================================================================
) else (
    echo.
    echo ===================================================================
    echo   [ERROR] Push failed. If browser sign-in did not complete,
    echo   please check your credentials or run again.
    echo ===================================================================
)

echo.
pause
