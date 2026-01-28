@echo off
echo ========================================
echo   BNS Hospital Management System
echo   Compilation and Startup Script
echo ========================================
echo.

echo Step 1: Cleaning previous compilation...
if exist bin rmdir /s /q bin
mkdir bin
echo ✓ Cleaned bin directory

echo.
echo Step 2: Compiling Java files...
echo.

javac -encoding UTF-8 -d bin ^
  src/main/*.java ^
  src/main/models/*.java ^
  src/main/views/*.java ^
  src/main/views/components/*.java ^
  src/main/services/*.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo ✗ Compilation failed!
    echo Please check the errors above.
    pause
    exit /b 1
)

echo.
echo ✓ Compilation successful!
echo   Compiled files saved to: bin\

echo.
echo Step 3: Creating data directory structure...
if not exist data mkdir data
if not exist data\logs mkdir data\logs
echo ✓ Created data directories

echo.
echo Step 4: Checking for default users CSV...
if not exist data\users.csv (
    echo ! No users.csv found, one will be created on startup
) else (
    echo ✓ Found existing users.csv
)

echo.
echo Step 5: Running BNS Hospital Management System...
echo ========================================
echo.
echo Starting application...
echo.

java -cp bin main.Application

echo.
echo ========================================
echo   Application closed.
echo ========================================
pause