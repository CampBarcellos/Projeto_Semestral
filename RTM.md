# RTM — Matriz de Rastreabilidade de Requisitos
### Projeto: Biblioteca Pessoal | Matéria: Qualidade de Software

---

## 1. Requisitos Funcionais

| ID    | Requisito                                      |
|-------|------------------------------------------------|
| RF-01 | Cadastrar novo usuário                         |
| RF-02 | Fazer login com e-mail e senha                 |
| RF-03 | Fazer logout (encerrar sessão)                 |
| RF-04 | Adicionar um livro                             |
| RF-05 | Listar todos os livros                         |
| RF-06 | Buscar livro por ID                            |
| RF-07 | Editar um livro existente                      |
| RF-08 | Remover um livro                               |

---

## 2. Matriz de Rastreabilidade

| ID    | Requisito               | Classe Testada       | Método de Teste                        | Tipo de Teste         |
|-------|-------------------------|----------------------|----------------------------------------|-----------------------|
| RF-01 | Cadastrar usuário       | UsuarioService       | `deveCadastrarUsuarioComSucesso()`     | Unitário / Integração |
| RF-01 | Cadastrar usuário       | UsuarioController    | `deveCadastrarUsuarioViaController()`  | Caixa Preta (E2E)     |
| RF-02 | Login de usuário        | UsuarioService       | `deveBuscarUsuarioPorId()`             | Unitário / Integração |
| RF-03 | Logout / Sessão         | Frontend (app.js)    | —                                      | Manual                |
| RF-04 | Adicionar livro         | LivroService         | `deveAdicionarEListarLivros()`         | Unitário / Integração |
| RF-04 | Adicionar livro         | LivroService         | `deveAdicionarLivroParametrizado()`    | Parametrizado         |
| RF-04 | Adicionar livro         | LivroController      | `deveAdicionarLivroViaController()`    | Caixa Preta (E2E)     |
| RF-05 | Listar livros           | LivroService         | `deveAdicionarEListarLivros()`         | Unitário / Integração |
| RF-05 | Listar livros           | LivroController      | `deveListarLivrosViaController()`      | Caixa Preta (E2E)     |
| RF-06 | Buscar livro por ID     | LivroService         | `deveBuscarLivroPorId()`               | Unitário / Integração |
| RF-07 | Editar livro            | LivroService         | `deveAtualizarLivro()`                 | Unitário / Integração |
| RF-08 | Remover livro           | LivroService         | `deveDeletarLivro()`                   | Unitário / Integração |
| RF-08 | Remover livro           | LivroController      | `deveDeletarLivroViaController()`      | Caixa Preta (E2E)     |

---

## 3. Diagramas UML de Sequência

### RF-01 — Cadastrar Usuário

```
Frontend          UsuarioController     UsuarioService     UsuarioRepository      BD
   |                     |                    |                    |               |
   |-- POST /usuarios --> |                    |                    |               |
   |                     |-- adicionarUsuario()|                    |               |
   |                     |                    |-- save(usuario) --> |               |
   |                     |                    |                    |-- INSERT ----> |
   |                     |                    |                    |<-- OK -------- |
   |                     |                    |<-- usuario salvo --|               |
   |                     |<-- usuario salvo --|                    |               |
   |<-- 200 OK + JSON -- |                    |                    |               |
```

---

### RF-02 — Login de Usuário

```
Frontend          UsuarioController     UsuarioService     UsuarioRepository      BD
   |                     |                    |                    |               |
   |-- GET /usuarios/{id}|                    |                    |               |
   |                     |-- buscarPorId(id) >|                    |               |
   |                     |                    |-- findById(id) --> |               |
   |                     |                    |                    |-- SELECT ----> |
   |                     |                    |                    |<-- usuario --- |
   |                     |                    |<-- Optional<User>--|               |
   |                     |<-- usuario --------|                    |               |
   |<-- 200 OK + JSON -- |                    |                    |               |
```

---

### RF-04 — Adicionar Livro

```
Frontend          LivroController       LivroService       LivroRepository        BD
   |                     |                    |                    |               |
   |-- POST /livros ----> |                    |                    |               |
   |                     |-- adicionarLivro() >                    |               |
   |                     |                    |-- save(livro) ----> |               |
   |                     |                    |                    |-- INSERT ----> |
   |                     |                    |                    |<-- OK -------- |
   |                     |                    |<-- livro salvo ----|               |
   |                     |<-- livro salvo ----|                    |               |
   |<-- 200 OK + JSON -- |                    |                    |               |
```

---

### RF-05 — Listar Livros

```
Frontend          LivroController       LivroService       LivroRepository        BD
   |                     |                    |                    |               |
   |-- GET /livros -----> |                    |                    |               |
   |                     |-- listarLivros() -->|                    |               |
   |                     |                    |-- findAll() ------> |               |
   |                     |                    |                    |-- SELECT ----> |
   |                     |                    |                    |<-- lista ----- |
   |                     |                    |<-- List<Livro> ----|               |
   |                     |<-- List<Livro> ----|                    |               |
   |<-- 200 OK + JSON -- |                    |                    |               |
```

---

### RF-07 — Editar Livro

```
Frontend          LivroController       LivroService       LivroRepository        BD
   |                     |                    |                    |               |
   |-- PUT /livros/{id} ->|                    |                    |               |
   |                     |-- atualizarLivro() >                    |               |
   |                     |                    |-- save(livro) ----> |               |
   |                     |                    |                    |-- UPDATE ----> |
   |                     |                    |                    |<-- OK -------- |
   |                     |                    |<-- livro atualizado|               |
   |                     |<-- livro atualizado|                    |               |
   |<-- 200 OK + JSON -- |                    |                    |               |
```

---

### RF-08 — Remover Livro

```
Frontend          LivroController       LivroService       LivroRepository        BD
   |                     |                    |                    |               |
   |-- DELETE /livros/{id}|                    |                    |               |
   |                     |-- deletarLivro(id) >                    |               |
   |                     |                    |-- deleteById(id) -> |               |
   |                     |                    |                    |-- DELETE ----> |
   |                     |                    |                    |<-- OK -------- |
   |                     |<-- void ---------- |                    |               |
   |<-- 200 OK --------- |                    |                    |               |
```

---

## 4. Cobertura de Testes

| Classe            | Métodos cobertos                                      | Cobertura estimada |
|-------------------|-------------------------------------------------------|--------------------|
| LivroService      | adicionar, listar, buscarPorId, atualizar, deletar    | ✅ ~100%           |
| UsuarioService    | adicionar, listar, buscarPorId, atualizar, deletar    | ✅ ~100%           |
| LivroController   | POST, GET, GET/{id}, PUT, DELETE                      | ✅ ~100%           |
| UsuarioController | POST, GET, GET/{id}, PUT, DELETE                      | ✅ ~100%           |

**Meta do projeto: mínimo 80% de cobertura ✅**

---

## 5. Estratégia de Testes

| Tipo                  | Ferramenta                  | Descrição                                      |
|-----------------------|-----------------------------|------------------------------------------------|
| Unitário / Integração | JUnit 5 + Testcontainers    | Testa a lógica dos Services com banco real     |
| Parametrizado         | JUnit 5 `@ParameterizedTest`| Múltiplos cenários para um mesmo teste         |
| Caixa Preta (E2E)     | MockMvc (Spring Boot Test)  | Testa os endpoints HTTP dos Controllers        |
| Caixa Branca          | JUnit 5                     | Testa lógica interna dos métodos               |

> ⚠️ **O uso de Mocks está proibido.** Todos os testes de persistência utilizam **Testcontainers** com banco de dados real.