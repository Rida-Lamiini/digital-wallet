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

# Run the deployment
docker-compose -f docker-compose.deploy.yml up -d

# Check if services are running
docker-compose -f docker-compose.deploy.yml ps

echo "Deployment completed. Services should be accessible at:"
echo "Application: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com"
echo "Frontend: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com"
echo "Backend API: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/api/"
