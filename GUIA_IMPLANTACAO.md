# Guia de Implantação - API de Gestão de Eventos

Este guia fornece instruções detalhadas para implantar a API de Gestão de Eventos em diferentes ambientes.

## 📋 Pré-requisitos

### Ambiente de Desenvolvimento
- **Java 17** ou superior
- **Maven 3.6** ou superior
- **Git** para controle de versão
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Ambiente de Produção
- **Java 17** JRE/JDK
- **PostgreSQL 12** ou superior (recomendado)
- **Nginx** (opcional, para proxy reverso)
- **Docker** (opcional, para containerização)

## 🚀 Implantação Local (Desenvolvimento)

### 1. Clonar o Repositório
```bash
git clone <url-do-repositorio>
cd eventos-api
```

### 2. Configurar Ambiente
```bash
# Verificar versão do Java
java -version

# Verificar versão do Maven
mvn -version
```

### 3. Compilar e Executar
```bash
# Compilar o projeto
mvn clean compile

# Executar testes
mvn test

# Executar a aplicação
mvn spring-boot:run
```

### 4. Verificar Funcionamento
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (vazio)

## 🏭 Implantação em Produção

### Opção 1: Servidor Tradicional

#### 1. Preparar o Servidor
```bash
# Instalar Java 17
sudo apt update
sudo apt install openjdk-17-jdk

# Verificar instalação
java -version
```

#### 2. Configurar PostgreSQL
```bash
# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib

# Criar base de dados
sudo -u postgres createdb eventos_db

# Criar utilizador
sudo -u postgres createuser --interactive eventos_user
```

#### 3. Configurar Aplicação
Criar `application-prod.properties`:
```properties
# Base de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/eventos_db
spring.datasource.username=eventos_user
spring.datasource.password=sua_senha_segura
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Logging
logging.level.com.portfolio.eventos=INFO
logging.file.name=/var/log/eventos-api/application.log

# Segurança
server.port=8080
server.servlet.context-path=/api
```

#### 4. Gerar e Implantar JAR
```bash
# Gerar JAR de produção
mvn clean package -Pprod

# Copiar para servidor
scp target/eventos-api-1.0.0.jar user@servidor:/opt/eventos-api/

# No servidor, criar serviço systemd
sudo nano /etc/systemd/system/eventos-api.service
```

Conteúdo do serviço:
```ini
[Unit]
Description=API de Gestão de Eventos
After=network.target

[Service]
Type=simple
User=eventos
ExecStart=/usr/bin/java -jar /opt/eventos-api/eventos-api-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 5. Iniciar Serviço
```bash
# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar e iniciar serviço
sudo systemctl enable eventos-api
sudo systemctl start eventos-api

# Verificar status
sudo systemctl status eventos-api
```

### Opção 2: Docker

#### 1. Criar Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

# Criar utilizador não-root
RUN addgroup --system eventos && adduser --system --group eventos

# Definir diretório de trabalho
WORKDIR /app

# Copiar JAR
COPY target/eventos-api-1.0.0.jar app.jar

# Alterar proprietário
RUN chown eventos:eventos app.jar

# Mudar para utilizador não-root
USER eventos

# Expor porta
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. Criar docker-compose.yml
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: eventos_db
      POSTGRES_USER: eventos_user
      POSTGRES_PASSWORD: sua_senha_segura
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  eventos-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/eventos_db
      SPRING_DATASOURCE_USERNAME: eventos_user
      SPRING_DATASOURCE_PASSWORD: sua_senha_segura
    depends_on:
      - postgres

volumes:
  postgres_data:
```

#### 3. Executar com Docker
```bash
# Construir e executar
docker-compose up -d

# Verificar logs
docker-compose logs -f eventos-api

# Parar serviços
docker-compose down
```

## 🔧 Configurações Avançadas

### Nginx como Proxy Reverso
```nginx
server {
    listen 80;
    server_name seu-dominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### SSL/HTTPS com Let's Encrypt
```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx

# Obter certificado
sudo certbot --nginx -d seu-dominio.com

# Renovação automática
sudo crontab -e
# Adicionar: 0 12 * * * /usr/bin/certbot renew --quiet
```

### Monitorização com Actuator
Adicionar ao `application-prod.properties`:
```properties
# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
management.info.env.enabled=true
```

## 🔒 Segurança em Produção

### 1. Configurações de Segurança
```properties
# Desabilitar H2 Console
spring.h2.console.enabled=false

# Configurar CORS adequadamente
cors.allowed-origins=https://seu-frontend.com

# Configurar JWT (se implementado)
jwt.secret=sua_chave_secreta_muito_longa_e_segura
jwt.expiration=86400000
```

### 2. Firewall
```bash
# Permitir apenas portas necessárias
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw enable
```

### 3. Backup da Base de Dados
```bash
# Script de backup
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
pg_dump -h localhost -U eventos_user eventos_db > /backup/eventos_db_$DATE.sql
```

## 📊 Monitorização e Logs

### 1. Configurar Logs
```properties
# Configuração de logging
logging.level.com.portfolio.eventos=INFO
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.file.name=/var/log/eventos-api/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### 2. Rotação de Logs
```bash
# Configurar logrotate
sudo nano /etc/logrotate.d/eventos-api
```

Conteúdo:
```
/var/log/eventos-api/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 eventos eventos
}
```

## 🚨 Resolução de Problemas

### Problemas Comuns

#### 1. Erro de Conexão com Base de Dados
```bash
# Verificar se PostgreSQL está a correr
sudo systemctl status postgresql

# Verificar conectividade
psql -h localhost -U eventos_user -d eventos_db
```

#### 2. Porta 8080 Ocupada
```bash
# Encontrar processo que usa a porta
sudo lsof -i :8080

# Matar processo se necessário
sudo kill -9 <PID>
```

#### 3. Problemas de Memória
```bash
# Aumentar heap size
java -Xmx2g -jar eventos-api-1.0.0.jar
```

### Logs Úteis
```bash
# Logs da aplicação
tail -f /var/log/eventos-api/application.log

# Logs do sistema
sudo journalctl -u eventos-api -f

# Logs do Docker
docker-compose logs -f eventos-api
```

## ✅ Checklist de Implantação

### Antes da Implantação
- [ ] Testes passam localmente
- [ ] Configurações de produção verificadas
- [ ] Base de dados configurada
- [ ] Backup da base de dados atual (se aplicável)
- [ ] Certificados SSL configurados
- [ ] Firewall configurado

### Durante a Implantação
- [ ] Aplicação compila sem erros
- [ ] Serviço inicia corretamente
- [ ] Endpoints respondem adequadamente
- [ ] Base de dados conecta corretamente
- [ ] Logs não mostram erros críticos

### Após a Implantação
- [ ] Testes de fumo executados
- [ ] Monitorização configurada
- [ ] Backup automático funcionando
- [ ] Documentação atualizada
- [ ] Equipe notificada

## 📞 Suporte

Para problemas ou dúvidas:
1. Verificar logs da aplicação
2. Consultar documentação
3. Verificar issues conhecidos
4. Contactar equipe de desenvolvimento

---

**Nota**: Este guia assume conhecimento básico de administração de sistemas Linux. Para ambientes Windows, adapte os comandos conforme necessário.

