<div align="center">

![](https://img.shields.io/badge/Status-Em%20Desenvolvimento-orange)
</div>

<div align="center">

# AuthMicroService - Microserviço de autenticação.
![](https://img.shields.io/badge/Autor%20-%20Pablo%20Machado%20Lima%20-%20?style=flat)
![](https://img.shields.io/badge/Language-java-brightgreen)
![](https://img.shields.io/badge/Spring%20Boot%20-%20Gradle%20-%20?style=flat&logo=spring)
![](https://img.shields.io/badge/Arquitetura-Hexagonal-brightgreen)

![Sonar Build](https://img.shields.io/badge/Sonar%20-%20Builder%20-%20?style=flat&logo=sonarqubeserver
)
![GitHub](https://img.shields.io/github/license/wesleyosantos91/poc-multi-module-arch-hexagonal-springboot)
</div> 

<div align="center">

## Arquitetura
![Arquitetura](https://i.imgur.com/kW1Wsl2.png "Arquitetura")

</div>

<div align="center">

## Sonar

![Lines of Code](https://img.shields.io/badge/line_of_code-1.3k-green%20?logo=sonarqubecloud)
![Coverage](https://img.shields.io/badge/coverage-96.2%25-green%20?logo=sonarqubecloud)
![Duplicated Lines (%)](https://img.shields.io/badge/duplicated_lines-2.2%25-green%20?logo=sonarqubecloud)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot&metric=security_rating)](https://sonarcloud.io/dashboard?id=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot&metric=bugs)](https://sonarcloud.io/dashboard?id=wesleyosantos91_poc-multi-module-arch-hexagonal-springboot)

</div>

## Fundamentos teóricos

> Ports & Adapters Architecture ou Arquitetura Hexagonal: A arquitetura hexagonal, ou arquitetura de portas e adaptadores, é um padrão arquitetural usado no design de software. O objetivo é criar componentes de aplicativos fracamente acoplados que possam ser facilmente conectados ao ambiente de software por meio de portas e adaptadores.

##  Pré -requisitos

- [ `Java 21+` ](https://www.oracle.com/java/technologies/downloads/#java21)
- [ `Docker` ](https://www.docker.com/)
- [ `Docker-Compose` ](https://docs.docker.com/compose/install/)

## Stack
- **Sonar** Analise de qualidade e cobertura de testes

## Portas
| Aplicação   | Porta |
|-------------|-------|
| Ms-Launcher | 8080  |
| Sonarqube   | 9000  |
| Postgres    | 5432  |

## Links

- Sonar Cloud
    - http://localhost:9000
- OpenAPI
    - Swagger
        - http://localhost:8080/swagger-ui/index.html
    - API Docs
        - http://localhost:8080/v3/api-docs
## Setup

- ### Variáveis de ambiente

| Variável de Ambiente  | Descrição                       |
|-----------------------|---------------------------------|
| `SPRING_MAIL_PASS`    | Especifique a senha do email.   |
| `SPRING_MAIL_USER`    | Especifique o usuário do email. |
| `SPRING_SONAR_TOKEN`  | Especifique o token do sonar.   |


### Executar docker-compose para subir aplicação com container docker
- Execute o seguinte comando para subir os containers:
  ```
  docker-compose up
  ```
- Execute o seguinte comando para verificar os status do containers docker:
  ```
  docker-compose ps
  ```

### Sonarqube

- Realize o Login com user: admin password: admin, gerar para uma nova senha
- Clique na opção Manually
- Crie os Project display name/project key: `authmicroservice`
- Clique na opção Locally
- Preencha com `wos` e clique em Generate
- Copie o token gerado e cole na variável de ambiente `SPRING_SONAR_TOKEN` no arquivo

### Estrutura do projeto

```plaintext
authmicroservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hubsi/authmicroservice/
│   │   │       ├── adapters/           # Adaptadores de entrada/saída
│   │   │       │   ├── in/             # Entrada (Controllers)
│   │   │       │   │   ├── controller/ # Controllers REST
│   │   │       │   │   └── request/    # DTOs de requisição
│   │   │       │   └── out/            # Saída (Repositories)
│   │   │       │       ├── persistence/ # Implementações de repositórios
│   │   │       │       │   ├── repository/ # Repositórios JPA
│   │   │       │       │   └── entities/   # Entidades JPA
│   │   │       │       ├── email/         # Adapter de envio de email
│   │   │       │       ├── response/      # DTOs de resposta
│   │   │       │       └── security/      # Integrações de segurança
│   │   │       ├── application/       # Casos de uso
│   │   │       │   ├── service/       # Serviços de aplicação
│   │   │       │   ├── usecases/      # Interfaces de casos de uso
│   │   │       │   └── port/          # DTOs e portas de saída
│   │   │       │       ├── out/       # Interfaces de repositórios
│   │   │       ├── domain/            # Classes de domínio
│   │   │       ├── infrastructure/    # Infraestrutura (DB, Email, etc)
│   │   │       │   ├── config/        # Configurações do Spring
│   │   │       │   ├── exceptions/    # Exceções personalizadas
│   │   │       ├── utils/             # Utilitários e helpers
│   │   │       │   ├── enums/         # Enumerações
│   │   │       │   ├── mappers/       # Mapeadores
│   │   │       │   └── validation/    # Validações customizadas
│   │   └── resources/
│   │       └── ...
│   └── test/
├── build.gradle
├── docker-compose.yml
├── README.md
└── ...