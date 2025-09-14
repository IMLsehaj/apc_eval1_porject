@echo off
echo ========================================
echo   Logistics Tracking System Startup
echo ========================================
echo.

REM Check if frontend dependencies exist
if not exist "frontend\node_modules" (
    echo Installing frontend dependencies...
    cd frontend
    call npm install
    cd ..
    echo.
)

echo Starting services in separate windows...
echo.

REM Start backend in new window
echo Starting Spring Boot backend...
start "Backend - Spring Boot" cmd /k "mvn spring-boot:run"

REM Wait a moment
timeout /t 3 /nobreak >nul

REM Start frontend in new window  
echo Starting React frontend...
start "Frontend - React" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo Services are starting in separate windows:
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo ========================================
echo.
echo Press any key to close this window...
pause >nul