# 📖 Documentação Completa da API - Eventos

Esta documentação fornece informações detalhadas sobre todos os endpoints, modelos de dados, códigos de resposta e exemplos de uso da API de Gestão de Eventos.

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Autenticação](#autenticação)
3. [Códigos de Resposta](#códigos-de-resposta)
4. [Modelos de Dados](#modelos-de-dados)
5. [Endpoints](#endpoints)
6. [Exemplos de Uso](#exemplos-de-uso)
7. [Filtros e Paginação](#filtros-e-paginação)
8. [Tratamento de Erros](#tratamento-de-erros)

## 🌐 Visão Geral

### URL Base
```
http://localhost:8080/api
```

### Formato de Dados
- **Request**: JSON
- **Response**: JSON
- **Charset**: UTF-8

### Headers Obrigatórios
```http
Content-Type: application/json
Accept: application/json
```

### Versionamento
A API utiliza versionamento através da URL. A versão atual é `v1` (implícita).

## 🔐 Autenticação

### JWT (JSON Web Token)

A API utiliza JWT para autenticação. Após o login, inclua o token no header:

```http
Authorization: Bearer {seu_jwt_token}
```

### Endpoints Públicos
Os seguintes endpoints não requerem autenticação:
- `GET /api/eventos`
- `GET /api/eventos/{id}`
- `GET /api/categorias`
- `GET /api/locais`
- Documentação Swagger

## 📊 Códigos de Resposta

| Código | Descrição | Uso |
|--------|-----------|-----|
| **200** | OK | Requisição bem-sucedida |
| **201** | Created | Recurso criado com sucesso |
| **204** | No Content | Operação bem-sucedida sem conteúdo |
| **400** | Bad Request | Dados inválidos na requisição |
| **401** | Unauthorized | Token de autenticação inválido |
| **403** | Forbidden | Acesso negado |
| **404** | Not Found | Recurso não encontrado |
| **409** | Conflict | Conflito de dados |
| **422** | Unprocessable Entity | Erro de validação |
| **500** | Internal Server Error | Erro interno do servidor |

## 📋 Modelos de Dados

### EventoDTO

```json
{
  "id": 1,
  "nome": "string",
  "descricao": "string",
  "dataInicio": "2024-08-15",
  "dataFim": "2024-08-16",
  "horaInicio": "09:00",
  "horaFim": "18:00",
  "capacidadeMaxima": 200,
  "preco": 150.00,
  "status": "EM_BREVE",
  "localId": 1,
  "categoriaId": 1,
  "dataCriacao": "2024-07-25T01:30:00",
  "dataAtualizacao": "2024-07-25T01:30:00",
  "vagasDisponiveis": 180,
  "numeroInscricoesConfirmadas": 20
}
```

#### Validações
- `nome`: Obrigatório, máximo 200 caracteres
- `dataInicio`: Obrigatória, formato ISO date
- `dataFim`: Obrigatória, deve ser posterior à data de início
- `capacidadeMaxima`: Mínimo 0
- `preco`: Mínimo 0.0
- `localId`: Obrigatório
- `categoriaId`: Obrigatório

#### Status do Evento
- `EM_BREVE`: Evento ainda não iniciado
- `EM_ANDAMENTO`: Evento em execução
- `FINALIZADO`: Evento concluído
- `CANCELADO`: Evento cancelado

### ParticipanteDTO

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-00",
  "dataNascimento": "1990-05-15",
  "role": "PARTICIPANTE",
  "dataCriacao": "2024-07-25T01:30:00",
  "dataAtualizacao": "2024-07-25T01:30:00"
}
```

#### Validações
- `nome`: Obrigatório, máximo 100 caracteres
- `email`: Obrigatório, formato válido, único
- `cpf`: Obrigatório, formato válido, único
- `telefone`: Formato válido

#### Roles
- `PARTICIPANTE`: Usuário comum
- `ORGANIZADOR`: Pode criar e gerenciar eventos
- `ADMIN`: Acesso total ao sistema

### InscricaoDTO

```json
{
  "id": 1,
  "participanteId": 1,
  "eventoId": 1,
  "dataInscricao": "2024-07-25T01:30:00",
  "status": "CONFIRMADA",
  "observacoes": "Necessidades especiais: cadeirante"
}
```

#### Status da Inscrição
- `PENDENTE`: Aguardando confirmação
- `CONFIRMADA`: Inscrição confirmada
- `CANCELADA`: Inscrição cancelada
- `LISTA_ESPERA`: Em lista de espera

### CategoriaDTO

```json
{
  "id": 1,
  "nome": "Tecnologia",
  "descricao": "Eventos relacionados à tecnologia e inovação",
  "cor": "#FF5722",
  "icone": "tech-icon"
}
```

### LocalDTO

```json
{
  "id": 1,
  "nome": "Centro de Convenções",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "capacidadeMaxima": 500,
  "descricao": "Moderno centro de convenções",
  "latitude": -23.5505,
  "longitude": -46.6333
}
```

## 🔗 Endpoints

### 📅 Eventos

#### Listar Eventos
```http
GET /api/eventos
```

**Parâmetros de Query:**
- `page` (int): Número da página (padrão: 0)
- `size` (int): Tamanho da página (padrão: 10)
- `sort` (string[]): Ordenação (ex: "nome,asc")

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Conferência Tech 2024",
      "dataInicio": "2024-08-15",
      "status": "EM_BREVE"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### Buscar Evento por ID
```http
GET /api/eventos/{id}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Conferência Tech 2024",
  "descricao": "Evento sobre tecnologia",
  "dataInicio": "2024-08-15",
  "dataFim": "2024-08-16",
  "capacidadeMaxima": 200,
  "vagasDisponiveis": 180
}
```

#### Criar Evento
```http
POST /api/eventos
```

**Body:**
```json
{
  "nome": "Novo Evento",
  "descricao": "Descrição do evento",
  "dataInicio": "2024-08-15",
  "dataFim": "2024-08-16",
  "horaInicio": "09:00",
  "horaFim": "18:00",
  "capacidadeMaxima": 100,
  "preco": 50.00,
  "localId": 1,
  "categoriaId": 1
}
```

#### Atualizar Evento
```http
PUT /api/eventos/{id}
```

#### Excluir Evento
```http
DELETE /api/eventos/{id}
```

#### Buscar Eventos Próximos
```http
GET /api/eventos/proximos?dias=7
```

#### Buscar Eventos Populares
```http
GET /api/eventos/populares?limit=5
```

#### Filtrar Eventos
```http
GET /api/eventos/filtrar
```

**Parâmetros de Query:**
- `categoriaId` (long): ID da categoria
- `localId` (long): ID do local
- `status` (string): Status do evento
- `dataInicio` (date): Data de início mínima
- `dataFim` (date): Data de fim máxima
- `precoMaximo` (decimal): Preço máximo

### 👥 Participantes

#### Listar Participantes
```http
GET /api/participantes
```

#### Buscar Participante por ID
```http
GET /api/participantes/{id}
```

#### Criar Participante
```http
POST /api/participantes
```

**Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-9999",
  "cpf": "123.456.789-00",
  "dataNascimento": "1990-05-15"
}
```

#### Atualizar Participante
```http
PUT /api/participantes/{id}
```

#### Excluir Participante
```http
DELETE /api/participantes/{id}
```

### 📝 Inscrições

#### Listar Inscrições
```http
GET /api/inscricoes
```

#### Buscar Inscrição por ID
```http
GET /api/inscricoes/{id}
```

#### Criar Inscrição
```http
POST /api/inscricoes
```

**Body:**
```json
{
  "participanteId": 1,
  "eventoId": 1,
  "observacoes": "Observações especiais"
}
```

#### Confirmar Inscrição
```http
PUT /api/inscricoes/{id}/confirmar
```

#### Cancelar Inscrição
```http
PUT /api/inscricoes/{id}/cancelar
```

#### Buscar Inscrições por Participante
```http
GET /api/inscricoes/participante/{participanteId}
```

#### Buscar Inscrições por Evento
```http
GET /api/inscricoes/evento/{eventoId}
```

### 🏷️ Categorias

#### Listar Categorias
```http
GET /api/categorias
```

#### Buscar Categoria por ID
```http
GET /api/categorias/{id}
```

#### Criar Categoria
```http
POST /api/categorias
```

**Body:**
```json
{
  "nome": "Tecnologia",
  "descricao": "Eventos de tecnologia",
  "cor": "#FF5722",
  "icone": "tech-icon"
}
```

#### Atualizar Categoria
```http
PUT /api/categorias/{id}
```

#### Excluir Categoria
```http
DELETE /api/categorias/{id}
```

### 📍 Locais

#### Listar Locais
```http
GET /api/locais
```

#### Buscar Local por ID
```http
GET /api/locais/{id}
```

#### Criar Local
```http
POST /api/locais
```

**Body:**
```json
{
  "nome": "Centro de Convenções",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "capacidadeMaxima": 500,
  "descricao": "Moderno centro de convenções"
}
```

#### Atualizar Local
```http
PUT /api/locais/{id}
```

#### Excluir Local
```http
DELETE /api/locais/{id}
```

## 💡 Exemplos de Uso

### Cenário Completo: Criando um Evento

#### 1. Criar Categoria
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tecnologia",
    "descricao": "Eventos relacionados à tecnologia",
    "cor": "#2196F3"
  }'
```

#### 2. Criar Local
```bash
curl -X POST http://localhost:8080/api/locais \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Auditório Tech",
    "endereco": "Av. Paulista, 1000",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310-100",
    "capacidadeMaxima": 300
  }'
```

#### 3. Criar Evento
```bash
curl -X POST http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "DevConf 2024",
    "descricao": "Conferência de desenvolvimento de software",
    "dataInicio": "2024-09-15",
    "dataFim": "2024-09-16",
    "horaInicio": "08:00",
    "horaFim": "18:00",
    "capacidadeMaxima": 250,
    "preco": 199.90,
    "localId": 1,
    "categoriaId": 1
  }'
```

#### 4. Criar Participante
```bash
curl -X POST http://localhost:8080/api/participantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria.santos@email.com",
    "telefone": "(11) 98765-4321",
    "cpf": "987.654.321-00",
    "dataNascimento": "1985-03-20"
  }'
```

#### 5. Fazer Inscrição
```bash
curl -X POST http://localhost:8080/api/inscricoes \
  -H "Content-Type: application/json" \
  -d '{
    "participanteId": 1,
    "eventoId": 1,
    "observacoes": "Primeira vez no evento"
  }'
