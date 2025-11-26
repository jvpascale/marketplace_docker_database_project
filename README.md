# Projeto de Gerenciamento de Banco de Dados

## Visão Geral

Este projeto é uma aplicação de gerenciamento de banco de dados desenvolvida em **Java** que utiliza o **PostgreSQL** como Sistema Gerenciador de Banco de Dados (SGBD). Ele foi estruturado para demonstrar a implementação de um sistema de persistência de dados robusto, seguindo uma arquitetura em camadas (Entidades, Repositórios e Serviços) para garantir a separação de responsabilidades e a manutenibilidade do código.

O projeto simula um sistema de gerenciamento de dados que abrange informações de:

- **Usuários**

- **Funcionários**

- **Dependentes**

- **Departamentos**

- **Produtos**

- **Pedidos**

## Guia de Início Rápido (Docker) 
#### Requisitos: Docker instalado na maquina

Para rodar a aplicação de maneira simples e rápida, utilizamos o Docker. Siga os passos abaixo:

1.  Certifique-se de que o arquivo `docker-compose.yaml` , [Clique aqui para visualizar o arquivo citado](https://github.com/jvpascale/marketplace_docker_database_project/blob/master/docker-compose.yml) , está presente no diretório.
2.  Abra um terminal (Linux ou Windows) dentro da pasta onde está o arquivo `docker-compose.yaml`.
3.  Antes de inicializar, certifique-se que as portas 3000, 8080 e 5433, da maquina local, estão livres para serem utilizadas
4.  Execute o seguinte comando para subir o ambiente:
    ```bash
    docker-compose up --build
    ```
5.  Após a inicialização, acesse o frontend da aplicação pelo navegador através da URL:
    [http://localhost:3000](http://localhost:3000)
6.  Na página inicial, clique no botão para **popular o banco de dados**.
7.  Pronto! A aplicação está configurada e pronta para ir.

## Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
| --- | --- | --- |
| **Linguagem Backend** | Java | Linguagem principal de desenvolvimento do backend. |
| **Linguagem Frontend** | Typescript, HTML, CSS | Linguagens principais de desenvolvimento do frontend
| **Banco de Dados** | PostgreSQL | SGBD relacional utilizado para persistência de dados. |
| **Conexão BD** | JDBC | API padrão do Java para conexão e execução de comandos SQL. |
| **IDE** | IntelliJ IDEA (Implícito pelo arquivo `.iml`) | Ambiente de Desenvolvimento Integrado. |

## Funcionalidades da API (Endpoints)

O projeto expõe uma API RESTful para interagir com os dados, permitindo consultas complexas e filtragens. Abaixo estão alguns dos principais endpoints disponíveis:

### Usuários e Dependentes
| Endpoint | Descrição |
| :--- | :--- |
| `POST /users/search` | Busca usuários pelo sobrenome. |
| `POST /users/buyers/price` | Busca usuários compradores com base no preço do pedido. |
| `POST /users/buyers/filter` | Filtra usuários compradores por categoria e data do pedido. |
| `POST /dependents/unit` | Lista dependentes por unidade de localização. |
| `POST /dependents/minors` | Lista dependentes menores de idade. |
| `POST /dependents/employee` | Lista dependentes de um funcionário específico (via CPF). |

### Produtos
| Endpoint | Descrição |
| :--- | :--- |
| `POST /products/seller` | Lista produtos por ID do vendedor. |
| `POST /products/filter/sales` | Filtra produtos por quantidade de vendas em um período. |
| `POST /products/filter/price` | Filtra produtos por faixa de preço. |

### Pedidos
| Endpoint | Descrição |
| :--- | :--- |
| `POST /orders/user` | Lista pedidos de um usuário específico. |
| `POST /orders/filter/status` | Filtra pedidos por status e data. |
| `POST /orders/filter/price` | Filtra pedidos por faixa de preço. |
| `POST /orders/filter/department` | Filtra pedidos por departamento e data. |
| `POST /orders/employee` | Lista pedidos associados a um funcionário (via CPF). |

### Funcionários e Departamentos
| Endpoint | Descrição |
| :--- | :--- |
| `POST /employees/filter/supervisor` | Lista funcionários supervisionados por um CPF específico. |
| `POST /employees/filter/productivity` | Filtra funcionários por número de pedidos entregues em um período. |
| `POST /employees/filter/department` | Lista funcionários por departamento. |
| `POST /departments/order-route` | Obtém a rota (origem/destino) de um pedido. |
| `POST /departments/filter/order-quantity` | Filtra departamentos por quantidade de pedidos. |
| `POST /departments/filter/employee-quantity` | Filtra departamentos por quantidade de funcionários. |

## Arquitetura do Projeto

O projeto segue um padrão de design que separa as responsabilidades em camadas bem definidas:

1. **`Entities`** (Entidades): Contém as classes de modelo de dados (POJOs - Plain Old Java Objects) que representam as tabelas do banco de dados.
  - `Departament.java`
  - `Dependent.java`
  - `Employee.java`
  - `Order.java`
  - `Product.java`
  - `User.java`

1. **`Database`** (Banco de Dados): Contém a classe `DB.java`, responsável por gerenciar a conexão com o banco de dados PostgreSQL.

1. **`Repositories`** (Repositórios): Camada de Acesso a Dados (DAO - Data Access Object). Implementa a lógica de comunicação com o banco de dados, contendo métodos para operações CRUD (Create, Read, Update, Delete) para cada entidade.

1. **`Services`** (Serviços): Camada de Lógica de Negócios. Contém a lógica de aplicação e regras de negócio, utilizando os Repositórios para interagir com o banco de dados.

1. **`Controllers`** (Controladores): (Estrutura presente, mas sem código visível) Destinada a gerenciar o fluxo de requisições e respostas, atuando como interface entre a camada de Serviço e a interface do usuário (ou outra camada de apresentação).

1. **`Main.java`**: Ponto de entrada da aplicação.

## Configuração e Execução

Para configurar e executar o projeto em sua máquina, siga os passos abaixo:

### Pré-requisitos

- **Java Development Kit (JDK):** Versão 8 ou superior.

- **PostgreSQL:** Servidor de banco de dados instalado e em execução.

- **Driver JDBC para PostgreSQL:** Geralmente incluído em projetos Maven/Gradle, mas pode ser necessário adicioná-lo manualmente ao *classpath* do projeto se estiver usando uma IDE.

### 1. Configuração do Banco de Dados

O projeto está configurado para se conectar a um banco de dados PostgreSQL.

- **Nome do Banco de Dados:** `marketplace`

- **Usuário:** `user`

- **Senha:** `password`

- **Porta:** `5433`

**Atenção:** As credenciais de acesso estão definidas diretamente na classe `src/Database/DB.java`. Para um ambiente de produção, é **altamente recomendado** utilizar variáveis de ambiente ou arquivos de configuração seguros.

Você deve criar o banco de dados `meubanco` no seu servidor PostgreSQL.

```sql
CREATE DATABASE marketplace;
