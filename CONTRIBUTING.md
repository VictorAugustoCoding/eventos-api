# 🤝 Guia de Contribuição - API de Eventos

Obrigado por considerar contribuir para a API de Eventos! Este documento fornece diretrizes para contribuir com o projeto de forma efetiva e consistente.

## 📋 Índice

1. [Como Contribuir](#como-contribuir)
2. [Configuração do Ambiente](#configuração-do-ambiente)
3. [Padrões de Código](#padrões-de-código)
4. [Processo de Desenvolvimento](#processo-de-desenvolvimento)
5. [Testes](#testes)
6. [Documentação](#documentação)
7. [Pull Requests](#pull-requests)
8. [Reportar Issues](#reportar-issues)

## 🚀 Como Contribuir

Existem várias maneiras de contribuir com o projeto:

### 🐛 Reportar Bugs
- Verifique se o bug já foi reportado nas [Issues](https://github.com/seu-usuario/eventos-api/issues)
- Use o template de bug report
- Forneça informações detalhadas sobre o problema

### 💡 Sugerir Melhorias
- Abra uma issue com o label "enhancement"
- Descreva claramente a melhoria proposta
- Explique o valor que ela agregaria ao projeto

### 📝 Melhorar Documentação
- Corrija erros de digitação
- Adicione exemplos mais claros
- Traduza documentação para outros idiomas

### 🔧 Contribuir com Código
- Implemente novas funcionalidades
- Corrija bugs existentes
- Melhore a performance
- Adicione testes

## 🛠️ Configuração do Ambiente

### 1. Fork e Clone

```bash
# Fork o repositório no GitHub
# Clone seu fork
git clone https://github.com/seu-usuario/eventos-api.git
cd eventos-api

# Adicione o repositório original como upstream
git remote add upstream https://github.com/original-user/eventos-api.git
```

### 2. Configuração Local

```bash
# Instalar dependências
mvn clean install

# Executar testes
mvn test

# Executar aplicação
mvn spring-boot:run
```

### 3. Configuração do IDE

#### IntelliJ IDEA
1. Importe o projeto como Maven project
2. Configure o SDK para Java 17
3. Instale os plugins:
   - Lombok
   - SonarLint
   - CheckStyle

#### VS Code
1. Instale as extensões:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - SonarLint

### 4. Configuração do Git

```bash
# Configurar informações do usuário
git config user.name "Seu Nome"
git config user.email "seu.email@exemplo.com"

# Configurar hooks (opcional)
cp .githooks/* .git/hooks/
chmod +x .git/hooks/*
```

## 📏 Padrões de Código

### Convenções Java

#### Nomenclatura
```java
// Classes: PascalCase
public class EventoService { }

// Métodos e variáveis: camelCase
public void criarEvento() { }
private String nomeEvento;

// Constantes: UPPER_SNAKE_CASE
private static final String STATUS_ATIVO = "ATIVO";

// Packages: lowercase
package com.portfolio.eventos.service;
```

#### Estrutura de Classes
```java
@Service
public class EventoService {
    
    // 1. Constantes
    private static final Logger logger = LoggerFactory.getLogger(EventoService.class);
    
    // 2. Campos privados
    private final EventoRepository eventoRepository;
    
    // 3. Construtor
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }
    
    // 4. Métodos públicos
    public EventoDTO criarEvento(EventoDTO eventoDTO) {
        // implementação
    }
    
    // 5. Métodos privados
    private void validarEvento(EventoDTO eventoDTO) {
        // implementação
    }
}
```

### Formatação

#### Indentação
- Use 4 espaços (não tabs)
- Máximo 120 caracteres por linha

#### Imports
```java
// 1. Imports do Java
import java.time.LocalDate;
import java.util.List;

// 2. Imports de terceiros
import org.springframework.stereotype.Service;

// 3. Imports do projeto
import com.portfolio.eventos.entity.Evento;
```

### Comentários

#### JavaDoc
```java
/**
 * Cria um novo evento no sistema.
 * 
 * @param eventoDTO dados do evento a ser criado
 * @return evento criado com ID gerado
 * @throws ValidationException se os dados forem inválidos
 * @throws ResourceNotFoundException se categoria ou local não existirem
 */
public EventoDTO criarEvento(EventoDTO eventoDTO) {
    // implementação
}
```

#### Comentários Inline
```java
// TODO: Implementar cache para melhorar performance
// FIXME: Corrigir validação de data
// NOTE: Este método será depreciado na v2.0
```

### Tratamento de Exceções

```java
// ✅ Correto
try {
    return eventoRepository.save(evento);
} catch (DataIntegrityViolationException e) {
    logger.error("Erro ao salvar evento: {}", e.getMessage());
    throw new ValidationException("Dados do evento são inválidos");
}

// ❌ Incorreto
try {
    return eventoRepository.save(evento);
} catch (Exception e) {
    // Captura muito genérica
}
```

## 🔄 Processo de Desenvolvimento

### 1. Workflow Git

```bash
# 1. Sincronizar com upstream
git checkout main
git pull upstream main

# 2. Criar branch para feature
git checkout -b feature/nova-funcionalidade

# 3. Fazer commits
git add .
git commit -m "feat: adicionar endpoint de busca avançada"

# 4. Push para seu fork
git push origin feature/nova-funcionalidade

# 5. Abrir Pull Request
```

### 2. Convenção de Commits

Use [Conventional Commits](https://www.conventionalcommits.org/):

```bash
# Tipos de commit
feat: nova funcionalidade
fix: correção de bug
docs: alteração na documentação
style: formatação, sem mudança de lógica
refactor: refatoração de código
test: adição ou correção de testes
chore: tarefas de manutenção

# Exemplos
git commit -m "feat: adicionar endpoint de filtro por data"
git commit -m "fix: corrigir validação de email duplicado"
git commit -m "docs: atualizar README com novos endpoints"
git commit -m "test: adicionar testes para EventoService"
```

### 3. Branches

#### Nomenclatura
- `feature/nome-da-funcionalidade`
- `fix/nome-do-bug`
- `docs/nome-da-documentacao`
- `refactor/nome-da-refatoracao`

#### Estratégia
- `main`: código estável em produção
- `develop`: código em desenvolvimento
- `feature/*`: novas funcionalidades
- `hotfix/*`: correções urgentes

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/
├── com/portfolio/eventos/
│   ├── controller/     # Testes de controller
│   ├── service/        # Testes de service
│   ├── repository/     # Testes de repository
│   └── integration/    # Testes de integração
```

### Padrões de Teste

#### Nomenclatura
```java
// Padrão: metodo_cenario_resultadoEsperado
@Test
void criarEvento_ComDadosValidos_DeveRetornarEventoCriado() {
    // teste
}

@Test
void criarEvento_ComEmailDuplicado_DeveLancarExcecao() {
    // teste
}
```

#### Estrutura AAA (Arrange, Act, Assert)
```java
@Test
void criarEvento_ComDadosValidos_DeveRetornarEventoCriado() {
    // Arrange
    EventoDTO eventoDTO = criarEventoDTO();
    when(eventoRepository.save(any())).thenReturn(evento);
    
    // Act
    EventoDTO resultado = eventoService.criarEvento(eventoDTO);
    
    // Assert
    assertThat(resultado).isNotNull();
    assertThat(resultado.getNome()).isEqualTo("Evento Teste");
}
```

### Cobertura de Testes

- **Mínimo**: 80% de cobertura
- **Ideal**: 90% de cobertura
- **Crítico**: 100% para lógica de negócio

```bash
# Executar testes com cobertura
mvn clean test jacoco:report

# Ver relatório
open target/site/jacoco/index.html
```

### Testes de Integração

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class EventoIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("eventos_test")
            .withUsername("test")
            .withPassword("test");
    
    @Test
    void devePermitirFluxoCompletoDeEvento() {
        // teste de integração completo
    }
}
```

## 📚 Documentação

### README
- Mantenha atualizado
- Inclua exemplos práticos
- Documente mudanças importantes

### Código
- Use JavaDoc para métodos públicos
- Comente lógica complexa
- Mantenha comentários atualizados

### API
- Atualize Swagger annotations
- Documente novos endpoints
- Inclua exemplos de request/response

### Changelog
```markdown
## [1.2.0] - 2024-07-25

### Added
- Endpoint de busca avançada de eventos
- Filtro por múltiplas categorias
- Paginação melhorada

### Changed
- Otimização de queries do banco
- Melhoria na validação de dados

### Fixed
- Correção no cálculo de vagas disponíveis
- Fix na ordenação por data
```

## 🔍 Pull Requests

### Checklist

Antes de abrir um PR, verifique:

- [ ] Código segue os padrões estabelecidos
- [ ] Testes foram adicionados/atualizados
- [ ] Todos os testes estão passando
- [ ] Documentação foi atualizada
- [ ] Commit messages seguem convenção
- [ ] Branch está atualizada com main
- [ ] Não há conflitos de merge

### Template de PR

```markdown
## Descrição
Breve descrição das mudanças realizadas.

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação

## Como Testar
1. Passos para testar as mudanças
2. Comandos específicos
3. Cenários de teste

## Screenshots (se aplicável)
Adicione screenshots das mudanças visuais.

## Checklist
- [ ] Código revisado
- [ ] Testes adicionados
- [ ] Documentação atualizada
- [ ] Sem breaking changes
```

### Processo de Review

1. **Automated Checks**: CI/CD deve passar
2. **Code Review**: Pelo menos 1 aprovação
3. **Testing**: Testes manuais se necessário
4. **Merge**: Squash and merge preferido

## 🐛 Reportar Issues

### Template de Bug

```markdown
## Descrição do Bug
Descrição clara e concisa do bug.

## Passos para Reproduzir
1. Vá para '...'
2. Clique em '....'
3. Role para baixo até '....'
4. Veja o erro

## Comportamento Esperado
Descrição do que deveria acontecer.

## Comportamento Atual
Descrição do que está acontecendo.

## Screenshots
Se aplicável, adicione screenshots.

## Ambiente
- OS: [ex: Windows 10]
- Java Version: [ex: 17]
- Spring Boot Version: [ex: 3.2.0]

## Informações Adicionais
Qualquer outra informação relevante.
```

### Template de Feature Request

```markdown
## Resumo da Funcionalidade
Descrição clara da funcionalidade desejada.

## Motivação
Por que esta funcionalidade é necessária?

## Solução Proposta
Como você imagina que isso deveria funcionar?

## Alternativas Consideradas
Outras soluções que você considerou?

## Informações Adicionais
Contexto adicional ou screenshots.
```

## 🏷️ Labels

### Issues
- `bug`: Algo não está funcionando
- `enhancement`: Nova funcionalidade
- `documentation`: Melhorias na documentação
- `good first issue`: Bom para iniciantes
- `help wanted`: Ajuda extra é bem-vinda
- `question`: Pergunta ou discussão

### Pull Requests
- `work in progress`: PR em desenvolvimento
- `ready for review`: Pronto para revisão
- `needs changes`: Precisa de alterações
- `approved`: Aprovado para merge

## 🎯 Metas de Qualidade

### Métricas
- **Cobertura de Testes**: > 85%
- **Complexidade Ciclomática**: < 10
- **Duplicação de Código**: < 3%
- **Vulnerabilidades**: 0 críticas

### Ferramentas
- **SonarQube**: Análise de qualidade
- **SpotBugs**: Detecção de bugs
- **Checkstyle**: Padrões de código
- **PMD**: Análise estática

## 🤝 Código de Conduta

### Nossos Padrões

Exemplos de comportamento que contribuem para criar um ambiente positivo:

- Usar linguagem acolhedora e inclusiva
- Respeitar diferentes pontos de vista
- Aceitar críticas construtivas
- Focar no que é melhor para a comunidade
- Mostrar empatia com outros membros

### Comportamentos Inaceitáveis

- Uso de linguagem ou imagens sexualizadas
- Trolling, comentários insultuosos
- Assédio público ou privado
- Publicar informações privadas de terceiros
- Outras condutas consideradas inapropriadas

## 📞 Contato

Para dúvidas sobre contribuição:

- **Issues**: Use as issues do GitHub
- **Discussões**: GitHub Discussions
- **Email**: contribuicoes@eventos-api.com
- **Discord**: [Link do servidor]

---

Obrigado por contribuir! Sua participação torna este projeto melhor para todos. 🚀

