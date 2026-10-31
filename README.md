# Sistema de Chamados do IFRN

API REST desenvolvida em Java com Spring Boot para gerenciar chamados de suporte técnico do Instituto Federal do Rio Grande do Norte (IFRN). O sistema permite registrar problemas, acompanhar o andamento dos atendimentos e consultar chamados por diferentes critérios.

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA e Hibernate**
- **PostgreSQL 16**
- **Flyway** para versionamento das migrações do banco de dados
- **Maven** para gerenciamento de dependências e construção do projeto
- **Docker e Docker Compose** para execução da aplicação e do banco de dados
- **Insomnia** para testes dos endpoints da API

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de setores.
- Cadastro, consulta, atualização e exclusão de categorias.
- Cadastro e consulta de usuários.
- Consulta de categorias por setor.
- Abertura e consulta de chamados.
- Filtros de chamados por status, prioridade, setor, categoria, solicitante, técnico e período.
- Atribuição de chamados a técnicos.
- Resolução e cancelamento de chamados.
- Registro e consulta de interações nos chamados.
- Paginação dos resultados das consultas.

## Regras de negócio

- Um chamado é criado com status `ABERTO` e prioridade `MEDIA`.
- Os chamados podem passar pelos estados `ABERTO`, `EM_ATENDIMENTO`, `RESOLVIDO` e `CANCELADO`, respeitando as transições permitidas pelo sistema.
- O técnico responsável deve pertencer ao setor relacionado à categoria do chamado.
- O cancelamento deve respeitar as condições definidas para o estado do chamado.
- As interações devem respeitar as regras de participação e o estado do chamado.
- Os setores e as categorias não podem ser excluídos quando houver vínculos que impeçam a operação.

## Arquitetura

O projeto utiliza uma arquitetura baseada no padrão **MVC (Model-View-Controller)**, organizada em camadas para separar as responsabilidades e facilitar a manutenção e a evolução da aplicação. Como se trata de uma API REST, a camada de apresentação é composta por controllers que recebem requisições HTTP e retornam respostas, geralmente em JSON.

As principais responsabilidades são:

- **Controller:** recebe as requisições HTTP, encaminha as operações para os serviços e retorna as respostas da API.
- **Service:** concentra as regras de negócio e coordena as operações da aplicação.
- **Repository:** realiza o acesso e a persistência dos dados utilizando o Spring Data JPA.
- **Models:** contém os modelos que representam os dados e as entidades persistidas no banco de dados.
- **DTO (Data Transfer Object):** define os objetos utilizados para transportar dados nas requisições e respostas da API.
- **Exception:** reúne o tratamento de exceções e erros da aplicação.

Essa organização promove a separação de responsabilidades, reduz o acoplamento entre as camadas e facilita os testes e a manutenção do sistema.



## Modelo de dados

O banco de dados utiliza as seguintes tabelas principais:

- `TB_SETOR`: armazena os setores responsáveis pelos atendimentos.
- `TB_CATEGORIA`: armazena as categorias dos problemas, vinculadas aos setores.
- `TB_USUARIO`: armazena os usuários solicitantes e técnicos.
- `TB_CHAMADO`: armazena os chamados registrados.
- `TB_INTERACAO`: armazena as mensagens e interações relacionadas aos chamados.

Os relacionamentos entre as tabelas são definidos por chaves estrangeiras no PostgreSQL.

## Como executar o projeto

### Pré-requisitos

- Git
- Docker
- Docker Compose

Não é necessário instalar Java, Maven ou PostgreSQL localmente para executar a aplicação pelo Docker Compose.

### 1. Clonar o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd api-sistema-de-chamados-ifrn
```

Substitua `<URL_DO_REPOSITORIO>` pela URL do repositório publicado no GitHub.

### 2. Configurar as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto, conforme as variáveis utilizadas no `docker-compose.yml`:

```env
POSTGRES_DB=chamados
POSTGRES_USER=chamados_user
POSTGRES_PASSWORD=defina_uma_senha
```

Utilize uma senha adequada para o seu ambiente. O arquivo `.env` não deve ser enviado ao repositório; mantenha-o no `.gitignore`.

### 3. Iniciar a aplicação

Na raiz do projeto, execute:

```bash
docker compose up --build
```

O Docker Compose iniciará o PostgreSQL e a API. O serviço da API aguarda o banco de dados passar pela verificação de saúde antes de iniciar.

A aplicação ficará disponível em:

```text
http://localhost:8080
```

Para encerrar os serviços, utilize `Ctrl + C` ou execute:

```bash
docker compose down
```

**Atenção:** não utilize `docker compose down -v` se quiser preservar os dados do banco, pois essa opção remove os volumes associados ao Compose.

## Endpoints da API

Todos os endpoints abaixo utilizam o prefixo `http://localhost:8080`.

