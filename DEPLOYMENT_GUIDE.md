# 🚀 Guia Completo de Deploy - API de Eventos

Este guia fornece instruções detalhadas para fazer o deploy da API de Eventos em diferentes ambientes, desde desenvolvimento local até produção em nuvem.

## 📋 Índice

1. [Preparação para Deploy](#preparação-para-deploy)
2. [Deploy Local](#deploy-local)
3. [Deploy com Docker](#deploy-com-docker)
4. [Deploy no Heroku](#deploy-no-heroku)
5. [Deploy na AWS](#deploy-na-aws)
6. [Deploy no Google Cloud](#deploy-no-google-cloud)
7. [Configuração de Banco de Dados](#configuração-de-banco-de-dados)
8. [Monitoramento e Logs](#monitoramento-e-logs)
9. [Troubleshooting](#troubleshooting)

## 🛠️ Preparação para Deploy

### 1. Verificação de Pré-requisitos

Antes de fazer o deploy, certifique-se de que:

- ✅ Todos os testes estão passando
- ✅ A aplicação compila sem erros
- ✅ As configurações de produção estão corretas
- ✅ As variáveis de ambiente estão definidas

```bash
# Executar testes
mvn clean test

# Verificar compilação
mvn clean compile

# Gerar JAR de produção
mvn clean package -Pprod
```

### 2. Configuração de Perfis

#### application-prod.properties
```properties
# Configurações de Produção
spring.application.name=eventos-api
server.port=${PORT:8080}

# Banco de Dados PostgreSQL
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/eventos_db}
spring.datasource.username=${DB_USERNAME:eventos_user}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.com.portfolio.eventos=INFO
logging.level.org.springframework.security=WARN
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Swagger (desabilitado em produção)
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false

# Configurações de segurança
server.error.include-message=never
server.error.include-binding-errors=never
server.error.include-stacktrace=never
```

### 3. Dockerfile

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

# Metadados
LABEL maintainer="seu.email@exemplo.com"
LABEL description="API de Gestão de Eventos"
LABEL version="1.0.0"

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Criar usuário não-root
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Diretório de trabalho
WORKDIR /app

# Copiar JAR
COPY target/eventos-api-1.0-SNAPSHOT.jar app.jar

# Alterar proprietário
RUN chown appuser:appuser app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### 4. docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: eventos-postgres
    environment:
      POSTGRES_DB: eventos_db
      POSTGRES_USER: eventos_user
      POSTGRES_PASSWORD: eventos_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - eventos-network

  eventos-api:
    build: .
    container_name: eventos-api
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: jdbc:postgresql://postgres:5432/eventos_db
      DB_USERNAME: eventos_user
      DB_PASSWORD: eventos_password
      JWT_SECRET: sua_chave_jwt_super_secreta_com_pelo_menos_256_bits
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    networks:
      - eventos-network
    restart: unless-stopped

volumes:
  postgres_data:

networks:
  eventos-network:
    driver: bridge
```

## 🐳 Deploy com Docker

### 1. Build da Imagem

```bash
# Construir JAR
mvn clean package -DskipTests

# Construir imagem Docker
docker build -t eventos-api:latest .

# Verificar imagem criada
docker images | grep eventos-api
```

### 2. Executar com Docker Compose

```bash
# Subir todos os serviços
docker-compose up -d

# Verificar status
docker-compose ps

# Ver logs
docker-compose logs -f eventos-api

# Parar serviços
docker-compose down
```

### 3. Deploy em Registry

```bash
# Tag para registry
docker tag eventos-api:latest seu-registry/eventos-api:latest

# Push para registry
docker push seu-registry/eventos-api:latest
```

## ☁️ Deploy no Heroku

### 1. Preparação

```bash
# Instalar Heroku CLI
# https://devcenter.heroku.com/articles/heroku-cli

# Login
heroku login

# Criar aplicação
heroku create sua-app-eventos-api
```

### 2. Configuração

```bash
# Configurar variáveis de ambiente
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set JWT_SECRET=sua_chave_jwt_super_secreta_com_pelo_menos_256_bits
heroku config:set JWT_EXPIRATION=86400000

# Adicionar PostgreSQL
heroku addons:create heroku-postgresql:hobby-dev

# Verificar configurações
heroku config
```

### 3. Deploy

```bash
# Adicionar remote do Heroku
heroku git:remote -a sua-app-eventos-api

# Deploy
git push heroku main

# Verificar logs
heroku logs --tail

# Abrir aplicação
heroku open
```

### 4. Procfile

```
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/eventos-api-1.0-SNAPSHOT.jar
```

## 🌐 Deploy na AWS

### 1. EC2 Instance

#### Configuração da Instância

```bash
# Conectar à instância
ssh -i sua-chave.pem ec2-user@seu-ip-publico

# Atualizar sistema
sudo yum update -y

# Instalar Java 17
sudo yum install -y java-17-amazon-corretto

# Instalar PostgreSQL
sudo yum install -y postgresql postgresql-server

# Configurar PostgreSQL
sudo postgresql-setup initdb
sudo systemctl enable postgresql
sudo systemctl start postgresql
```

#### Script de Deploy

```bash
#!/bin/bash
# deploy.sh

# Variáveis
APP_NAME="eventos-api"
JAR_FILE="eventos-api-1.0-SNAPSHOT.jar"
APP_DIR="/opt/eventos-api"
SERVICE_FILE="/etc/systemd/system/eventos-api.service"

# Criar diretório da aplicação
sudo mkdir -p $APP_DIR

# Copiar JAR
sudo cp target/$JAR_FILE $APP_DIR/

# Criar arquivo de serviço
sudo tee $SERVICE_FILE > /dev/null <<EOF
[Unit]
Description=Eventos API
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java -jar $JAR_FILE
Restart=always
RestartSec=10

Environment=SPRING_PROFILES_ACTIVE=prod
Environment=DB_URL=jdbc:postgresql://localhost:5432/eventos_db
Environment=DB_USERNAME=eventos_user
Environment=DB_PASSWORD=sua_senha
Environment=JWT_SECRET=sua_chave_jwt

[Install]
WantedBy=multi-user.target
EOF

# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar e iniciar serviço
sudo systemctl enable eventos-api
sudo systemctl start eventos-api

# Verificar status
sudo systemctl status eventos-api
```

### 2. RDS (PostgreSQL)

```bash
# Criar instância RDS via AWS CLI
aws rds create-db-instance \
    --db-instance-identifier eventos-db \
    --db-instance-class db.t3.micro \
    --engine postgres \
    --master-username eventos_user \
    --master-user-password sua_senha_segura \
    --allocated-storage 20 \
    --vpc-security-group-ids sg-xxxxxxxxx
```

### 3. Load Balancer

```yaml
# application-load-balancer.yml
AWSTemplateFormatVersion: '2010-09-09'
Resources:
  EventosALB:
    Type: AWS::ElasticLoadBalancingV2::LoadBalancer
    Properties:
      Name: eventos-api-alb
      Scheme: internet-facing
      Type: application
      Subnets:
        - subnet-xxxxxxxxx
        - subnet-yyyyyyyyy
      SecurityGroups:
        - !Ref ALBSecurityGroup

  EventosTargetGroup:
    Type: AWS::ElasticLoadBalancingV2::TargetGroup
    Properties:
      Name: eventos-api-targets
      Port: 8080
      Protocol: HTTP
      VpcId: vpc-xxxxxxxxx
      HealthCheckPath: /actuator/health
      HealthCheckProtocol: HTTP
```

## 🔧 Configuração de Banco de Dados

### PostgreSQL em Produção

#### 1. Instalação e Configuração

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# CentOS/RHEL
sudo yum install postgresql-server postgresql-contrib
sudo postgresql-setup initdb
```

#### 2. Configuração de Segurança

```sql
-- Conectar como postgres
sudo -u postgres psql

-- Criar banco e usuário
CREATE DATABASE eventos_db;
CREATE USER eventos_user WITH PASSWORD 'senha_super_segura';
GRANT ALL PRIVILEGES ON DATABASE eventos_db TO eventos_user;

-- Configurar permissões
ALTER USER eventos_user CREATEDB;
\q
```

#### 3. Configuração do postgresql.conf

```bash
# Editar configuração
sudo nano /etc/postgresql/13/main/postgresql.conf

# Configurações recomendadas
listen_addresses = 'localhost'
port = 5432
max_connections = 100
shared_buffers = 128MB
effective_cache_size = 512MB
```

#### 4. Backup e Restore

```bash
# Backup
pg_dump -h localhost -U eventos_user eventos_db > backup.sql

# Restore
psql -h localhost -U eventos_user eventos_db < backup.sql
```

## 📊 Monitoramento e Logs

### 1. Spring Boot Actuator

```properties
# application-prod.properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.metrics.export.prometheus.enabled=true
```

### 2. Configuração de Logs

```xml
<!-- logback-spring.xml -->
<configuration>
    <springProfile name="prod">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>/var/log/eventos-api/application.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>/var/log/eventos-api/application.%d{yyyy-MM-dd}.log</fileNamePattern>
                <maxHistory>30</maxHistory>
            </rollingPolicy>
            <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

### 3. Prometheus e Grafana

```yaml
# docker-compose.monitoring.yml
version: '3.8'

services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```

## 🔍 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com Banco

```bash
# Verificar conectividade
telnet localhost 5432

# Verificar logs do PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-13-main.log

# Testar conexão
psql -h localhost -U eventos_user -d eventos_db
```

#### 2. Erro de Memória

```bash
# Verificar uso de memória
free -h
top -p $(pgrep java)

# Ajustar configurações JVM
export JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC"
```

#### 3. Erro de Porta em Uso

```bash
# Verificar processo usando a porta
sudo lsof -i :8080
sudo netstat -tulpn | grep 8080

# Matar processo
sudo kill -9 PID
```

### Logs Úteis

```bash
# Logs da aplicação
tail -f /var/log/eventos-api/application.log

# Logs do sistema
journalctl -u eventos-api -f

# Logs do Docker
docker logs -f eventos-api
```

### Comandos de Diagnóstico

```bash
# Status da aplicação
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics

# Informações da aplicação
curl http://localhost:8080/actuator/info

# Verificar conectividade
curl -I http://localhost:8080/api/eventos
```

## 📝 Checklist de Deploy

### Pré-Deploy
- [ ] Testes passando
- [ ] Código revisado
- [ ] Configurações de produção verificadas
- [ ] Backup do banco de dados atual
- [ ] Variáveis de ambiente configuradas

### Durante o Deploy
- [ ] Aplicação buildada com sucesso
- [ ] Deploy executado sem erros
- [ ] Serviços iniciados corretamente
- [ ] Health checks passando

### Pós-Deploy
- [ ] Endpoints funcionando
- [ ] Banco de dados acessível
- [ ] Logs sem erros críticos
- [ ] Monitoramento ativo
- [ ] Documentação atualizada

---

Este guia fornece uma base sólida para fazer o deploy da API de Eventos em diferentes ambientes. Adapte as configurações conforme suas necessidades específicas e sempre teste em ambiente de staging antes de fazer deploy em produção.

