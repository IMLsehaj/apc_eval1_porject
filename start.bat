@echo off
echo Starting Logistics Tracking System...
echo.

echo Installing frontend dependencies if needed...
if not exist "frontend/node_modules" (
    echo Installing frontend dependencies...
    cd frontend
    npm install
    cd ..
)

echo.
echo Starting backend (Spring Boot)...
start /b mvn spring-boot:run

echo Waiting for backend to initialize...
timeout /t 10 /nobreak > nul

echo Starting frontend (React)...
cd frontend
start npm start
cd ..

echo.
echo Both services are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Press any key to stop all services...
pause > nul

echo Stopping services...
taskkill /f /im java.exe 2>nul
taskkill /f /im node.exe 2>nul