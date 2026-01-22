# Scheduling API

API REST para sistema de agendamento de serviços usando Java 17 e Spring Boot.

## Tecnologias
- Spring Boot 3 (Web, Security, Data JPA, Validation)
- H2 Database (dev)
- JWT (jjwt)
- Maven, Lombok

## Executando
1. Java 17 instalado.
2. `mvn clean install`
3. `mvn spring-boot:run`

H2 console em `/h2-console` (user `sa`, senha `password`).

## Autenticação
- Registro: `POST /api/auth/register`
- Login: `POST /api/auth/login` (retorna Bearer token)
- Enviar header `Authorization: Bearer <token>` nas chamadas protegidas.

## Endpoints principais
- Serviços: `POST /api/servicos` (funcionário), `GET /api/servicos`, `PUT /api/servicos/{id}/ativo/{ativo}` (funcionário)
- Agendamentos: `POST /api/agendamentos` (cliente), `GET /api/agendamentos`, `POST /api/agendamentos/{id}/cancelar`

## Regras de negócio
- Sem agendamento em horário duplicado para mesmo funcionário.
- Sem agendamento em data passada.
- Não cancelar agendamento concluído.

## Config JWT
Defina `security.jwt.secret` como Base64 de ao menos 256 bits em `application.yaml` ou variável de ambiente. Atualize `expiration-seconds` se necessário.