```

### Busca Avançada de Eventos

```bash
# Buscar eventos de tecnologia em São Paulo com preço até R$ 200
curl "http://localhost:8080/api/eventos/filtrar?categoriaId=1&localId=1&precoMaximo=200.00&page=0&size=10"

# Buscar eventos dos próximos 30 dias
curl "http://localhost:8080/api/eventos/proximos?dias=30"

# Buscar os 10 eventos mais populares
curl "http://localhost:8080/api/eventos/populares?limit=10"
```

## 🔍 Filtros e Paginação

### Paginação Padrão

Todos os endpoints de listagem suportam paginação:

```http
GET /api/eventos?page=0&size=20&sort=nome,asc&sort=dataInicio,desc
```

**Parâmetros:**
- `page`: Número da página (base 0)
- `size`: Itens por página (máximo 100)
- `sort`: Campo e direção da ordenação

### Resposta Paginada

```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "empty": false
    }
  },
  "totalElements": 150,
  "totalPages": 8,
  "first": true,
  "last": false,
  "numberOfElements": 20
}
```

### Filtros Disponíveis

#### Eventos
- Data de início/fim
- Categoria
- Local
- Status
- Preço máximo
- Capacidade

#### Participantes
- Nome (busca parcial)
- Email
- Role
- Data de cadastro

#### Inscrições
- Status
- Data de inscrição
- Participante
- Evento

## ⚠️ Tratamento de Erros

### Formato de Erro Padrão

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Dados inválidos na requisição",
  "timestamp": "2024-07-25T01:30:00.000Z",
  "path": "/api/eventos",
  "details": [
    {
      "field": "nome",
      "message": "Nome é obrigatório"
    },
    {
      "field": "dataInicio",
      "message": "Data de início deve ser futura"
    }
  ]
}
```

