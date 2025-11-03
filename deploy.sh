#!/bin/bash

# Update system packages
sudo apt update && sudo apt upgrade -y

# Install Docker if not already installed
if ! command -v docker &> /dev/null; then
    echo "Installing Docker..."
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
    newgrp docker
fi

# Install Docker Compose if not already installed
if ! command -v docker-compose &> /dev/null; then
    echo "Installing Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

# Install lsof if not available (needed for port checking)
if ! command -v lsof &> /dev/null; then
    echo "Installing lsof..."
    sudo apt update && sudo apt install -y lsof
fi

# Clone the repository (assuming it's public or you have access)
# Replace with your actual repository URL
# git clone https://github.com/rida999/e-wallet-backend.git
# git clone https://github.com/rida999/e-wallet-frontend.git

# Navigate to the project directory
# cd digital-wallet

# Copy environment file (you need to provide this)
# cp .env.example .env

# Pull the latest images from Docker Hub
docker pull rida999/e-wallet-backend:latest
docker pull rida999/e-wallet-frontend:latest

# Stop and remove existing containers if they exist
docker-compose -f docker-compose.deploy.yml down --remove-orphans

# Clean up any dangling containers with the same names
docker rm -f e-wallet-nginx e-wallet-frontend e-wallet-backend e-wallet-db 2>/dev/null || true

# Check if port 80 is in use and kill the process
if lsof -Pi :80 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "Port 80 is in use. Stopping the process..."
    sudo fuser -k 80/tcp 2>/dev/null || true
    sleep 2
fi

# Choose deployment configuration
if [ "$1" = "prod" ]; then
    COMPOSE_FILE="docker-compose.prod.yml"
    echo "Using production configuration..."
else
    COMPOSE_FILE="docker-compose.deploy.yml"
    echo "Using development configuration..."
fi

# Run the deployment
docker-compose -f $COMPOSE_FILE up -d

# Wait a moment for services to start
sleep 10

# Check if services are running
docker-compose -f $COMPOSE_FILE ps

echo ""
echo "Deployment completed. Services should be accessible at:"
if [ "$COMPOSE_FILE" = "docker-compose.prod.yml" ]; then
    echo "Application: https://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com"
    echo "Health Check: https://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/health"
else
    echo "Application: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com"
    echo "Frontend: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com"
    echo "Backend API: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/api/"
fi
echo ""
echo "Note: If you see the Nginx welcome page instead of the React app,"
echo "the frontend container might still be starting. Wait a few minutes and refresh."
echo "You can check the frontend logs with: docker-compose -f $COMPOSE_FILE logs frontend"
echo ""
echo "To check all service logs: docker-compose -f $COMPOSE_FILE logs"
echo "To restart services: docker-compose -f $COMPOSE_FILE restart"
echo "To stop services: docker-compose -f $COMPOSE_FILE down"
echo "To view service status: docker-compose -f $COMPOSE_FILE ps"
