#!/bin/bash
echo "Starting Logistics Tracking System..."
echo

echo "Installing dependencies if needed..."
if [ ! -d "node_modules" ]; then
    echo "Installing root dependencies..."
    npm install
fi

if [ ! -d "frontend/node_modules" ]; then
    echo "Installing frontend dependencies..."
    cd frontend
    npm install
    cd ..
fi

echo
echo "Starting both backend and frontend..."
npm run dev