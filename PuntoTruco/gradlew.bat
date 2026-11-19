@echo off
setlocal
cd /d "%~dp0"
set "GRADLE_VERSION=8.11.1"
if "%GRADLE_USER_HOME%"=="" set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "BOOT=%GRADLE_USER_HOME%\puntotruco-bootstrap"
set "GRADLE_EXE=%BOOT%\gradle-%GRADLE_VERSION%\bin\gradle.bat"
if not exist "%GRADLE_EXE%" (
  if not exist "%BOOT%" mkdir "%BOOT%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $zip='%BOOT%\gradle-%GRADLE_VERSION%-bin.zip'; Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath '%BOOT%' -Force; Remove-Item $zip"
  if errorlevel 1 exit /b 1
)
call "%GRADLE_EXE%" %*
