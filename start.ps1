# PowerShell script to start both frontend and backend
Write-Host "Starting Logistics Tracking System..." -ForegroundColor Green
Write-Host ""

# Check if frontend dependencies are installed
if (-not (Test-Path "frontend/node_modules")) {
    Write-Host "Installing frontend dependencies..." -ForegroundColor Yellow
    Set-Location frontend
    npm install
    Set-Location ..
}

Write-Host ""
Write-Host "Starting backend (Spring Boot) in background..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-Command", "mvn spring-boot:run" -WindowStyle Minimized

Write-Host "Waiting 10 seconds for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Starting frontend (React)..." -ForegroundColor Cyan
Set-Location frontend
Start-Process powershell -ArgumentList "-Command", "npm start" -WindowStyle Normal
Set-Location ..

Write-Host ""
Write-Host "Both services are starting!" -ForegroundColor Green
Write-Host "Backend: http://localhost:8080" -ForegroundColor White
Write-Host "Frontend: http://localhost:3000" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")