@echo off
setlocal enabledelayedexpansion
title Smart Patient Health Monitoring - GitHub Push Launcher

echo ===================================================================
echo   Pushing Smart Patient Health Monitoring System to GitHub
echo   Target: https://github.com/kit25csecdheekshanada-arch/Smart-Health-Monitoring.git
echo ===================================================================
echo.

cd /d "%~dp0"

REM Configure PATH so MinGit and Git Credential Manager work reliably
set "PATH=%~dp0..\mingit\cmd;%~dp0..\mingit\clangarm64\bin;%PATH%"

echo Checking Git status...
git.exe status -s
echo.

echo Choose authentication method:
echo   [1] Standard Web Browser Sign-In (Recommended)
echo   [2] GitHub Personal Access Token (PAT)
echo.
set /p "AUTH_CHOICE=Select option [1 or 2, default is 1]: "

if "%AUTH_CHOICE%"=="2" (
    echo.
    echo Please generate a Personal Access Token on GitHub:
    echo   https://github.com/settings/tokens
    echo   Scope needed: "repo" (Full control of private/public repositories)
    echo.
    set /p "GITHUB_TOKEN=Paste your GitHub Personal Access Token (PAT): "
    if "!GITHUB_TOKEN!"=="" (
        echo [ERROR] Token cannot be empty. Aborting.
        pause
        exit /b 1
    )
    echo.
    echo Pushing to GitHub with token...
    git.exe push "https://!GITHUB_TOKEN!@github.com/kit25csecdheekshanada-arch/Smart-Health-Monitoring.git" main
) else (
    echo.
    echo Pushing to GitHub using Git Credential Manager...
    echo (A browser sign-in window will open if this is your first time)
    git.exe push -u origin main
)

if %ERRORLEVEL% equ 0 (
    echo.
    echo ===================================================================
    echo   SUCCESS: Project successfully pushed to GitHub!
    echo   Repository: https://github.com/kit25csecdheekshanada-arch/Smart-Health-Monitoring
    echo ===================================================================
) else (
    echo.
    echo ===================================================================
    echo   [NOTICE] Git push encountered an issue.
    echo   If authentication failed, try running again with option [2] (PAT).
    echo ===================================================================
)

echo.
pause
