# Music Library API

API REST em Java/Spring Boot que disponibiliza dados sobre artistas e álbuns, desenvolvida conforme Projeto Prático SEPLAG – Desenvolvedor Back End Java Sênior (ANEXO II-A).

---

## Dados do Projeto

- **Vaga:** Desenvolvedor Back End Java Sênior
- **Instituição:** SEPLAG/MT (Secretaria de Estado de Planejamento e Gestão)
- **Projeto:** Implementação Back End – API REST de Biblioteca Musical

---

## Tecnologias

- Java 21, Spring Boot 3.5
- Spring Security, JWT (JJWT)
- Spring Data JPA, PostgreSQL
- Flyway, MinIO, Bucket4j
- SpringDoc OpenAPI
- Docker, Maven

---
## Estrutura de Dados e Decisões Arquiteturais

### Modelo de Dados

| Tabela | Campos | Descrição |
|--------|--------|-----------|
| **ARTISTA** | ID_ARTISTA, NOME_ARTISTA, TIPO_ARTISTA | Artistas (cantor ou banda) |
| **ALBUM** | ID_ALBUM, TITULO_ALBUM, KEY_OBJECT_MINIO | Álbuns musicais |
| **ARTISTA_ALBUM** | ID_ARTISTA, ID_ALBUM | Relacionamento N:N |
| **album_images** | id, album_id, object_key, created_at | Imagens de capa no MinIO |
| **USUARIO** | ID_USUARIO, LOGIN_USUARIO, PASSWORD_USUARIO, KEY_USUARIO | Autenticação |
| **TOKENS** | ID_TOKENS, ID_USUARIO, NOME_TOKEN, THEN_TOKEN | Refresh tokens |

**TIPO_ARTISTA:** enum `CANTOR` ou `BANDA`, conforme exemplos do edital.

### Carga Inicial (exemplos do edital)

- **Serj Tankian** (CANTOR): Harakiri, Black Blooms, The Rough Dog
- **Mike Shinoda** (CANTOR): The Rising Tied, Post Traumatic, Post Traumatic EP, Where'd You Go
- **Michel Teló** (CANTOR): Bem Sertanejo, Bem Sertanejo - O Show (Ao Vivo), Bem Sertanejo - (1ª Temporada) - EP
- **Guns N' Roses** (BANDA): Use Your Illusion I, Use Your Illusion II, Greatest Hits

### Arquitetura

- **Camadas:** Controller → Service → Repository (JPA)
- **Segurança:** JWT stateless, CORS configurável
- **Versionamento:** Endpoints sob `/v1/`
- **Armazenamento:** PostgreSQL (dados), MinIO S3 (imagens)

---

## Requisitos Implementados

| Requisito | Implementação |
|-----------|---------------|
| **a) Segurança CORS** | `CorsConfig` – origens configuráveis via `app.cors.allowed-origins` |
| **b) JWT + refresh** | Token 5 min, refresh 7 dias, endpoints `/auth/login` e `/auth/refresh` |
| **c) POST, PUT, GET** | Álbuns e artistas com CRUD completo |
| **d) Paginação** | `Pageable` nos álbuns (`?page=0&size=10`) |
| **e) Cantores/bandas** | Filtro `tipoArtista` em artistas; álbuns exibem tipo dos artistas associados |
| **f) Nome + ordenação** | Artistas: `?nome=...&sort=nome,asc` ou `sort=nome,desc` |
| **g) Upload de capas** | `POST /v1/albuns/{id}/capas` (multipart) |
| **h) MinIO (S3)** | Bucket `album-covers`, armazenamento de imagens |
| **i) URLs pré-assinadas** | `GET /v1/albuns/{id}/capas` – expiração 30 min |
| **j) Versionamento** | Base `/v1/` em todos os endpoints |
| **k) Flyway** | Migrações em `src/main/resources/db/migration/` |
| **l) OpenAPI/Swagger** | `/swagger-ui.html` e `/api-docs` |

### Requisitos Sênior

| Requisito | Implementação |
|-----------|---------------|
| **Health Checks** | Actuator: `/actuator/health`, Liveness e Readiness |
| **Testes unitários** | Teste de contexto (`MusicLibraryApiApplicationTests`) |
| **WebSocket** | Não implementado |
| **Rate limit** | 10 requisições/minuto por usuário (Bucket4j) |
| **Regionais** | Não implementado |

---

## Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL (ou Docker)
- MinIO (ou Docker)

---

## Como Executar

### Opção 1: Docker Compose (recomendado)

```bash
docker compose up -d --build
```

