# 🎯 API de Gestão de Eventos

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

Uma API REST robusta e escalável para gestão completa de eventos, participantes e inscrições, desenvolvida com Spring Boot 3.2 e Java 17. Este projeto demonstra as melhores práticas de desenvolvimento backend, incluindo arquitetura limpa, testes abrangentes, documentação completa e configuração para produção.

## 🚀 Características Principais

### Funcionalidades Core
- **Gestão Completa de Eventos**: Criação, edição, listagem e exclusão de eventos com suporte a filtros avançados
- **Sistema de Participantes**: Cadastro e gerenciamento de participantes com diferentes níveis de acesso
- **Controle de Inscrições**: Sistema completo de inscrições com confirmação, cancelamento e controle de capacidade
- **Categorização**: Organização de eventos por categorias personalizáveis
- **Gestão de Locais**: Cadastro e gerenciamento de locais para realização dos eventos

### Recursos Técnicos
- **API RESTful**: Endpoints bem estruturados seguindo padrões REST
- **Documentação Interativa**: Swagger UI integrado para teste e documentação da API
- **Validação Robusta**: Validação de dados com Bean Validation (JSR-303)
- **Tratamento de Exceções**: Sistema global de tratamento de erros
- **Paginação**: Suporte completo a paginação e ordenação
- **CORS**: Configuração adequada para integração frontend
- **Testes Automatizados**: Cobertura de testes unitários e de integração

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/portfolio/eventos/
├── config/          # Configurações (Segurança, CORS, etc.)
├── controller/      # Controladores REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── exception/      # Tratamento de exceções
├── repository/     # Repositórios JPA
└── service/        # Lógica de negócio
```

### Tecnologias Utilizadas

| Categoria | Tecnologia | Versão | Propósito |
|-----------|------------|--------|-----------|
| **Core** | Java | 17 | Linguagem principal |
| **Framework** | Spring Boot | 3.2.0 | Framework principal |
| **Persistência** | Spring Data JPA | 3.2.0 | Acesso a dados |
| **Banco de Dados** | H2 Database | Runtime | Desenvolvimento/Testes |
| **Banco de Dados** | PostgreSQL | Runtime | Produção |
| **Segurança** | Spring Security | 6.2.0 | Autenticação/Autorização |
| **Documentação** | Springdoc OpenAPI | 2.2.0 | Documentação da API |
| **Autenticação** | JWT | 0.12.3 | Tokens de acesso |
| **Validação** | Bean Validation | 3.0.2 | Validação de dados |
| **Build** | Maven | 3.6+ | Gerenciamento de dependências |
| **Testes** | JUnit 5 | 5.9+ | Framework de testes |
| **Testes** | Mockito | 4.8+ | Mocking para testes |

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6** ou superior
- **Git** para clonagem do repositório

### Verificação dos Pré-requisitos

```bash
# Verificar versão do Java
java -version

# Verificar versão do Maven
mvn -version

# Verificar versão do Git
git --version
```

## 🛠️ Instalação e Configuração

### 1. Clonagem do Repositório

```bash
git clone https://github.com/seu-usuario/eventos-api.git
cd eventos-api
```

### 2. Compilação do Projeto

```bash
# Limpar e compilar o projeto
mvn clean compile

# Executar testes
mvn test

# Gerar o JAR executável
mvn clean package
```

### 3. Execução da Aplicação

#### Modo Desenvolvimento
```bash
# Executar com Maven
mvn spring-boot:run

# Ou executar o JAR gerado
java -jar target/eventos-api-1.0-SNAPSHOT.jar
```

#### Configuração de Perfis

A aplicação suporta diferentes perfis de execução:

- **dev**: Desenvolvimento (H2 Database, logs detalhados)
- **test**: Testes (H2 em memória)
- **prod**: Produção (PostgreSQL, logs otimizados)

```bash
# Executar com perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🌐 Acesso à Aplicação

Após iniciar a aplicação, os seguintes endpoints estarão disponíveis:

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **API Base** | http://localhost:8080 | Endpoint principal da API |
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html | Interface de documentação interativa |
| **API Docs** | http://localhost:8080/v3/api-docs | Documentação OpenAPI em JSON |
| **H2 Console** | http://localhost:8080/h2-console | Console do banco H2 (apenas dev) |

### Credenciais H2 Console (Desenvolvimento)
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(vazio)*

## 📚 Documentação da API

### Endpoints Principais

#### Eventos
- `GET /api/eventos` - Listar eventos (com paginação)
- `GET /api/eventos/{id}` - Buscar evento por ID
- `POST /api/eventos` - Criar novo evento
- `PUT /api/eventos/{id}` - Atualizar evento
- `DELETE /api/eventos/{id}` - Excluir evento
- `GET /api/eventos/proximos` - Eventos próximos
- `GET /api/eventos/populares` - Eventos mais populares
- `GET /api/eventos/filtrar` - Busca com filtros

