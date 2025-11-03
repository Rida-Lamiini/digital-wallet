# E-Wallet AWS EC2 Deployment Guide

This guide provides a complete production-ready deployment of the E-Wallet application on AWS EC2.

## Instance Details

- **Instance ID**: i-0fda39f9670595bf2
- **Public IP**: 16.170.214.112
- **Public DNS**: ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
- **Region**: eu-north-1
- **Instance Type**: m7i-flex.large

## Quick Deployment (Automated)

### 1. Initial EC2 Setup

```bash
# Connect to your EC2 instance
ssh -i your-key.pem ubuntu@ec2-16-170-214-112.eu-north-1.compute.amazonaws.com

# Run the AWS setup script
wget https://raw.githubusercontent.com/rida999/digital-wallet/main/aws-setup.sh
chmod +x aws-setup.sh
sudo ./aws-setup.sh
```

### 2. Deploy Application

```bash
# Clone repository
cd /opt/e-wallet
git clone https://github.com/rida999/digital-wallet.git .
# OR copy files manually

# Create environment file
nano .env
# Add your environment variables:
# db_name=ewallet
# db_username=ewallet_user
# db_password=your_secure_password
# jwt_secret=your_jwt_secret_key

# Run deployment
./deploy.sh
```

## Manual Deployment Steps

### Prerequisites

- Ubuntu/Debian-based EC2 instance
- SSH access with key pair
- Security group allowing ports 22, 80, 443

### 1. System Preparation

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install monitoring tools
sudo apt install -y htop iotop ncdu fail2ban lsof

# Configure firewall
sudo ufw --force enable
sudo ufw allow ssh
sudo ufw allow 80
sudo ufw allow 443
```

### 2. Application Setup

```bash
# Create application directory
sudo mkdir -p /opt/e-wallet
sudo chown -R $USER:$USER /opt/e-wallet
cd /opt/e-wallet

# Clone or copy application files
git clone https://github.com/rida999/digital-wallet.git .

# Create environment configuration
cp .env.example .env
nano .env
```

### 3. Environment Configuration

Create `.env` file with:

```env
# Database Configuration
db_name=ewallet
db_username=ewallet_user
db_password=your_secure_password_here

# JWT Configuration
jwt_secret=your_jwt_secret_key_here
```

### 4. Deploy Application

```bash
# Make scripts executable
chmod +x deploy.sh aws-setup.sh

# Run deployment
./deploy.sh
```

## Architecture Overview

```
Internet → Nginx (Port 80/443) → Frontend (React)
                              → Backend API (Spring Boot)
                              → Database (PostgreSQL)
```

### Services

- **Nginx**: Reverse proxy, SSL termination, static file serving
- **Frontend**: React SPA served through Nginx
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL with persistent storage

## SSL Configuration (Production)

```bash
# Install Certbot
sudo apt install -y certbot python3-certbot-nginx

# Get SSL certificate (replace with your domain)
sudo certbot --nginx -d your-domain.com

# Test renewal
sudo certbot renew --dry-run
```

## Monitoring & Maintenance

### Health Checks

```bash
# Check service status
docker-compose -f docker-compose.deploy.yml ps

# View logs
docker-compose -f docker-compose.deploy.yml logs -f

# Monitor resources
docker stats
htop
```

### Backup & Recovery

```bash
# Manual backup
/opt/e-wallet/backup.sh

# View backup files
ls -la /opt/e-wallet/backups/
```

### Updates

```bash
# Update application
cd /opt/e-wallet
git pull
docker-compose -f docker-compose.deploy.yml pull
docker-compose -f docker-compose.deploy.yml up -d

# Update system
sudo apt update && sudo apt upgrade -y
```

## Security Best Practices

### AWS Security Groups

- **Inbound Rules**:
  - SSH (22) - Your IP only
  - HTTP (80) - 0.0.0.0/0
  - HTTPS (443) - 0.0.0.0/0

### Instance Security

```bash
# Disable password authentication
sudo sed -i 's/#PasswordAuthentication yes/PasswordAuthentication no/' /etc/ssh/sshd_config
sudo systemctl restart ssh

# Set up fail2ban
sudo systemctl enable fail2ban
sudo systemctl start fail2ban
```

### Application Security

- Use strong passwords for database
- Keep JWT secrets secure
- Regularly update Docker images
- Monitor logs for suspicious activity

## Troubleshooting

### Common Issues

1. **Port 80 already in use**

   ```bash
   sudo lsof -i :80
   sudo fuser -k 80/tcp
   ```

2. **Container fails to start**

   ```bash
   docker-compose -f docker-compose.deploy.yml logs <service_name>
   ```

3. **Database connection issues**

   ```bash
   docker-compose -f docker-compose.deploy.yml exec db psql -U $db_username -d $db_name
   ```

4. **Frontend not loading**
   ```bash
   docker-compose -f docker-compose.deploy.yml logs frontend
   # Check if React app started properly
   ```

### Performance Tuning

```bash
# Adjust Docker resource limits in docker-compose.deploy.yml
# Increase memory limits if needed
# Configure swap space for better performance
```

## Access URLs

- **Application**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com
- **API**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/api/
- **Health Check**: http://ec2-16-170-214-112.eu-north-1.compute.amazonaws.com/api/health

## Support

For issues or questions:

1. Check logs: `docker-compose -f docker-compose.deploy.yml logs`
2. Verify environment variables in `.env`
3. Ensure all required ports are open in security groups
4. Check system resources: `df -h` and `free -h`