Containers: API (8080), PostgreSQL (5432), MinIO (9000 API, 9001 Console).

### Opção 2: Local (Maven)

1. Subir PostgreSQL e MinIO (ou via `docker compose up db minio -d`).
2. Executar:

```bash
./mvnw spring-boot:run
```

Configuração padrão: `localhost:5432/music-library`, `localhost:9000` (MinIO).

### Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/music-library | URL do banco |
| SPRING_DATASOURCE_USERNAME | postgres | Usuário |
| SPRING_DATASOURCE_PASSWORD | admin | Senha |
| MINIO_URL | http://localhost:9000 | Endpoint MinIO |
| MINIO_ACCESS_KEY | root | Access key |
| MINIO_SECRET_KEY | rootpass123 | Secret key |
| MINIO_BUCKET | album-covers | Bucket |
| JWT_SECRET | change-me-... | Chave JWT (32+ chars em produção) |
| CORS_ALLOWED_ORIGINS | http://localhost:8080,3000,5173 | Origens permitidas |

---

## Como Testar

### Executar testes

```bash
./mvnw test
```

### Fluxo manual

1. **Registrar usuário** (ou usar admin/admin já existente)

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login":"novousuario","senha":"senha123"}'
```

Resposta: `accessToken` e `refreshToken` (201 Created).

2. **Login**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","senha":"admin"}'
```

Resposta: `accessToken` e `refreshToken`.

3. **Listar álbuns (autenticado)**

```bash
curl -X GET "http://localhost:8080/v1/albuns?page=0&size=10&tipoArtista=CANTOR" \
  -H "Authorization: Bearer <accessToken>"
```

4. **Documentação interativa**

- Swagger UI: http://localhost:8080/swagger-ui.html  
- OpenAPI JSON: http://localhost:8080/api-docs

### Usuário padrão

- **Login:** `admin`
- **Senha:** `admin` (hash BCrypt na migration)

---

## Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /auth/register | Registrar novo usuário (retorna JWT) |
| POST | /auth/login | Login (retorna JWT) |
| POST | /auth/refresh | Renovar token |
| GET | /v1/artistas | Listar artistas (filtros: nome, tipoArtista; sort) |
| GET | /v1/artistas/{id} | Buscar artista por ID |
| POST | /v1/artistas | Criar artista |
| PUT | /v1/artistas/{id} | Atualizar artista |
| DELETE | /v1/artistas/{id} | Excluir artista |
| GET | /v1/albuns | Listar álbuns (paginação; filtros: nomeArtista, tipoArtista) |
| GET | /v1/albuns/{id} | Buscar álbum por ID |
| POST | /v1/albuns | Criar álbum |
| PUT | /v1/albuns/{id} | Atualizar álbum |
| DELETE | /v1/albuns/{id} | Excluir álbum |
| POST | /v1/albuns/{id}/capas | Upload de capas (multipart) |
| GET | /v1/albuns/{id}/capas | URLs pré-assinadas (30 min) |
| GET | /actuator/health | Health check |
| GET | /actuator/health/liveness | Liveness |
| GET | /actuator/health/readiness | Readiness |

---

## O Que Não Foi Implementado e Motivo

| Item | Motivo |
|------|--------|
| **WebSocket** | Priorização em requisitos core (JWT, MinIO, paginação, rate limit). WebSocket demanda integração front-end e ambiente de testes adequado. |
| **Endpoint de Regionais** | Foco nas funcionalidades principais de artistas/álbuns. Sincronização com API externa seria um módulo adicional com agendamento e políticas de retry. |
| **Filtro tipoArtista nos álbuns (backend)** | Artistas têm filtro por `tipoArtista`. Nos álbuns o filtro está parcialmente implementado; o tipo é exposto via artistas associados para consulta parametrizada. |

---

## Estrutura do Projeto

```
src/main/java/br/gov/mt/seplag/music_library_api/
├── config/         # CORS, MinIO, OpenAPI, Rate Limit, Security
├── controller/     # AlbumController, ArtistaController
├── dto/            # Request/Response DTOs
├── entity/         # Entidades JPA
├── repository/     # Repositories JPA
├── security/       # AuthController, JWT, RefreshToken
└── service/        # AlbumService, ArtistaService, MinioStorageService

src/main/resources/
├── application.yaml
└── db/migration/   # Flyway (V1..V6)
```

---

## Licença e Autoria

Projeto desenvolvido como parte do processo seletivo SEPLAG/MT. Código de autoria própria, com uso de tecnologias e bibliotecas open source conforme licenças respectivas.