#### Participantes
- `GET /api/participantes` - Listar participantes
- `GET /api/participantes/{id}` - Buscar participante por ID
- `POST /api/participantes` - Criar participante
- `PUT /api/participantes/{id}` - Atualizar participante
- `DELETE /api/participantes/{id}` - Excluir participante

#### Inscrições
- `GET /api/inscricoes` - Listar inscrições
- `POST /api/inscricoes` - Criar inscrição
- `PUT /api/inscricoes/{id}/confirmar` - Confirmar inscrição
- `PUT /api/inscricoes/{id}/cancelar` - Cancelar inscrição
- `GET /api/inscricoes/evento/{eventoId}` - Inscrições por evento

#### Categorias
- `GET /api/categorias` - Listar categorias
- `POST /api/categorias` - Criar categoria
- `PUT /api/categorias/{id}` - Atualizar categoria
- `DELETE /api/categorias/{id}` - Excluir categoria

#### Locais
- `GET /api/locais` - Listar locais
- `POST /api/locais` - Criar local
- `PUT /api/locais/{id}` - Atualizar local
- `DELETE /api/locais/{id}` - Excluir local

### Exemplos de Uso

#### Criar um Evento
```bash
curl -X POST http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Conferência de Tecnologia 2024",
    "descricao": "Evento sobre as últimas tendências em tecnologia",
    "dataInicio": "2024-08-15",
    "dataFim": "2024-08-16",
    "horaInicio": "09:00",
    "horaFim": "18:00",
    "capacidadeMaxima": 200,
    "preco": 150.00,
    "localId": 1,
    "categoriaId": 1
  }'
```

#### Listar Eventos com Paginação
```bash
curl "http://localhost:8080/api/eventos?page=0&size=10&sort=dataInicio,asc"
```

## 🧪 Testes

O projeto inclui uma suíte abrangente de testes:

### Executar Todos os Testes
```bash
mvn test
```

### Executar Testes Específicos
```bash
# Testes de uma classe específica
mvn test -Dtest=EventoServiceTest

# Testes de um método específico
mvn test -Dtest=EventoServiceTest#criarEvento_DeveRetornarEventoCriado
```

### Relatório de Cobertura
```bash
mvn jacoco:report
```

### Tipos de Testes Implementados

1. **Testes Unitários**: Testam componentes isoladamente
2. **Testes de Integração**: Testam a integração entre camadas
3. **Testes de Controller**: Testam endpoints REST
4. **Testes de Repositório**: Testam acesso a dados

## 🔧 Configuração

### Variáveis de Ambiente

Para produção, configure as seguintes variáveis de ambiente:

```bash
# Banco de Dados
export DB_URL=jdbc:postgresql://localhost:5432/eventos_db
export DB_USERNAME=eventos_user
export DB_PASSWORD=sua_senha_segura

# JWT
export JWT_SECRET=sua_chave_jwt_super_secreta_com_pelo_menos_256_bits
export JWT_EXPIRATION=86400000

# Perfil da aplicação
export SPRING_PROFILES_ACTIVE=prod
```

### Configuração do PostgreSQL

Para usar PostgreSQL em produção:

1. Instale o PostgreSQL
2. Crie o banco de dados:
```sql
CREATE DATABASE eventos_db;
CREATE USER eventos_user WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE eventos_db TO eventos_user;
```

3. Configure as variáveis de ambiente ou o `application-prod.properties`

## 🚀 Deploy

### Deploy Local com Docker

```bash
# Construir imagem Docker
docker build -t eventos-api .

# Executar container
docker run -p 8080:8080 eventos-api
```

### Deploy em Produção

#### Heroku
```bash
# Login no Heroku
heroku login

# Criar aplicação
heroku create sua-app-eventos

# Configurar variáveis de ambiente
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set JWT_SECRET=sua_chave_jwt

# Deploy
git push heroku main
```

#### AWS EC2
1. Configure uma instância EC2
2. Instale Java 17 e PostgreSQL
3. Configure as variáveis de ambiente
4. Execute o JAR da aplicação

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Padrões de Código

- Siga as convenções do Java
- Use nomes descritivos para variáveis e métodos
- Adicione testes para novas funcionalidades
- Mantenha a documentação atualizada

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- LinkedIn: [Seu Perfil](https://linkedin.com/in/seu-perfil)
- Email: seu.email@exemplo.com

## 🙏 Agradecimentos

- Spring Boot Team pela excelente framework
- Comunidade Java pelo suporte contínuo
- Contribuidores do projeto

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!

