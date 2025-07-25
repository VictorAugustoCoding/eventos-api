# API de Gestão de Eventos - Projeto de Portfólio

## 🎯 Visão Geral do Projeto

Este projeto demonstra competências avançadas em desenvolvimento Java e Spring Boot através da criação de uma API REST completa para gestão de eventos e inscrições. A aplicação foi desenvolvida seguindo as melhores práticas da indústria e padrões de arquitetura modernos.

## 🏆 Competências Demonstradas

### Desenvolvimento Java Avançado
- **Java 17** com uso de features modernas
- **Programação Orientada a Objetos** com herança, polimorfismo e encapsulamento
- **Generics e Collections** para manipulação eficiente de dados
- **Stream API** para processamento funcional de dados
- **Optional** para tratamento seguro de valores nulos
- **Annotations** personalizadas e do framework

### Arquitetura e Design Patterns
- **Arquitetura em Camadas** (Controller → Service → Repository → Entity)
- **Repository Pattern** com Spring Data JPA
- **DTO Pattern** para separação de responsabilidades
- **Dependency Injection** com Spring IoC
- **Builder Pattern** nas entidades
- **Strategy Pattern** para validações
- **Observer Pattern** através de eventos Spring

### Spring Framework Ecosystem
- **Spring Boot 3.2** com auto-configuração
- **Spring Data JPA** com consultas customizadas
- **Spring Security** para autenticação e autorização
- **Spring Web** para APIs REST
- **Spring Validation** para validação de dados
- **Spring Test** para testes automatizados

### Persistência de Dados
- **JPA/Hibernate** como ORM principal
- **Relacionamentos complexos** (OneToMany, ManyToOne, ManyToMany)
- **Consultas JPQL** personalizadas
- **Transações** com @Transactional
- **Auditoria** automática de entidades
- **Migrations** com Flyway (configurado)

### Segurança
- **Spring Security** com configuração moderna
- **BCrypt** para hash de senhas
- **CORS** configurado adequadamente
- **Autorização baseada em roles**
- **Proteção contra CSRF**
- **Validação de entrada** em todos os endpoints

### Qualidade de Código
- **Clean Code** com nomes descritivos e métodos pequenos
- **SOLID Principles** aplicados consistentemente
- **Exception Handling** centralizado e estruturado
- **Logging** adequado para debugging e monitorização
- **Documentação** completa e atualizada
- **Testes** unitários e de integração

## 🔧 Funcionalidades Técnicas Avançadas

### Paginação e Filtros
```java
@GetMapping("/filtrar")
public ResponseEntity<Page<EventoDTO>> buscarEventosComFiltros(
    @RequestParam(required = false) Long categoriaId,
    @RequestParam(required = false) Evento.StatusEvento status,
    Pageable pageable) {
    // Implementação com Specification Pattern
}
```

### Validações Customizadas
```java
@Entity
public class Evento {
    @NotNull(message = "Data de início é obrigatória")
    @Future(message = "Data de início deve ser no futuro")
    private LocalDate dataInicio;
    
    // Validação customizada no nível de negócio
    public boolean temVagasDisponiveis() {
        return temCapacidadeIlimitada() || 
               getNumeroInscricoesConfirmadas() < getCapacidadeMaxima();
    }
}
```

### Consultas Complexas
```java
@Query("SELECT e FROM Evento e WHERE " +
       "(:categoriaId IS NULL OR e.categoria.id = :categoriaId) AND " +
       "(:status IS NULL OR e.status = :status) AND " +
       "e.dataInicio >= :dataInicio")
Page<Evento> findEventosComFiltros(
    @Param("categoriaId") Long categoriaId,
    @Param("status") StatusEvento status,
    @Param("dataInicio") LocalDate dataInicio,
    Pageable pageable);
```

### Tratamento Global de Exceções
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex) {
        // Resposta estruturada com detalhes do erro
    }
}
```

## 📊 Métricas do Projeto

- **Linhas de Código**: ~2.500 linhas
- **Classes**: 25+ classes bem estruturadas
- **Endpoints**: 30+ endpoints REST
- **Entidades**: 5 entidades com relacionamentos complexos
- **Testes**: Cobertura de testes unitários e integração
- **Documentação**: README completo + documentação Swagger

## 🚀 Diferenciais Competitivos

### 1. Arquitetura Escalável
- Separação clara de responsabilidades
- Baixo acoplamento entre camadas
- Fácil manutenção e extensão

### 2. Código Limpo e Legível
- Nomes descritivos e auto-documentados
- Métodos pequenos e focados
- Comentários apenas onde necessário

### 3. Tratamento Robusto de Erros
- Exceções customizadas para diferentes cenários
- Respostas de erro estruturadas e informativas
- Logs adequados para debugging

### 4. Validações Abrangentes
- Validação de entrada em todos os endpoints
- Regras de negócio implementadas corretamente
- Feedback claro para o utilizador

### 5. Segurança Implementada
- Autenticação e autorização funcionais
- Proteção contra vulnerabilidades comuns
- Configuração adequada para produção

## 🎓 Conceitos Avançados Aplicados

### Programação Funcional
```java
public List<EventoDTO> buscarEventosMaisPopulares(int limit) {
    return eventoRepository.findAll().stream()
        .sorted((e1, e2) -> Long.compare(
            e2.getNumeroInscricoesConfirmadas(), 
            e1.getNumeroInscricoesConfirmadas()))
        .limit(limit)
        .map(this::toDTOComTotais)
        .collect(Collectors.toList());
}
```

### Generics e Type Safety
```java
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByIdAndActiveTrue(ID id);
    List<T> findAllByActiveTrue();
}
```

### Enums com Comportamento
```java
public enum StatusEvento {
    EM_BREVE("Em Breve"),
    ATIVO("Ativo"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");
    
    public boolean podeReceberInscricoes() {
        return this == EM_BREVE || this == ATIVO;
    }
}
```

## 🔍 Casos de Uso Demonstrados

### 1. Gestão Completa de Eventos
- Criação com validações complexas
- Atualização com regras de negócio
- Filtros avançados para busca
- Controle de capacidade e vagas

### 2. Sistema de Inscrições
- Validação de disponibilidade
- Controle de estados da inscrição
- Prevenção de inscrições duplicadas
- Histórico completo por participante

### 3. Relatórios e Analytics
- Eventos mais populares
- Estatísticas por categoria
- Ocupação de locais
- Tendências temporais

## 💼 Valor para Empresas

Este projeto demonstra capacidade para:

1. **Desenvolver APIs robustas** que podem servir aplicações web e mobile
2. **Implementar arquiteturas escaláveis** que crescem com o negócio
3. **Seguir boas práticas** reconhecidas pela indústria
4. **Trabalhar com tecnologias modernas** do ecossistema Java
5. **Documentar adequadamente** facilitando manutenção por equipes
6. **Pensar em segurança** desde o início do desenvolvimento

## 🎯 Próximos Passos (Roadmap)

- [ ] Implementação de cache com Redis
- [ ] Integração com sistemas de pagamento
- [ ] Notificações em tempo real com WebSockets
- [ ] Métricas e monitorização com Micrometer
- [ ] Deploy automatizado com Docker e Kubernetes
- [ ] Testes de performance com JMeter

---

**Este projeto representa não apenas competência técnica, mas também capacidade de entregar soluções completas e bem arquitetadas que agregam valor real ao negócio.**

