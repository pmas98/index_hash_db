@echo off
setlocal ENABLEDELAYEDEXPANSION

rem Windows build helper matching Makefile targets

set SRC_DIR=src\main\java
set BUILD_DIR=build
set CLASSES_DIR=%BUILD_DIR%\classes
set MAIN_CLASS=hashindex.demo.HashIndexDemo
set FILE_DEMO_CLASS=hashindex.demo.HashIndexDemoWithFile
set FRONTEND_CLASS=hashindex.demo.HashIndexUI

if "%1"=="" goto :compile
if /I "%1"=="all" goto :compile
if /I "%1"=="compile" goto :compile
if /I "%1"=="demo" goto :demo
if /I "%1"=="demo-file" goto :demo_file
if /I "%1"=="frontend" goto :frontend
if /I "%1"=="clean" goto :clean
if /I "%1"=="distclean" goto :distclean
if /I "%1"=="tree" goto :tree
if /I "%1"=="help" goto :help

echo Unknown target: %1
exit /b 1

:compile
if not exist "%CLASSES_DIR%" (
	mkdir "%CLASSES_DIR%"
)

rem Generate source file list
set SOURCES_FILE=%BUILD_DIR%\sources.txt
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
>"%SOURCES_FILE%" (
	for /R "%SRC_DIR%" %%F in (*.java) do (
		echo %%F
	)
)

javac -d "%CLASSES_DIR%" -cp "%SRC_DIR%" @"%SOURCES_FILE%"
if errorlevel 1 (
	echo Compilation failed.
	exit /b 1
)

del /q "%SOURCES_FILE%" >nul 2>&1
exit /b 0

:demo
call "%~f0" compile || exit /b 1
java -cp "%CLASSES_DIR%" %MAIN_CLASS%
exit /b %errorlevel%

:frontend
call "%~f0" compile || exit /b 1
java -cp "%CLASSES_DIR%" %FRONTEND_CLASS%
exit /b %errorlevel%

:demo_file
call "%~f0" compile || exit /b 1
java -cp "%CLASSES_DIR%" %FILE_DEMO_CLASS%
exit /b %errorlevel%

:clean
if exist "%BUILD_DIR%" (
	rmdir /S /Q "%BUILD_DIR%"
)
exit /b 0

:distclean
call "%~f0" clean
rem Remove stray .class files in repo root if any
for %%F in (*.class) do del /q "%%F" >nul 2>&1
exit /b 0

:tree
echo Project Structure:
for /R "%SRC_DIR%" %%F in (*.java) do echo %%F
exit /b 0

:help
echo Available targets:
echo   all        - Compile all Java files
echo   compile    - Compile all Java files
echo   demo       - Run basic demo
echo   demo-file  - Run file-based demo
echo   frontend   - Run GUI frontend
echo   clean      - Remove build files
echo   distclean  - Remove all generated files
echo   tree       - Show project structure
echo   help       - Show this help
exit /b 0

endlocal