### Setores

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/setores` | Cadastra um setor |
| `GET` | `/setores` | Lista setores, com opção de filtro por nome e paginação |
| `GET` | `/setores/{id}` | Consulta um setor |
| `PUT` | `/setores/{id}` | Atualiza um setor |
| `DELETE` | `/setores/{id}` | Exclui um setor, respeitando as regras de integridade |
| `GET` | `/setores/{id}/categorias` | Lista as categorias de um setor |

### Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/categorias` | Cadastra uma categoria |
| `GET` | `/categorias` | Lista categorias, com filtros por nome e setor |
| `GET` | `/categorias/{id}` | Consulta uma categoria |
| `PUT` | `/categorias/{id}` | Atualiza uma categoria |
| `DELETE` | `/categorias/{id}` | Exclui uma categoria, respeitando as regras de integridade |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastra um usuário |
| `GET` | `/usuarios` | Lista usuários, com filtros por nome, tipo e setor |
| `GET` | `/usuarios/{id}` | Consulta um usuário |
| `PUT` | `/usuarios/{id}` | Atualiza um usuário |
| `GET` | `/usuarios/{id}/chamados` | Lista os chamados relacionados a um usuário, com opção de filtro por status |

### Chamados

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/chamados` | Abre um chamado |
| `GET` | `/chamados` | Lista chamados com filtros e paginação |
| `GET` | `/chamados/{id}` | Consulta os detalhes de um chamado, incluindo suas interações |
| `PATCH` | `/chamados/{id}/assumir` | Atribui um técnico ao chamado |
| `PATCH` | `/chamados/{id}/resolver` | Registra a resolução do chamado |
| `PATCH` | `/chamados/{id}/cancelar` | Cancela um chamado, respeitando as regras de negócio |
| `POST` | `/chamados/{id}/interacoes` | Registra uma interação no chamado |

### Filtros e paginação

A listagem de chamados aceita os seguintes parâmetros de consulta:

- `status`
- `prioridade`
- `setorId`
- `categoriaId`
- `solicitanteId`
- `tecnicoId`
- `dataInicio`
- `dataFim`
- `page`
- `size`
- `sort`

Exemplo:

```http
GET http://localhost:8080/chamados?status=ABERTO&page=0&size=10
```

A listagem também pode ser filtrada por categoria, setor, solicitante, técnico, prioridade e período de abertura.

Os endpoints paginados utilizam os parâmetros de paginação do Spring Data, como `page` e `size`.

## Validação

Os testes da API podem ser realizados utilizando o Insomnia.

Com os serviços em execução, configure as requisições para `http://localhost:8080` e teste os endpoints de acordo com suas funcionalidades.

Sugestões de validação:

1. Cadastrar e consultar setores.
2. Cadastrar categorias e verificar sua associação com os setores.
3. Cadastrar e consultar usuários.
4. Abrir um chamado e verificar seu status inicial e sua prioridade.
5. Consultar chamados utilizando os filtros disponíveis.
6. Atribuir um técnico e verificar as regras de associação entre setor e categoria.
7. Registrar uma interação e consultar os detalhes do chamado.
8. Resolver ou cancelar chamados e verificar as transições de status permitidas.
9. Tentar realizar operações inválidas e verificar as respostas de erro da API.
10. Reiniciar a aplicação e confirmar que os dados continuam disponíveis.

As respostas devem ser verificadas conforme o comportamento implementado nos controllers, serviços e tratamento global de exceções.

## Persistência dos dados

A persistência é realizada no PostgreSQL, utilizando o Spring Data JPA e o Hibernate para mapear as entidades da aplicação para as tabelas do banco de dados.

O projeto utiliza o Flyway para versionar e executar as migrações do banco de dados.

As migrações ficam no diretório:

```text
src/main/resources/db/migration/
```

Os arquivos seguem o padrão de nomenclatura do Flyway, por exemplo:

```text
V1__criar_tabelas.sql
V2__inserir_dados_iniciais.sql
V3__inserir_chamados_iniciais.sql
```

A V1 cria a estrutura do banco, enquanto as migrações seguintes inserem dados iniciais e outros registros definidos para a aplicação.

O Hibernate utiliza a configuração `ddl-auto=validate` para verificar a compatibilidade entre o mapeamento das entidades e a estrutura existente no banco, sem assumir a responsabilidade de criar ou atualizar o esquema.

O Docker Compose configura um volume persistente para o PostgreSQL. Dessa forma, os dados permanecem armazenados mesmo quando os containers são parados ou recriados, desde que o volume não seja removido.

## Estrutura do projeto

A organização segue a separação em camadas e os recursos de configuração do Spring Boot:

```text
api-sistema-de-chamados-ifrn/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/alessandra/api_sistema_de_chamados_ifrn/
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── entity/
│       │       ├── dto/
│       │       └── exception/
│       └── resources/
│           ├── application.properties
│           └── db/
│               └── migration/
├── Dockerfile
├── docker-compose.yml
├── .env
├── .gitignore
├── pom.xml
└── README.md
```

A estrutura acima representa a organização esperada das camadas. Os nomes dos diretórios devem ser ajustados caso o projeto utilize uma organização diferente.

## Códigos HTTP

A API utiliza códigos HTTP para indicar o resultado das operações. Entre os principais:

| Código | Significado |
|---|---|
| `200 OK` | Requisição realizada com sucesso |
| `201 Created` | Recurso criado com sucesso |
| `204 No Content` | Operação concluída sem conteúdo de resposta |
| `400 Bad Request` | Requisição inválida |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Conflito com uma regra de negócio ou restrição |

As respostas de erro dependem das exceções lançadas pela aplicação e de seu tratamento global.

