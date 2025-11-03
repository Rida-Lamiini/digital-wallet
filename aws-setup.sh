#!/bin/bash

# AWS EC2 Setup Script for E-Wallet Application
# This script configures the EC2 instance for production deployment

set -e

echo "=== AWS EC2 Setup for E-Wallet Application ==="

# Update system
echo "Updating system packages..."
sudo apt update && sudo apt upgrade -y

# Install required packages
echo "Installing required packages..."
sudo apt install -y curl wget git unzip software-properties-common ufw

# Install Docker
echo "Installing Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
fi

# Install Docker Compose
echo "Installing Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

# Install additional tools
echo "Installing additional tools..."
sudo apt install -y htop iotop ncdu fail2ban lsof

# Configure firewall
echo "Configuring firewall..."
sudo ufw --force enable
sudo ufw allow ssh
sudo ufw allow 80
sudo ufw allow 443
sudo ufw --force reload

# Create application directory
echo "Creating application directory..."
sudo mkdir -p /opt/e-wallet
sudo chown -R $USER:$USER /opt/e-wallet

# Create swap space if needed (for t3.micro instances)
if [ ! -f /swapfile ]; then
    echo "Creating swap space..."
    sudo fallocate -l 1G /swapfile
    sudo chmod 600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile
    echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
fi

# Configure Docker daemon for production
echo "Configuring Docker for production..."
sudo mkdir -p /etc/docker
cat <<EOF | sudo tee /etc/docker/daemon.json
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m",
    "max-file": "5"
  },
  "storage-driver": "overlay2",
  "iptables": false,
  "bridge": "none"
}
EOF

sudo systemctl restart docker

# Install Certbot for SSL (optional)
echo "Installing Certbot for SSL..."
sudo apt install -y certbot python3-certbot-nginx

# Create systemd service for application monitoring
echo "Creating systemd service for application monitoring..."
cat <<EOF | sudo tee /etc/systemd/system/e-wallet-monitor.service
[Unit]
Description=E-Wallet Application Monitor
After=docker.service
Requires=docker.service

[Service]
Type=oneshot
User=$USER
WorkingDirectory=/opt/e-wallet
ExecStart=/opt/e-wallet/monitor.sh
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
EOF

# Create monitoring script
cat <<EOF | tee /opt/e-wallet/monitor.sh
#!/bin/bash
# E-Wallet Application Health Monitor

cd /opt/e-wallet

# Check if all services are running
if docker-compose -f docker-compose.deploy.yml ps | grep -q "Up"; then
    echo "All services are running"
    exit 0
else
    echo "Some services are down, restarting..."
    docker-compose -f docker-compose.deploy.yml restart
    exit 1
fi
EOF

chmod +x /opt/e-wallet/monitor.sh

# Set up log rotation
echo "Setting up log rotation..."
cat <<EOF | sudo tee /etc/logrotate.d/e-wallet
/opt/e-wallet/logs/*.log {
    daily
    missingok
    rotate 7
    compress
    delaycompress
    notifempty
    create 644 $USER $USER
    postrotate
        docker-compose -f /opt/e-wallet/docker-compose.deploy.yml logs -f --tail=0 > /dev/null 2>&1 || true
    endscript
}
EOF

# Create backup script
echo "Creating backup script..."
cat <<EOF | tee /opt/e-wallet/backup.sh
#!/bin/bash
# E-Wallet Database Backup Script

BACKUP_DIR="/opt/e-wallet/backups"
DATE=\$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="\$BACKUP_DIR/e-wallet_backup_\$DATE.sql"

mkdir -p \$BACKUP_DIR

# Backup PostgreSQL database
docker exec e-wallet-db pg_dump -U \${db_username} \${db_name} > \$BACKUP_FILE

# Compress backup
gzip \$BACKUP_FILE

# Keep only last 7 backups
cd \$BACKUP_DIR
ls -t *.sql.gz | tail -n +8 | xargs -r rm

echo "Backup completed: \$BACKUP_FILE.gz"
EOF

chmod +x /opt/e-wallet/backup.sh

# Set up cron jobs
echo "Setting up cron jobs..."
(crontab -l ; echo "0 2 * * * /opt/e-wallet/backup.sh") | crontab -
(crontab -l ; echo "*/5 * * * * /opt/e-wallet/monitor.sh") | crontab -

echo "=== AWS EC2 Setup Complete ==="
echo ""
echo "Next steps:"
echo "1. Copy your application files to /opt/e-wallet/"
echo "2. Create your .env file with database and JWT credentials"
echo "3. Run the deployment: cd /opt/e-wallet && ./deploy.sh"
echo "4. Set up SSL certificate: sudo certbot --nginx -d your-domain.com"
echo ""
echo "Useful commands:"
echo "- Check services: docker-compose -f docker-compose.deploy.yml ps"
echo "- View logs: docker-compose -f docker-compose.deploy.yml logs -f"
echo "- Monitor resources: htop"
echo "- Check firewall: sudo ufw status"