### Erros de Validação

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Erro de validação",
  "timestamp": "2024-07-25T01:30:00.000Z",
  "path": "/api/eventos",
  "validationErrors": {
    "nome": "Nome deve ter entre 1 e 200 caracteres",
    "email": "Email deve ter formato válido",
    "capacidadeMaxima": "Capacidade deve ser maior que 0"
  }
}
```

### Códigos de Erro Específicos

| Código | Cenário | Solução |
|--------|---------|---------|
| `EVENTO_001` | Evento não encontrado | Verificar ID do evento |
| `EVENTO_002` | Capacidade esgotada | Aguardar vagas ou entrar na lista de espera |
| `EVENTO_003` | Data de inscrição expirada | Evento não aceita mais inscrições |
| `PART_001` | Email já cadastrado | Usar email diferente |
| `PART_002` | CPF já cadastrado | Verificar dados do participante |
| `INSC_001` | Participante já inscrito | Verificar inscrições existentes |

## 📱 SDKs e Bibliotecas

### JavaScript/TypeScript

```javascript
// Exemplo de cliente JavaScript
class EventosApiClient {
  constructor(baseUrl, token) {
    this.baseUrl = baseUrl;
    this.token = token;
  }

  async getEventos(page = 0, size = 10) {
    const response = await fetch(
      `${this.baseUrl}/api/eventos?page=${page}&size=${size}`,
      {
        headers: {
          'Authorization': `Bearer ${this.token}`,
          'Content-Type': 'application/json'
        }
      }
    );
    return response.json();
  }

