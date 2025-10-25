@echo off

echo =================================================
echo           BUILDING PROFILE TOOL
echo =================================================

REM -- Build Backend --
echo.
echo [INFO] Building Spring Boot backend...
cd backend
call mvn clean package
if %errorlevel% neq 0 (
    echo [ERROR] Backend build failed.
    exit /b %errorlevel%
)
cd ..
echo [INFO] Backend build successful.

REM -- Build Frontend --
echo.
echo [INFO] Building Angular frontend...
cd frontend
call npm run build
if %errorlevel% neq 0 (
    echo [ERROR] Frontend build failed.
    exit /b %errorlevel%
)
cd ..
echo [INFO] Frontend build successful.

echo.
echo =================================================
echo           BUILD COMPLETED SUCCESSFULLY
echo =================================================
