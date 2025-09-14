# Logistics Tracking System

# 🚀 Logistics Tracking System

A full-stack web application for logistics and shipment tracking with Spring Boot backend and React frontend.

## 🎯 **One-Command Startup Options**

Here are **4 simple approaches** to start both frontend and backend with one command:

### **Option 1: Simple Command (Recommended) ⭐**
```bash
run.cmd
```
Just double-click `run.cmd` in file explorer or run it in terminal.

### **Option 2: NPM Script**
```bash
npm start
```

### **Option 3: PowerShell Script (Pretty Output)**
```powershell
PowerShell -ExecutionPolicy Bypass -File start-services.ps1
```

### **Option 4: Manual NPM**
```bash
npm run start
```

## 🌐 **Application URLs**
- **Backend API**: http://localhost:8000
- **Frontend Web App**: http://localhost:3000

## 📋 **What Each Option Does**

All options will:
1. ✅ Check and install frontend dependencies if needed
2. 🚀 Start Spring Boot backend on port **8000**
3. 🎨 Start React frontend on port **3000** 
4. 🌐 Automatically open your browser to the frontend
5. 📱 Run both services simultaneously

## Features

- User authentication and authorization (Admin, Manager, Customer roles)
- Shipment tracking and management
- Real-time status updates
- Role-based access control
- RESTful API backend
- Modern React frontend

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Node.js 14+ and npm
- Git

### One-Command Startup

1. Clone the repository
2. Navigate to the project directory
3. Run one of these commands:

**Windows:**
```bash
start.bat
```

**Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

**Using npm directly:**
```bash
npm install
npm run dev
```

This will:
- Install all dependencies
- Start the Spring Boot backend on `http://localhost:8080`
- Start the React frontend on `http://localhost:3000`

### Manual Setup

If you prefer to run services separately:

**Backend:**
```bash
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

## API Endpoints

### Authentication
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login

### User Management
- `GET /api/users` - Get all users (Admin only)
- `PUT /api/users/update` - Update user profile
- `DELETE /api/users/{id}` - Delete user (Admin only)

### Shipment Management
- `POST /api/shipments` - Create new shipment
- `GET /api/shipments/track/{trackingNumber}` - Track shipment
- `PUT /api/shipments/{id}/status` - Update shipment status
- `PUT /api/shipments/{id}/fees` - Update shipment fees

## User Roles

- **Admin**: Full system access, user management, all shipment operations
- **Manager**: Shipment management, status updates, tracking
- **Customer**: Create shipments, track own shipments, view profile

## Technology Stack

**Backend:**
- Spring Boot 2.7+
- Spring Data JPA
- H2 Database (development)
- Maven

**Frontend:**
- React 18
- React Router
- Axios for API calls
- Context API for state management

## Development

The application uses:
- Backend runs on port 8080
- Frontend runs on port 3000
- CORS is configured for cross-origin requests
- Hot reload enabled for both services

## Project Structure

```
├── src/main/java/           # Spring Boot backend
├── frontend/                # React frontend
│   ├── src/
│   │   ├── components/     # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   └── context/        # React contexts
├── start.bat               # Windows startup script
├── start.sh                # Linux/Mac startup script
└── package.json            # Root package.json
```
