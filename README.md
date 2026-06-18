# User Service

Microsserviço de gerenciamento de usuários do sistema de gestão de infraestrutura urbana (TCC). Responsável por autenticação, autorização e gamificação de cidadãos e agentes municipais.

## Tecnologias

- **Java 17** + **Spring Boot 4.0.5**
- **Spring Security** com autenticação via JWT (JJWT 0.11.5)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Pré-requisitos

- Java 17+
- PostgreSQL rodando localmente
- Maven (ou use o wrapper `mvnw` incluso)

## Configuração

Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE infra_urbana_usuarios;
```

Configure as propriedades em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/infra_urbana_usuarios
spring.datasource.username=<seu-usuario>
spring.datasource.password=<sua-senha>

api.security.token.secret=<chave-secreta-jwt>
api.security.token.expiration=86400000
```

O schema é criado automaticamente pelo Hibernate (`ddl-auto=update`).

## Executando

```bash
# Windows
mvnw spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

O serviço sobe na porta **8082**.

## Build

```bash
mvnw clean package
java -jar target/user-service-*.jar
```

## Testes

```bash
mvnw test
```

## API

### Autenticação — `POST /api/auth`

#### Registrar cidadão
```
POST /api/auth/registrar
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

Retorna:
```json
{ "id": 1, "nome": "João Silva", "email": "joao@email.com", "perfil": "ROLE_CIDADAO" }
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

Retorna:
```json
{ "token": "<jwt>" }
```

---

### Usuários — `GET|POST /api/usuarios`

> Todos os endpoints abaixo exigem o header:
> `Authorization: Bearer <token>`

#### Perfil do usuário autenticado
```
GET /api/usuarios/meu-perfil
```

Retorna:
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "perfil": "ROLE_CIDADAO",
  "pontosReputacao": 120,
  "historico": [
    { "pontos": 50, "descricao": "Denúncia registrada", "dataEvento": "2026-06-17T10:00:00" }
  ]
}
```

#### Adicionar pontos de reputação
```
POST /api/usuarios/pontuar
Content-Type: application/json

{
  "usuarioId": 1,
  "pontos": 50,
  "descricao": "Denúncia registrada"
}
```

#### Registrar agente municipal
> Requer perfil `ROLE_AGENTE_PREFEITURA`

```
POST /api/usuarios/registrar-agente
Content-Type: application/json

{
  "nome": "Maria Agente",
  "email": "maria@prefeitura.gov",
  "senha": "senha123"
}
```

---

### Regras de autorização

| Endpoint | Autenticação | Perfil exigido |
|---|---|---|
| `POST /api/auth/**` | Não | — |
| `GET /api/usuarios/meu-perfil` | Sim | Qualquer |
| `POST /api/usuarios/pontuar` | Sim | Qualquer |
| `POST /api/usuarios/registrar-agente` | Sim | `ROLE_AGENTE_PREFEITURA` |

## Modelo de dados

### `usuarios`

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | BIGINT PK | Identificador |
| `nome` | VARCHAR | Nome completo |
| `email` | VARCHAR UNIQUE | E-mail de acesso |
| `senha` | VARCHAR | Hash BCrypt |
| `perfil` | ENUM | `ROLE_CIDADAO` ou `ROLE_AGENTE_PREFEITURA` |
| `pontos_reputacao` | INTEGER | Pontuação acumulada (padrão 0) |
| `data_cadastro` | TIMESTAMP | Data de criação |

### `historico_gamificacao`

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | BIGINT PK | Identificador |
| `usuario_id` | BIGINT FK | Referência ao usuário |
| `pontos_alterados` | INTEGER | Pontos adicionados/removidos |
| `descricao_evento` | VARCHAR | Descrição do evento |
| `data_evento` | TIMESTAMP | Data do evento |

## Estrutura do projeto

```
src/main/java/com/usuario/
├── config/         # Configuração do Spring Security e filtro JWT
├── controller/     # Endpoints REST (AuthController, UsuarioController)
├── domain/         # Enums (Perfil)
├── dto/            # Objetos de transferência de dados
├── entity/         # Entidades JPA (Usuario, HistoricoGamificacao)
├── repository/     # Repositórios Spring Data JPA
└── service/        # Lógica de negócio (UsuarioService, JwtService)
```