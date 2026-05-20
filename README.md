# 📚 Biblioteca Pessoal

Aplicação web completa para cadastro e gerenciamento de livros de uma biblioteca pessoal, com autenticação de usuários. Desenvolvida com Spring Boot e MySQL, seguindo boas práticas de qualidade e testabilidade.

---

## 👥 Grupo

- Catarina
- Thainá
- Thiago

**Matéria:** Qualidade de Software

---

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.4.5
- MySQL (via XAMPP)
- JPA / Hibernate
- JUnit 5
- JaCoCo
- GitHub Actions (CI)
- HTML, CSS e JavaScript (frontend)

---

## ⚙️ Como rodar o projeto

### Pré-requisitos

- Java 17 instalado
- Maven instalado
- XAMPP com MySQL rodando
- Banco de dados `biblioteca` criado no MySQL

### Passos

1. Clone o repositório:
```bash
   git clone https://github.com/CampBarcellos/Projeto_Semestral.git
```

2. Entre na pasta do projeto:
```bash
   cd Projeto_Semestral/biblioteca
```

3. Suba o XAMPP e inicie o MySQL

4. Execute o projeto:
```bash
   mvn spring-boot:run
```

5. Acesse no navegador: `http://localhost:8080`

---

## 🧪 Testes

Para rodar os testes (com XAMPP e MySQL ligados):

```bash
mvn test
```

Para gerar o relatório de cobertura:

```bash
mvn verify
```

O relatório fica em `target/site/jacoco/index.html`.

---

## ✅ Cobertura de Testes

Cobertura mínima exigida: **80%**

| Classe | Cobertura |
|---|---|
| LivroService | ~100% |
| UsuarioService | ~100% |
| LivroController | ~100% |
| UsuarioController | ~100% |

---

## 🔄 CI — GitHub Actions

O projeto possui pipeline de integração contínua configurado com GitHub Actions. A cada push na branch `main`, o pipeline:

1. Sobe um banco MySQL no ambiente de CI
2. Roda todos os testes automaticamente
3. Gera o relatório de cobertura com JaCoCo
4. Faz o build do projeto

---

## 📄 Documentação

O arquivo `RTM.md` contém a Matriz de Rastreabilidade de Requisitos com os diagramas UML de sequência de cada funcionalidade.