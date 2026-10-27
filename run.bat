@echo off
setlocal
echo ===================================================================
echo   Smart Patient Health Monitoring System - Launcher
echo   Java 21 + Spring Boot + Thymeleaf + MySQL / Embedded H2
echo ===================================================================

REM Check if JAVA_HOME is set; if not, discover available JDK 21
if "%JAVA_HOME%"=="" (
    if exist "%~dp0..\jdk21\jdk-21.0.13.0.2+1" (
        set "JAVA_HOME=%~dp0..\jdk21\jdk-21.0.13.0.2+1"
    ) else if exist "C:\Users\dheek\.gemini\antigravity\scratch\jdk21\jdk-21.0.13.0.2+1" (
        set "JAVA_HOME=C:\Users\dheek\.gemini\antigravity\scratch\jdk21\jdk-21.0.13.0.2+1"
    ) else if exist "C:\Program Files\Microsoft\jdk-21.0.13.0.2-hotspot" (
        set "JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.13.0.2-hotspot"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
    ) else if exist "C:\Program Files\Java\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    )
)

if not "%JAVA_HOME%"=="" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo Using Java 21 JDK at: %JAVA_HOME%
)

echo.
echo Starting application on http://localhost:8080 ...
echo Default Development Credentials:
echo   - Administrator: admin / Admin@123
echo   - Cardiologist / Doctor: doctor / Doctor@123
echo   - ICU Nurse: nurse / Nurse@123
echo.

call mvnw.cmd spring-boot:run %*

pause
