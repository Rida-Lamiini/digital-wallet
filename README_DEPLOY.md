# E-Wallet Deployment Guide

This guide explains how to deploy the E-Wallet application to an AWS EC2 instance using Docker and Docker Compose.

## Prerequisites

- AWS EC2 instance with Ubuntu/Debian-based OS
- Docker and Docker Compose installed on the EC2 instance
- SSH access to the EC2 instance

## Instance Details

- **Instance ID**: i-0fda39f9670595bf2
- **Public IP**: 16.170.214.112
- **Public DNS**: ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
- **Region**: eu-north-1
- **Instance Type**: m7i-flex.large

## Deployment Steps

### 1. Connect to EC2 Instance

```bash
ssh -i your-key.pem ubuntu@ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
```

### 2. Clone the Repository

```bash
git clone https://github.com/rida999/digital-wallet.git
cd digital-wallet
```

### 3. Set Up Environment Variables

Create a `.env` file with your database and JWT configuration:

```bash
cp .env.example .env
# Edit .env with your actual values
nano .env
```

Required environment variables:

- `db_name`: Database name
- `db_username`: Database username
- `db_password`: Database password
- `jwt_secret`: JWT secret key

### 4. Run the Deployment Script

```bash
chmod +x deploy.sh
./deploy.sh
```

This script will:

- Update system packages
- Install Docker and Docker Compose (if not present)
- Pull the latest images from Docker Hub
- Start all services using Docker Compose

### 5. Verify Deployment

Check if all services are running:

```bash
docker-compose -f docker-compose.deploy.yml ps
```

Check logs:

```bash
docker-compose -f docker-compose.deploy.yml logs
```

## Access the Application

- **Main Application**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
- **Frontend**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
- **Backend API**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/api/

## Architecture

The deployment uses the following services:

- **Nginx**: Reverse proxy and load balancer (port 80)
- **Frontend**: React application (served through Nginx)
- **Backend**: Spring Boot API (internal port 8080)
- **Database**: PostgreSQL (internal port 5432)

## Troubleshooting

### Check Service Status

```bash
docker-compose -f docker-compose.deploy.yml ps
```

### View Logs

```bash
# All services
docker-compose -f docker-compose.deploy.yml logs

# Specific service
docker-compose -f docker-compose.deploy.yml logs backend
docker-compose -f docker-compose.deploy.yml logs frontend
docker-compose -f docker-compose.deploy.yml logs db
```

### Restart Services

```bash
docker-compose -f docker-compose.deploy.yml restart
```

### Update Images

```bash
docker-compose -f docker-compose.deploy.yml pull
docker-compose -f docker-compose.deploy.yml up -d
```

## Security Considerations

- Ensure your EC2 instance has proper security groups configured
- Use HTTPS in production (consider AWS Certificate Manager and ALB)
- Regularly update Docker images
- Monitor logs for security issues

## Monitoring

- Use `docker stats` to monitor container resource usage
- Check application logs regularly
- Set up CloudWatch alarms for EC2 metrics