  async criarEvento(evento) {
    const response = await fetch(`${this.baseUrl}/api/eventos`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(evento)
    });
    return response.json();
  }
}
```

### Python

```python
import requests

class EventosApiClient:
    def __init__(self, base_url, token):
        self.base_url = base_url
        self.token = token
        self.headers = {
            'Authorization': f'Bearer {token}',
            'Content-Type': 'application/json'
        }

    def get_eventos(self, page=0, size=10):
        url = f"{self.base_url}/api/eventos"
        params = {'page': page, 'size': size}
        response = requests.get(url, headers=self.headers, params=params)
        return response.json()

    def criar_evento(self, evento):
        url = f"{self.base_url}/api/eventos"
        response = requests.post(url, headers=self.headers, json=evento)
        return response.json()
```

## 🔄 Webhooks

### Configuração de Webhooks

A API suporta webhooks para notificar sobre eventos importantes:

```json
{
  "url": "https://seu-site.com/webhook",
  "events": [
    "evento.criado",
    "evento.atualizado",
    "inscricao.confirmada",
    "inscricao.cancelada"
  ],
  "secret": "sua_chave_secreta"
}
```

### Payload do Webhook

```json
{
  "event": "inscricao.confirmada",
  "timestamp": "2024-07-25T01:30:00.000Z",
  "data": {
    "inscricaoId": 123,
    "eventoId": 456,
    "participanteId": 789
  }
}
```

---

Esta documentação é mantida atualizada com cada versão da API. Para dúvidas ou sugestões, consulte o repositório do projeto ou entre em contato com a equipe de desenvolvimento.

