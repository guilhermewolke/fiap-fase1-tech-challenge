# Tech Challenge - FIAP (Fase 2)

API REST de usuários, restaurantes e itens do cardápio, feita em **Java 21 + Spring Boot** com **MySQL e MongoDB**,
organizada em Clean Architecture (`domain`, `application`, `infrastructure`).

Funcionalidades principais (`/api/v1/user`):

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/api/v1/user/` | Cria um usuário |
| GET | `/api/v1/user/` | Lista usuários (paginado) |
| GET | `/api/v1/user/{id}` | Busca usuário por ID |
| GET | `/api/v1/user/find-by-name` | Busca usuários por nome |
| PUT | `/api/v1/user/{id}` | Atualiza um usuário |
| PATCH | `/api/v1/user/change-password/{id}` | Troca a senha |
| DELETE | `/api/v1/user/{id}` | Remove um usuário |
| POST | `/api/v1/user/auth-login` | Valida login e senha |

Os endpoints de restaurantes (`/api/v1/restaurant`) e itens do cardápio (`/api/v1/restaurant/{restaurantId}/menu`),
as regras de proprietário, os exemplos e os testes estão em [Restaurantes e cardápios](docs/RESTAURANTES_CARDAPIOS.md).

As consultas são públicas. As escritas de restaurantes e cardápios usam HTTP Basic e exigem um usuário OWNER.
As alterações de conta, exclusão, troca de senha e associação de tipo exigem autenticação da própria conta.

## Pré-requisitos

- Docker e Docker Compose
- (opcional, para rodar sem Docker) JDK 21 — o Maven vem junto via `./mvnw`

## Configuração do ambiente

Assim que baixar o repositório, crie o arquivo `local.env` a partir do arquivo de exemplo
`local.env.sample` — basta copiar/renomear:

```bash
cp local.env.sample local.env
```

Depois preencha as informações com as credenciais necessárias:

| Variável | Descrição |
| --- | --- |
| `MYSQL_ROOT_PASSWORD` | Senha do usuário **root** do banco |
| `MYSQL_DATABASE` | Nome do banco de dados |
| `SPRING_DATASOURCE_USERNAME` | Usuário da aplicação no banco |
| `SPRING_DATASOURCE_PASSWORD` | Senha desse usuário |
| `MONGO_ROOT_USERNAME` | Usuário administrador do MongoDB |
| `MONGO_ROOT_PASSWORD` | Senha desse usuário |
| `SPRING_DATA_MONGODB_URI` | URI da aplicação no Docker, usando o host `mongodb` e `authSource=admin` |

> O `local.env` não é versionado (está no `.gitignore`), pois contém credenciais.

## Subindo a aplicação

```bash
docker compose --env-file local.env up --build
```

Isso sobe três containers:

- `mysql-db` — MySQL 8.4 na porta `3307`
- `mongodb-fiap` — MongoDB 8 na porta `27017`
- `spring-app` — a API na porta `8080` (só inicia depois que o banco está saudável)

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`).

Para parar:

```bash
docker compose --env-file local.env down
```

Os dados são mantidos nos volumes dos bancos quando os containers são removidos sem excluir os volumes.

## Acessando

- API: http://localhost:8080/api/v1/user
- Swagger UI: http://localhost:8080/swagger-ui.html

## Rodando localmente sem Docker

Suba apenas o banco e rode a aplicação pelo Maven Wrapper:

```bash
docker compose --env-file local.env up -d --wait db mongodb
./mvnw spring-boot:run
```

> Nesse caso, a configuração padrão de `application.properties` usa MySQL em `localhost:3307`
> e MongoDB em `localhost:27017`. Use as credenciais do ambiente local.

## Testes

```bash
(
  unset SPRING_DATASOURCE_URL SPRING_MONGODB_URI SPRING_DATA_MONGODB_URI
  ./mvnw "-Dspring.profiles.active=test" verify
)
```

## Postman

A pasta `postman/` contém as collections e o environment para testar os endpoints.
Em `postman/Fase2/`, `restaurantes-cardapios.postman_collection.json` valida o fluxo principal
e `restaurantes-cardapios-cenario-erro.postman_collection.json` reúne os casos de erro.
Cada collection cria seus próprios dados de demonstração.

