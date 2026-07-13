# HealthCare API

Sistema de gestão para clínica médica — API REST em Java + Spring Boot.

> 🚧 Projeto em construção, feito passo a passo. Esta é a **Parte 1: Setup inicial**.

## Tecnologias (previstas para o projeto completo)

Java 21 · Spring Boot · Spring Security · JWT · PostgreSQL · JPA/Hibernate · Docker · Flyway · Lombok · Swagger · JUnit · Mockito

## Arquitetura de pastas

```
src/main/java/com/healthcare/api/
├── controller   # endpoints REST
├── service      # regras de negócio
├── repository   # acesso a dados (Spring Data JPA)
├── entity       # entidades JPA (tabelas)
├── dto          # objetos de transferência de dados
├── mapper       # conversão entity <-> dto
├── config       # configurações gerais (Swagger, CORS, etc)
├── security     # JWT, filtros, roles
├── exception    # tratamento global de exceções
├── validation   # validações customizadas
└── util         # utilitários
```

## Como rodar (Parte 1)

1. Subir o banco de dados PostgreSQL via Docker:

   ```bash
   docker compose up -d
   ```

2. Rodar a aplicação:

   ```bash
   mvn spring-boot:run
   ```

   (se preferir usar o Maven Wrapper, rode `mvn -N io.takari:maven:wrapper` uma vez para gerá-lo)

3. A API sobe em `http://localhost:8080`.

> Ainda não há endpoints nem tabelas — isso vem nas próximas partes (entidades + Flyway, depois segurança JWT).

## Próximos passos

- [ ] Parte 2: Entidades + migrations Flyway
- [ ] Parte 3: Segurança (JWT)
- [ ] Parte 4: CRUD de Usuário
- [ ] Parte 5: Paciente e Médico
- [ ] Parte 6: Consulta
- [ ] Parte 7: Prontuário
- [ ] Parte 8: Financeiro
- [ ] Parte 9: Testes
- [ ] Parte 10: Swagger, exceptions globais, extras
- [ ] Parte 11: Docker final + README completo
