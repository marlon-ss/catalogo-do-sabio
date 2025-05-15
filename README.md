# Catálogo do Sábio 📚

Uma API REST para gerenciamento de biblioteca digital, desenvolvida com Spring Boot e MongoDB.

## 📋 Pré-requisitos

- Java 17
- Git
- Docker

## 🔧 Instalação

1. Clone o repositório:
```bash
  git clone https://github.com/marlon-ss/catalogo-do-sabio
```

2. Entre no diretório do projeto:
```bash
  cd catalogo-do-sabio
```

3. Execute o Docker Compose:
```bash
  docker-compose up -d
```
Feitos esses passos, o serviço ficará disponível na porta 8080 

## 📚 Documentação da API

- Swagger UI: `http://localhost:8080/swagger-ui.html`


## 🔍 Endpoints

### Usuários
- POST `/users/register` - Registro de novo usuário (apenas ADMIN)
- POST `/users/get/{username}` - Busca de usuário (apenas ADMIN)

### Livros
- GET `/books` - Lista todos os livros (paginado)
- GET `/books/{id}` - Busca livro por ISBN
- GET `/books/author/{author}` - Busca livros por autor
- GET `/books/genre/{genre}` - Busca livros por gênero
- GET `/books/recents` - Lista livros recentemente vistos

## 📝 Parâmetros de Paginação

Os endpoints que retornam listas de livros suportam paginação através dos seguintes parâmetros:

### Parâmetros de Requisição
- `page` (opcional): Número da página desejada (começa em 0)
    - Valor padrão: 0
    - Tipo: inteiro
- `size` (opcional): Quantidade de itens por página
    - Valor padrão: 50
    - Tipo: inteiro

### Headers de Resposta
Os endpoints paginados retornam os seguintes headers para controle de paginação:
- `X-Total-Count`: Número total de registros disponíveis
- `X-Total-Pages`: Número total de páginas
- `X-Current-Page`: Número da página atual
- `X-Page-Size`: Quantidade de itens por página

### Exemplo de Uso
```bash
# Buscar segunda página com 20 itens
GET /books?page=1&size=20

# Buscar livros do autor com paginação personalizada
GET /books/author/Stephen%20King?page=0&size=10

# Buscar livros por gênero
GET /books/genre/Terror?page=2&size=15
```
## 📝 Relatório

### I. Arquitetura de solução e arquitetura técnica
#### 🚀 Funcionalidades implementadas

- Catálogo completo de livros
- Sistema de autenticação de usuários
- Cache com Redis para melhor performance
- Histórico de livros visualizados por usuário
- Busca por autor, gênero e ISBN
- Paginação de resultados

#### 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- MongoDB
- Redis
- Docker
- Spring Security
- Swagger/OpenAPI

#### 📝 Decisões de design
- O desgin deste serviço foi desenvolvido pensando em escalabilidade e performance.
Para a escalabilidade foi utilizado um banco NoSQL (MongoDB) e foi utilizado o Redis como cache 
para que o serviço ganhe em performance e também visando escalabilidade com cache distribuído.
- Para a documentação, foi utilizado o Swagger por ser um metodo simples e intuitivo de visualizar 
as propriedas e funcionalidades da API.

### II. Explicação sobre a aplicação
- A aplicação foi desenvolvida visando ser um sistema simples de pesquisa de livros;
- Para uma vista mais técnica, a documentação sobre o funcionamento de cada endpoint pode ser acessada aqui: [Swagger](./api-docs.yaml);
- Em visão geral, o serviço funcionará com um usuário autenticado (basic auth) consumindo algum dos endpoints do BookController;
- Quando consumido o endpoint de busca específica por um livro, este livro ficará salvo aos recentes do usuário
  (limitado em 5 livros);
- Na inicialização do serviço e do banco de dados, é criado um usuário admin, o qual terá acesso aos endpoints do UserApiController;
- Neste controller será possivel criar novos usuários, tanto usarios comuns, quanto admin.
- Para o tratamento de exceções, os endpoints retornarão status 404 com uma mensagem informando que o objeto pesquisado não foi encontrado,
seja ele um usuário ou livro;
- Caso um usuário admin tente criar um outro usuário com um username já existente, esta chamada retornará um status 400
informando que já existe um usuário com este username.
- As buscas de livros por genero e autor, podem ser feitas com um texto parcial. Ex.: Autor: Marlon, a pesquisa pode ser feita enviando somente "mar".

### III. Melhorias e considerações finais
#### Pontos de melhoria:
- Troca do basic auth para autenticaçao JWT;
- Criação de novos endpoints para inclusao, atualizaçao e exclusao de livros, exclusao e atualizaçao de usuarios;
- Criação de novos filtros para melhores buscas aos livros

#### Considerações finais:
Entrega feita seguiundo todos os requisitos solicitados, incluindo os opcionais e com um extra incluindo a autenticação.
Durante o desenvolvimento esbarrei em alguns problemas com a configuração do Redis, MongoDB e Docker, principalmente para
rodar tudo no container, mas tais problemas foram contornados e o serviço foi entregue funcional.