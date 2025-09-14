# Simple PowerShell script to start both services
Write-Host "🚀 Starting Logistics Tracking System..." -ForegroundColor Green
Write-Host ""

# Check if frontend dependencies are installed
if (!(Test-Path "frontend/node_modules")) {
    Write-Host "📦 Installing frontend dependencies..." -ForegroundColor Yellow
    Set-Location frontend
    npm install
    Set-Location ..
    Write-Host "✅ Frontend dependencies installed!" -ForegroundColor Green
}

Write-Host "🔧 Starting Backend (Spring Boot) on port 8000..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-Command", "mvn spring-boot:run" -WindowStyle Normal

Write-Host "⏳ Waiting 8 seconds for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 8

Write-Host "🎨 Starting Frontend (React) on port 3000..." -ForegroundColor Magenta
Set-Location frontend
Start-Process powershell -ArgumentList "-Command", "npm start" -WindowStyle Normal
Set-Location ..

Write-Host ""
Write-Host "🎉 Both services are starting!" -ForegroundColor Green
Write-Host "📡 Backend: http://localhost:8000" -ForegroundColor Cyan
Write-Host "🌐 Frontend: http://localhost:3000" -ForegroundColor Magenta
Write-Host ""
Write-Host "Press any key to exit..." -ForegroundColor White
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")