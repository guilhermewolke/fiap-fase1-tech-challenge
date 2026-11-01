# Restaurantes e cardápios

API para cadastrar, consultar, atualizar e excluir restaurantes e itens do cardápio. Restaurantes são armazenados no MySQL; itens do cardápio, no MongoDB.

## Autenticação e permissões

Use **HTTP Basic** com o login e a senha de um usuário cadastrado. No Postman, configure **Authorization > Basic Auth**; no Swagger, use **Authorize**.

- Consultas de restaurantes e cardápios são públicas.
- Para cadastrar um restaurante, o usuário deve ter `userLevel=OWNER`. O usuário autenticado será o dono.
- Para alterar ou excluir um restaurante e gerenciar seus itens, o usuário deve ser `OWNER` e dono desse restaurante.
- Atualização, exclusão, troca de senha e associação de tipo de usuário exigem autenticação da própria conta. Na troca de senha, o ID e o login devem corresponder à conta autenticada.

Credenciais ausentes ou incorretas retornam `401`; falta de permissão retorna `403`.

## Regras principais

- O restaurante exige nome, endereço, horário de funcionamento e `categoryId` de uma categoria de cozinha existente. O dono é definido pelo servidor e preservado na atualização.
- O item exige nome, descrição, preço positivo, `dineInOnly` e caminho da foto. O restaurante vem de `restaurantId` na URL. `false` é válido para `dineInOnly`. O preço aceita até duas casas decimais significativas, e a atualização preserva o restaurante do item.
- Consulta, atualização e exclusão de item exigem que ele pertença ao restaurante da URL. Caso contrário, retornam `404`, mesmo que o usuário seja dono dos dois restaurantes.
- Nomes de restaurantes são únicos; nomes de itens são únicos dentro do mesmo restaurante. Maiúsculas, minúsculas e espaços nas pontas são desconsiderados.
- Para excluir um restaurante, remova seus itens primeiro. A tentativa de excluir um restaurante com itens retorna `409`.

## Endpoints

| Método | Rota | Operação |
| --- | --- | --- |
| POST | `/api/v1/restaurant` | Cadastrar restaurante |
| GET | `/api/v1/restaurant` | Listar restaurantes |
| GET | `/api/v1/restaurant/{id}` | Consultar restaurante |
| PUT | `/api/v1/restaurant/{id}` | Atualizar restaurante |
| DELETE | `/api/v1/restaurant/{id}` | Excluir restaurante |
| POST | `/api/v1/restaurant/{restaurantId}/menu` | Cadastrar item no restaurante |
| GET | `/api/v1/restaurant/{restaurantId}/menu` | Listar o cardápio do restaurante |
| GET | `/api/v1/restaurant/{restaurantId}/menu/{id}` | Consultar item do restaurante |
| PUT | `/api/v1/restaurant/{restaurantId}/menu/{id}` | Atualizar item do restaurante |
| DELETE | `/api/v1/restaurant/{restaurantId}/menu/{id}` | Excluir item do restaurante |

As listagens aceitam `page` a partir de `0` e `offset` de `1` a `100` (padrões: `0` e `10`). Para filtrar restaurantes por tipo de cozinha, informe `categoryId`, por exemplo: `/api/v1/restaurant?categoryId=1&page=0&offset=10`.

Cadastros retornam `201`; exclusões, `204`. Dados inválidos retornam `400`, registros inexistentes retornam `404` e conflitos retornam `409`.

Para cadastrar em `/api/v1/restaurant/1/menu`, envie:

```json
{
  "name": "Prato da casa",
  "description": "Arroz, feijão e carne",
  "price": 29.90,
  "dineInOnly": false,
  "photoPath": "/fotos/prato.png"
}
```

O corpo não recebe `restaurantId`; a resposta continua informando o restaurante. As rotas antigas em `/api/v1/menu-item` foram substituídas pelas rotas acima.

## Executar no Git Bash

Requisitos: JDK 21, Docker Desktop em execução e `local.env` preenchido. Execute os comandos na raiz do projeto e escolha uma das opções abaixo.

### Bancos no Docker e aplicação no IntelliJ

```bash
docker compose --env-file local.env up -d --wait db mongodb
docker compose --env-file local.env ps
```

Execute `TechChallengeApplication` no IntelliJ. Use MySQL em `localhost:3307` e MongoDB em `localhost:27017`.

### Todos os serviços no Docker

```bash
docker compose --env-file local.env up -d --build
docker compose --env-file local.env ps -a
```

Na URI do MongoDB, use o host `mongodb` no `local.env` do Compose e `localhost` na execução pelo IntelliJ.

Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

O grupo **Restaurantes** reúne os dois CRUDs. A documentação fica em `IRestaurantController` e `IMenuItemController`; as classes concretas mantêm o mapeamento HTTP e a chamada dos casos de uso.

## Testes automatizados

Com os bancos em execução e a aplicação parada no IntelliJ, prepare o MySQL de testes:

```bash
docker compose --env-file local.env exec -T db sh -c 'mysql --user=root --password="$MYSQL_ROOT_PASSWORD" < /docker-entrypoint-initdb.d/01-create-test-database.sql'
```

Execute a suíte completa:

```bash
(
  unset SPRING_DATASOURCE_URL SPRING_MONGODB_URI SPRING_DATA_MONGODB_URI
  ./mvnw "-Dspring.profiles.active=test" verify
)
```

Os testes usam e limpam os bancos `fiapdb_test` e `fiapfase2_test`. Confira as credenciais em `src/test/resources/application-test.properties`.

- Resultados: `target/surefire-reports`.
- Cobertura: `target/site/jacoco/index.html`.

## Postman

Importe as collections em `postman/Fase2/`: `restaurantes-cardapios.postman_collection.json` (fluxo principal) e `restaurantes-cardapios-cenario-erro.postman_collection.json` (erros). Defina `baseUrl` como `http://localhost:8080` e execute as requisições na ordem de cada collection.

Cada collection prepara seus próprios dados e remove os itens e restaurantes ao final. As contas e a categoria de demonstração permanecem no ambiente.
