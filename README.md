# 🏥 HealthCare API

Sistema de gestão para clínica médica — API REST desenvolvida em **Java + Spring Boot**, com autenticação via JWT, controle de acesso por perfil de usuário, agendamento de consultas, gestão financeira e documentação via Swagger.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens)
![Status](https://img.shields.io/badge/Status-Em%20desenvolvimento-yellow)

---

## 📌 Sobre o projeto

A **HealthCare API** simula o backend de uma clínica médica real. Ela permite cadastrar médicos e pacientes, agendar, remarcar e cancelar consultas, registrar pagamentos e gerar relatórios financeiros mensais — tudo protegido por autenticação JWT e controle de acesso baseado em perfis (roles).

O projeto foi construído com foco em **arquitetura em camadas**, **regras de negócio bem definidas** e **boas práticas de desenvolvimento backend com Spring**.

> 🚧 Projeto em desenvolvimento ativo. Módulos de médicos, pacientes, consultas e pagamentos já estão implementados e testados. Veja a seção [Roadmap](#-roadmap) para o que vem a seguir.

---

## 🗂️ Sumário

- [Tecnologias](#-tecnologias)
- [Arquitetura](#️-arquitetura)
- [Modelo de dados](#️-modelo-de-dados)
- [Perfis de acesso](#-perfis-de-acesso-roles)
- [Regras de negócio](#️-regras-de-negócio)
- [Endpoints principais](#-endpoints-principais)
- [Como executar](#-como-executar)
- [Documentação (Swagger)](#-documentação-swagger)
- [Roadmap](#-roadmap)
- [Autora](#-autora)

---

## 🚀 Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot |
| Segurança | Spring Security + JWT |
| Banco de dados | PostgreSQL |
| ORM | JPA / Hibernate |
| Containerização | Docker & Docker Compose |
| Documentação | Swagger / OpenAPI |

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas, separando responsabilidades de forma clara:

```
src/main/java/com/healthcare/api/
├── controller     # endpoints REST
├── service        # regras de negócio
├── repository     # acesso a dados (Spring Data JPA)
├── entity         # entidades JPA (tabelas)
├── dto            # objetos de transferência de dados
├── mapper         # conversão entity <-> dto
├── config         # configurações gerais (Swagger, CORS, etc)
├── security       # JWT, filtros, roles
├── exception      # tratamento global de exceções
├── validation     # validações customizadas
└── util           # utilitários
```

---

## 🗄️ Modelo de dados

Principais entidades e seus relacionamentos:

- **Usuário** — autenticação e perfil de acesso
- **Médico** — cadastro profissional (nome, CRM, especialidade)
- **Paciente** — cadastro do paciente (nome, CPF, contato)
- **Consulta** — vincula médico + paciente, com data, hora e status
- **Pagamento** — vinculado a uma consulta, com valor e forma de pagamento

---

## 🔐 Perfis de acesso (roles)

| Role | Permissões |
|---|---|
| `ADMIN` | Acesso total ao sistema |
| `MEDICO` | Acessa apenas seus próprios pacientes e consultas |
| `RECEPCIONISTA` | Gerencia agenda e cadastros, sem acesso a prontuário |
| `PACIENTE` | Acesso restrito às suas próprias informações |

Toda a autorização é feita via **JWT**, validado em cada requisição por endpoints protegidos com Spring Security.

---

## ⚖️ Regras de negócio

✅ Não é possível agendar uma consulta em horário já ocupado
✅ Consulta cancelada não pode receber pagamento
✅ Consultas podem ser remarcadas e canceladas, com validação de estado
🔲 Não é possível excluir médico com consulta futura agendada
🔲 Paciente menor de idade precisa de responsável vinculado
🔲 Recepcionista não pode acessar prontuário
🔲 Médico só acessa dados de seus próprios pacientes

> ✅ = implementado e testado · 🔲 = em desenvolvimento

---

## 📋 Endpoints principais

### Médicos
```
GET    /medicos
POST   /medicos
PUT    /medicos/{id}
DELETE /medicos/{id}
```

### Pacientes
```
GET    /pacientes
POST   /pacientes
```

### Consultas
```
POST   /consultas
PUT    /consultas/{id}/remarcar
PUT    /consultas/{id}/cancelar
```

### Financeiro
```
POST   /pagamentos
GET    /pagamentos/relatorio?ano={ano}&mes={mes}
```

Todos os endpoints protegidos exigem o header:
```
Authorization: Bearer {token}
```

---

## 🐳 Como executar

**Pré-requisitos:** Docker e Docker Compose instalados.

```bash
# 1. Clonar o repositório
git clone https://github.com/annakkarolyne/healthcare-api.git
cd healthcare-api

# 2. Subir a aplicação e o banco de dados juntos
docker-compose up --build
```

A API estará disponível em `http://localhost:8080`.

---

## 📖 Documentação (Swagger)

Com a aplicação rodando, acesse a documentação interativa da API em:

```
http://localhost:8080/swagger-ui.html
```

---

## 🗺️ Roadmap

- [x] CRUD de médicos
- [x] Cadastro de pacientes com validação de CPF
- [x] Agendamento, remarcação e cancelamento de consultas
- [x] Registro de pagamentos e relatório mensal
- [x] Autenticação JWT e controle por perfil
- [x] Containerização com Docker
- [x] Documentação Swagger
- [ ] Módulo de prontuário médico
- [ ] Testes automatizados (JUnit + Mockito)
- [ ] Deploy em nuvem (Render/Railway)

---

## 👩‍💻 Autora

Desenvolvido por **Ana Caroline**, como projeto de portfólio para demonstrar domínio de Java, Spring Boot, Spring Security, arquitetura em camadas e boas práticas de desenvolvimento backend.

🔗 [github.com/annakkarolyne](https://github.com/annakkarolyne)
