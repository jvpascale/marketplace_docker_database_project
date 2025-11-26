package com.marketplace_project.marketplace_project.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PopulateDatabaseRepository {

    private final JdbcTemplate jdbcTemplate;

    public PopulateDatabaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void populateDatabase() {
        String query_sql = """
            -- =================================================================================
            -- SCRIPT SQL CORRIGIDO - E-COMMERCE
            -- =================================================================================

            SET client_encoding = 'UTF8';
            SET search_path TO public;
            SET datestyle = 'ISO, YMD';

            BEGIN;

            -- 1. LIMPEZA
            DROP TABLE IF EXISTS Produto_Contem_Pedidos CASCADE;
            DROP TABLE IF EXISTS Pedidos CASCADE;
            DROP TABLE IF EXISTS Produto CASCADE;
            DROP TABLE IF EXISTS Dependente CASCADE;
            DROP TABLE IF EXISTS Funcionario CASCADE;
            DROP TABLE IF EXISTS Unidade CASCADE;
            DROP TABLE IF EXISTS Usuario CASCADE;

            -- 2. CRIAÇÃO DAS TABELAS
            CREATE TABLE Usuario (
                id SERIAL PRIMARY KEY,
                endereco VARCHAR(255) NOT NULL,
                p_nome VARCHAR(100) NOT NULL,
                sobrenome VARCHAR(100) NOT NULL
            );

            CREATE TABLE Unidade (
                localizacao VARCHAR(255) PRIMARY KEY,
                numero VARCHAR(50),
                nome VARCHAR(100) NOT NULL,
                gerente_cpf VARCHAR(14)
            );

            CREATE TABLE Funcionario (
                cpf VARCHAR(14) PRIMARY KEY,
                salario NUMERIC(10, 2) NOT NULL,
                nome VARCHAR(100) NOT NULL,
                cargo VARCHAR(100) NOT NULL,
                unidade_localizacao VARCHAR(255) NOT NULL,
                supervisor_cpf VARCHAR(14)
            );

            CREATE TABLE Dependente (
                nome VARCHAR(100) NOT NULL,
                idade INTEGER,
                parentesco VARCHAR(50),
                funcionario_cpf VARCHAR(14) NOT NULL,
                PRIMARY KEY (nome, funcionario_cpf)
            );

            CREATE TABLE Produto (
                id SERIAL PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                descricao TEXT,
                categoria VARCHAR(100),
                preco NUMERIC(10, 2) NOT NULL,
                estoque INTEGER NOT NULL,
                status VARCHAR(50),
                usuario_id INTEGER NOT NULL,
                texto_anunciado TEXT
            );

            CREATE TABLE Pedidos (
                codigo SERIAL PRIMARY KEY, 
                status VARCHAR(50) NOT NULL,
                data_de_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                valor_total NUMERIC(10, 2) NOT NULL,
                comprador_id INTEGER NOT NULL,
                vendedor_id INTEGER NOT NULL,
                meio_de_pagamento VARCHAR(50),
                numero_de_parcelas INTEGER,
                destino_localizacao VARCHAR(255) NOT NULL,
                origem_localizacao VARCHAR(255) NOT NULL,
                o_flag_chegada BOOLEAN DEFAULT FALSE,
                o_data TIMESTAMP WITHOUT TIME ZONE,
                d_flag_chegada BOOLEAN DEFAULT FALSE,
                d_data TIMESTAMP WITHOUT TIME ZONE,
                funcionario_cpf VARCHAR(14),
                placa_do_veiculo VARCHAR(10),
                data_de_entrega DATE,
                previsao_de_entrega DATE
            );

            CREATE TABLE Produto_Contem_Pedidos (
                produto_id INTEGER NOT NULL,
                pedido_codigo INTEGER NOT NULL,
                quantidade INTEGER NOT NULL,
                PRIMARY KEY (produto_id, pedido_codigo)
            );

            -- 3. CHAVES ESTRANGEIRAS
            ALTER TABLE Funcionario ADD CONSTRAINT fk_func_unidade FOREIGN KEY (unidade_localizacao) REFERENCES Unidade (localizacao) DEFERRABLE INITIALLY DEFERRED;
            ALTER TABLE Funcionario ADD CONSTRAINT fk_func_supervisor FOREIGN KEY (supervisor_cpf) REFERENCES Funcionario (cpf) DEFERRABLE INITIALLY DEFERRED;
            ALTER TABLE Unidade ADD CONSTRAINT fk_unidade_gerente FOREIGN KEY (gerente_cpf) REFERENCES Funcionario (cpf) DEFERRABLE INITIALLY DEFERRED;
            ALTER TABLE Dependente ADD CONSTRAINT fk_dep_funcionario FOREIGN KEY (funcionario_cpf) REFERENCES Funcionario (cpf);
            ALTER TABLE Produto ADD CONSTRAINT fk_prod_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario (id);
            ALTER TABLE Pedidos ADD CONSTRAINT fk_ped_comprador FOREIGN KEY (comprador_id) REFERENCES Usuario (id);
            ALTER TABLE Pedidos ADD CONSTRAINT fk_ped_vendedor FOREIGN KEY (vendedor_id) REFERENCES Usuario (id);
            ALTER TABLE Pedidos ADD CONSTRAINT fk_ped_destino FOREIGN KEY (destino_localizacao) REFERENCES Unidade (localizacao);
            ALTER TABLE Pedidos ADD CONSTRAINT fk_ped_origem FOREIGN KEY (origem_localizacao) REFERENCES Unidade (localizacao);
            ALTER TABLE Pedidos ADD CONSTRAINT fk_ped_funcionario FOREIGN KEY (funcionario_cpf) REFERENCES Funcionario (cpf);
            ALTER TABLE Produto_Contem_Pedidos ADD CONSTRAINT fk_pcp_produto FOREIGN KEY (produto_id) REFERENCES Produto (id);
            ALTER TABLE Produto_Contem_Pedidos ADD CONSTRAINT fk_pcp_pedido FOREIGN KEY (pedido_codigo) REFERENCES Pedidos (codigo);

            -- 4. POPULAÇÃO (DML)
            SET CONSTRAINTS ALL DEFERRED;

            -- Usuários
            INSERT INTO Usuario (endereco, p_nome, sobrenome) VALUES
            ('Rua das Flores, 123, São Paulo', 'Ana', 'Silva'),
            ('Avenida Brasil, 456, Rio de Janeiro', 'Bruno', 'Santos'),
            ('Praça da Sé, 789, Salvador', 'Carla', 'Oliveira'),
            ('Rua Augusta, 101, Curitiba', 'Daniel', 'Souza'),
            ('Avenida Paulista, 202, Belo Horizonte', 'Eduarda', 'Pereira'),
            ('Rua da Consolação, 303, Fortaleza', 'Fábio', 'Costa'),
            ('Avenida Ipiranga, 404, Porto Alegre', 'Gabriela', 'Ferreira'),
            ('Rua Oscar Freire, 505, Recife', 'Heitor', 'Rodrigues'),
            ('Avenida Rio Branco, 606, Manaus', 'Isabela', 'Almeida'),
            ('Rua 25 de Março, 707, Goiânia', 'João', 'Gomes'),
            ('Avenida Afonso Pena, 808, Belém', 'Juliana', 'Martins'),
            ('Rua da Praia, 909, Florianópolis', 'Lucas', 'Ribeiro'),
            ('Avenida Boa Viagem, 111, Natal', 'Mariana', 'Carvalho'),
            ('Rua das Laranjeiras, 222, Vitória', 'Nicolas', 'Melo'),
            ('Avenida Beira Mar, 333, São Luís', 'Olívia', 'Pinto'),
            ('Rua do Comércio, 444, Maceió', 'Pedro', 'Nunes'),
            ('Avenida Sete de Setembro, 555, Teresina', 'Quintino', 'Lima'),
            ('Rua da Aurora, 666, João Pessoa', 'Rafaela', 'Dias'),
            ('Avenida Agamenon Magalhães, 777, Aracaju', 'Sofia', 'Castro'),
            ('Rua XV de Novembro, 888, Campo Grande', 'Thiago', 'Moraes'),
            ('Avenida Getúlio Vargas, 999, Cuiabá', 'Úrsula', 'Neves'),
            ('Rua Direita, 100, Palmas', 'Victor', 'Pires'),
            ('Avenida dos Holandeses, 200, Porto Velho', 'Wanessa', 'Azevedo'),
            ('Rua do Imperador, 300, Boa Vista', 'Xavier', 'Barbosa'),
            ('Avenida Constantino Nery, 400, Rio Branco', 'Yasmin', 'Freitas'),
            ('Rua da Matriz, 500, Macapá', 'Ziraldo', 'Fernandes'),
            ('Alameda dos Anjos, 600, Brasília', 'Alice', 'Duarte'),
            ('Boulevard Castilhos França, 700, Teresópolis', 'Bento', 'Monteiro'),
            ('Caminho das Árvores, 800, Petrópolis', 'Cecília', 'Teixeira'),
            ('Doca de Souza Franco, 900, Niterói', 'Davi', 'Borges'),
            ('Estrada do Coco, 10, Lauro de Freitas', 'Elisa', 'Magalhães'),
            ('Fazenda Grande, 20, Simões Filho', 'Frederico', 'Lopes'),
            ('Gleba A, 30, Camaçari', 'Heloísa', 'Mendes'),
            ('Ilha dos Frades, 40, Itaparica', 'Igor', 'Nascimento'),
            ('Jardim das Margaridas, 50, Candeias', 'Júlia', 'Barros'),
            ('Largo do Pelourinho, 60, Ilhéus', 'Kléber', 'Correia'),
            ('Morro de São Paulo, 70, Valença', 'Lorena', 'Sampaio'),
            ('Nova Brasília, 80, Feira de Santana', 'Miguel', 'Andrade'),
            ('Ondina, 90, Itabuna', 'Natália', 'Brandão'),
            ('Pituba, 100, Jequié', 'Otávio', 'Cardoso'),
            ('Quadra 101, 110, Barreiras', 'Paula', 'Dantas'),
            ('Recanto das Emas, 120, Vitória da Conquista', 'Ricardo', 'Figueiredo'),
            ('Setor Hoteleiro Norte, 130, Porto Seguro', 'Sérgio', 'Guimarães'),
            ('Travessa dos Artistas, 140, Eunápolis', 'Tatiane', 'Jesus'),
            ('Vila dos Pescadores, 150, Teixeira de Freitas', 'Ulisses', 'Leal'),
            ('W3 Sul, 160, Alagoinhas', 'Valentina', 'Machado'),
            ('Zona Industrial, 170, Paulo Afonso', 'Wagner', 'Noronha'),
            ('Loteamento Aquarius, 180, Santo Antônio de Jesus', 'Yuri', 'Queiroz'),
            ('Parque da Cidade, 190, Jacobina', 'Zélia', 'Rocha'),
            ('Rua Chile, 200, Irecê', 'Antônio', 'Vasconcelos');

            -- Unidades
            INSERT INTO Unidade (localizacao, numero, nome, gerente_cpf) VALUES
            ('Rua das Flores, 100, São Paulo', '100', 'Matriz São Paulo', NULL),
            ('Avenida Brasil, 456, Rio de Janeiro', '456', 'Filial Rio de Janeiro', NULL),
            ('Praça da Sé, 789, Salvador', '789', 'Centro de Distribuição Salvador', NULL),
            ('Rua Augusta, 101, Curitiba', '101', 'Unidade Curitiba Centro', NULL),
            ('Avenida Paulista, 202, Belo Horizonte', '202', 'Escritório Belo Horizonte', NULL),
            ('Rua da Consolação, 303, Fortaleza', '303', 'Loja Fortaleza Beira Mar', NULL),
            ('Avenida Ipiranga, 404, Porto Alegre', '404', 'Depósito Porto Alegre', NULL),
            ('Rua Oscar Freire, 505, Recife', '505', 'Unidade Recife Jardins', NULL),
            ('Avenida Rio Branco, 606, Manaus', '606', 'Ponto de Coleta Manaus', NULL),
            ('Rua 25 de Março, 707, Goiânia', '707', 'Loja Goiânia Central', NULL),
            ('Avenida Afonso Pena, 808, Belém', '808', 'Unidade Belém Doca', NULL),
            ('Rua da Praia, 909, Florianópolis', '909', 'Escritório Florianópolis', NULL),
            ('Avenida Boa Viagem, 111, Natal', '111', 'Loja Natal Orla', NULL),
            ('Rua das Laranjeiras, 222, Vitória', '222', 'Unidade Vitória Centro', NULL),
            ('Avenida Beira Mar, 333, São Luís', '333', 'Filial São Luís', NULL),
            ('Rua do Comércio, 444, Maceió', '444', 'Ponto de Venda Maceió', NULL),
            ('Avenida Sete de Setembro, 555, Teresina', '555', 'Depósito Teresina', NULL),
            ('Rua da Aurora, 666, João Pessoa', '666', 'Loja João Pessoa', NULL),
            ('Avenida Agamenon Magalhães, 777, Aracaju', '777', 'Unidade Aracaju', NULL),
            ('Rua XV de Novembro, 888, Campo Grande', '888', 'Escritório Campo Grande', NULL),
            ('Avenida Getúlio Vargas, 999, Cuiabá', '999', 'Filial Cuiabá', NULL),
            ('Rua Direita, 100, Palmas', '100', 'Loja Palmas', NULL),
            ('Avenida dos Holandeses, 200, Porto Velho', '200', 'Unidade Porto Velho', NULL),
            ('Rua do Imperador, 300, Boa Vista', '300', 'Depósito Boa Vista', NULL),
            ('Avenida Constantino Nery, 400, Rio Branco', '400', 'Ponto de Coleta Rio Branco', NULL),
            ('Rua da Matriz, 500, Macapá', '500', 'Filial Macapá', NULL),
            ('Alameda dos Anjos, 600, Brasília', '600', 'Matriz Brasília', NULL),
            ('Boulevard Castilhos França, 700, Teresópolis', '700', 'Unidade Teresópolis', NULL),
            ('Caminho das Árvores, 800, Petrópolis', '800', 'Loja Petrópolis', NULL),
            ('Doca de Souza Franco, 900, Niterói', '900', 'Escritório Niterói', NULL),
            ('Estrada do Coco, 10, Lauro de Freitas', '10', 'Unidade Lauro de Freitas', NULL),
            ('Fazenda Grande, 20, Simões Filho', '20', 'Depósito Simões Filho', NULL),
            ('Gleba A, 30, Camaçari', '30', 'Filial Camaçari', NULL),
            ('Ilha dos Frades, 40, Itaparica', '40', 'Ponto de Venda Itaparica', NULL),
            ('Jardim das Margaridas, 50, Candeias', '50', 'Loja Candeias', NULL),
            ('Largo do Pelourinho, 60, Ilhéus', '60', 'Unidade Ilhéus', NULL),
            ('Morro de São Paulo, 70, Valença', '70', 'Depósito Valença', NULL),
            ('Nova Brasília, 80, Feira de Santana', '80', 'Filial Feira de Santana', NULL),
            ('Ondina, 90, Itabuna', '90', 'Ponto de Coleta Itabuna', NULL),
            ('Pituba, 100, Jequié', '100', 'Loja Jequié', NULL),
            ('Quadra 101, 110, Barreiras', '110', 'Unidade Barreiras', NULL),
            ('Recanto das Emas, 120, Vitória da Conquista', '120', 'Escritório Vitória da Conquista', NULL),
            ('Setor Hoteleiro Norte, 130, Porto Seguro', '130', 'Filial Porto Seguro', NULL),
            ('Travessa dos Artistas, 140, Eunápolis', '140', 'Loja Eunápolis', NULL),
            ('Vila dos Pescadores, 150, Teixeira de Freitas', '150', 'Unidade Teixeira de Freitas', NULL),
            ('W3 Sul, 160, Alagoinhas', '160', 'Depósito Alagoinhas', NULL),
            ('Zona Industrial, 170, Paulo Afonso', '170', 'Filial Paulo Afonso', NULL),
            ('Loteamento Aquarius, 180, Santo Antônio de Jesus', '180', 'Ponto de Venda Santo Antônio de Jesus', NULL),
            ('Parque da Cidade, 190, Jacobina', '190', 'Loja Jacobina', NULL),
            ('Rua Chile, 200, Irecê', '200', 'Unidade Irecê', NULL);

            -- Funcionários
            INSERT INTO Funcionario (cpf, salario, nome, cargo, unidade_localizacao, supervisor_cpf) VALUES
            ('111.111.111-01', 5000.00, 'Alexandre Silva', 'Gerente de Vendas', 'Rua das Flores, 100, São Paulo', NULL),
            ('111.111.111-02', 4500.00, 'Beatriz Santos', 'Analista Financeiro', 'Avenida Brasil, 456, Rio de Janeiro', '111.111.111-01'),
            ('111.111.111-03', 6000.00, 'Carlos Oliveira', 'Diretor de Operações', 'Praça da Sé, 789, Salvador', NULL),
            ('111.111.111-04', 3500.00, 'Diana Souza', 'Assistente Administrativo', 'Rua Augusta, 101, Curitiba', '111.111.111-03'),
            ('111.111.111-05', 5500.00, 'Eduardo Pereira', 'Gerente de Marketing', 'Avenida Paulista, 202, Belo Horizonte', NULL),
            ('111.111.111-06', 4000.00, 'Fernanda Costa', 'Desenvolvedor Júnior', 'Rua da Consolação, 303, Fortaleza', '111.111.111-05'),
            ('111.111.111-07', 7000.00, 'Gustavo Ferreira', 'Gerente de TI', 'Avenida Ipiranga, 404, Porto Alegre', NULL),
            ('111.111.111-08', 3000.00, 'Heloísa Rodrigues', 'Estagiária de RH', 'Rua Oscar Freire, 505, Recife', '111.111.111-07'),
            ('111.111.111-09', 5200.00, 'Igor Almeida', 'Coordenador de Logística', 'Avenida Rio Branco, 606, Manaus', NULL),
            ('111.111.111-10', 4800.00, 'Júlia Gomes', 'Analista de Suporte', 'Rua 25 de Março, 707, Goiânia', '111.111.111-09'),
            ('111.111.111-11', 6500.00, 'Kleber Martins', 'Gerente de Projetos', 'Avenida Afonso Pena, 808, Belém', NULL),
            ('111.111.111-12', 3800.00, 'Larissa Ribeiro', 'Técnica de Segurança', 'Rua da Praia, 909, Florianópolis', '111.111.111-11'),
            ('111.111.111-13', 5800.00, 'Marcelo Carvalho', 'Gerente de Filial', 'Avenida Boa Viagem, 111, Natal', NULL),
            ('111.111.111-14', 4200.00, 'Natália Melo', 'Vendedora Sênior', 'Rua das Laranjeiras, 222, Vitória', '111.111.111-13'),
            ('111.111.111-15', 7500.00, 'Otávio Pinto', 'Diretor Comercial', 'Avenida Beira Mar, 333, São Luís', NULL),
            ('111.111.111-16', 3200.00, 'Patrícia Nunes', 'Auxiliar de Limpeza', 'Rua do Comércio, 444, Maceió', '111.111.111-15'),
            ('111.111.111-17', 5100.00, 'Rafael Lima', 'Coordenador de Vendas', 'Avenida Sete de Setembro, 555, Teresina', NULL),
            ('111.111.111-18', 4600.00, 'Sofia Dias', 'Analista de RH', 'Rua da Aurora, 666, João Pessoa', '111.111.111-17'),
            ('111.111.111-19', 6200.00, 'Thiago Castro', 'Gerente de Produção', 'Avenida Agamenon Magalhães, 777, Aracaju', NULL),
            ('111.111.111-20', 3400.00, 'Úrsula Moraes', 'Operadora de Caixa', 'Rua XV de Novembro, 888, Campo Grande', '111.111.111-19'),
            ('111.111.111-21', 5300.00, 'Victor Neves', 'Supervisor de Estoque', 'Avenida Getúlio Vargas, 999, Cuiabá', NULL),
            ('111.111.111-22', 4100.00, 'Wanessa Pires', 'Motorista', 'Rua Direita, 100, Palmas', '111.111.111-21'),
            ('111.111.111-23', 7200.00, 'Xavier Azevedo', 'Diretor Financeiro', 'Avenida dos Holandeses, 200, Porto Velho', NULL),
            ('111.111.111-24', 3600.00, 'Yasmin Barbosa', 'Recepcionista', 'Rua do Imperador, 300, Boa Vista', '111.111.111-23'),
            ('111.111.111-25', 5900.00, 'Ziraldo Freitas', 'Gerente de Filial', 'Avenida Constantino Nery, 400, Rio Branco', NULL),
            ('111.111.111-26', 4300.00, 'Alice Fernandes', 'Analista de Qualidade', 'Rua da Matriz, 500, Macapá', '111.111.111-25'),
            ('111.111.111-27', 6800.00, 'Bento Duarte', 'Gerente Geral', 'Alameda dos Anjos, 600, Brasília', NULL),
            ('111.111.111-28', 3900.00, 'Cecília Monteiro', 'Assistente de Compras', 'Boulevard Castilhos França, 700, Teresópolis', '111.111.111-27'),
            ('111.111.111-29', 5400.00, 'Davi Teixeira', 'Coordenador de TI', 'Caminho das Árvores, 800, Petrópolis', NULL),
            ('111.111.111-30', 4700.00, 'Elisa Borges', 'Desenvolvedora Pleno', 'Doca de Souza Franco, 900, Niterói', '111.111.111-29'),
            ('111.111.111-31', 6100.00, 'Frederico Magalhães', 'Gerente de Vendas', 'Estrada do Coco, 10, Lauro de Freitas', NULL),
            ('111.111.111-32', 3300.00, 'Gabriela Lopes', 'Auxiliar de Escritório', 'Fazenda Grande, 20, Simões Filho', '111.111.111-31'),
            ('111.111.111-33', 5600.00, 'Heitor Mendes', 'Supervisor de Produção', 'Gleba A, 30, Camaçari', NULL),
            ('111.111.111-34', 4400.00, 'Isabela Nascimento', 'Técnica de Manutenção', 'Ilha dos Frades, 40, Itaparica', '111.111.111-33'),
            ('111.111.111-35', 7100.00, 'João Barros', 'Diretor de Marketing', 'Jardim das Margaridas, 50, Candeias', NULL),
            ('111.111.111-36', 3700.00, 'Juliana Correia', 'Vendedora Júnior', 'Largo do Pelourinho, 60, Ilhéus', '111.111.111-35'),
            ('111.111.111-37', 5000.00, 'Lucas Sampaio', 'Gerente de Filial', 'Morro de São Paulo, 70, Valença', NULL),
            ('111.111.111-38', 4500.00, 'Mariana Andrade', 'Analista de Sistemas', 'Nova Brasília, 80, Feira de Santana', '111.111.111-37'),
            ('111.111.111-39', 6000.00, 'Nicolas Brandão', 'Coordenador de RH', 'Ondina, 90, Itabuna', NULL),
            ('111.111.111-40', 3500.00, 'Olívia Cardoso', 'Assistente de Vendas', 'Pituba, 100, Jequié', '111.111.111-39'),
            ('111.111.111-41', 5500.00, 'Pedro Dantas', 'Gerente de Logística', 'Quadra 101, 110, Barreiras', NULL),
            ('111.111.111-42', 4000.00, 'Quintino Figueiredo', 'Motorista', 'Recanto das Emas, 120, Vitória da Conquista', '111.111.111-41'),
            ('111.111.111-43', 7000.00, 'Rafaela Guimarães', 'Diretora de Vendas', 'Setor Hoteleiro Norte, 130, Porto Seguro', NULL),
            ('111.111.111-44', 3000.00, 'Sérgio Jesus', 'Estagiário de Marketing', 'Travessa dos Artistas, 140, Eunápolis', '111.111.111-43'),
            ('111.111.111-45', 5200.00, 'Tatiane Leal', 'Supervisor de Qualidade', 'Vila dos Pescadores, 150, Teixeira de Freitas', NULL),
            ('111.111.111-46', 4800.00, 'Ulisses Machado', 'Analista Financeiro', 'W3 Sul, 160, Alagoinhas', '111.111.111-45'),
            ('111.111.111-47', 6500.00, 'Valentina Noronha', 'Gerente de TI', 'Zona Industrial, 170, Paulo Afonso', NULL),
            ('111.111.111-48', 3800.00, 'Wagner Queiroz', 'Técnico de Suporte', 'Loteamento Aquarius, 180, Santo Antônio de Jesus', '111.111.111-47'),
            ('111.111.111-49', 5800.00, 'Yuri Rocha', 'Gerente de Filial', 'Parque da Cidade, 190, Jacobina', NULL),
            ('111.111.111-50', 4200.00, 'Zélia Vasconcelos', 'Vendedora Pleno', 'Rua Chile, 200, Irecê', '111.111.111-49');

            -- Dependentes
            INSERT INTO Dependente (nome, idade, parentesco, funcionario_cpf) VALUES
            ('Pedro Silva', 10, 'Filho', '111.111.111-01'),
            ('Maria Silva', 8, 'Filha', '111.111.111-01'),
            ('João Santos', 15, 'Filho', '111.111.111-02'),
            ('Laura Oliveira', 5, 'Filha', '111.111.111-03'),
            ('Ricardo Souza', 12, 'Filho', '111.111.111-04'),
            ('Alice Pereira', 18, 'Filha', '111.111.111-05'),
            ('Gabriel Costa', 7, 'Filho', '111.111.111-06'),
            ('Sofia Ferreira', 14, 'Filha', '111.111.111-07'),
            ('Lucas Rodrigues', 9, 'Filho', '111.111.111-08'),
            ('Isadora Almeida', 11, 'Filha', '111.111.111-09'),
            ('Felipe Gomes', 6, 'Filho', '111.111.111-10'),
            ('Manuela Martins', 16, 'Filha', '111.111.111-11'),
            ('Davi Ribeiro', 4, 'Filho', '111.111.111-12'),
            ('Helena Carvalho', 13, 'Filha', '111.111.111-13'),
            ('Arthur Melo', 17, 'Filho', '111.111.111-14'),
            ('Valentina Pinto', 3, 'Filha', '111.111.111-15'),
            ('Guilherme Nunes', 19, 'Filho', '111.111.111-16'),
            ('Lívia Lima', 2, 'Filha', '111.111.111-17'),
            ('Enzo Dias', 20, 'Filho', '111.111.111-18'),
            ('Maitê Castro', 1, 'Filha', '111.111.111-19'),
            ('Benício Moraes', 21, 'Filho', '111.111.111-20'),
            ('Eloá Neves', 22, 'Filha', '111.111.111-21'),
            ('Theo Pires', 23, 'Filho', '111.111.111-22'),
            ('Júlia Azevedo', 24, 'Filha', '111.111.111-23'),
            ('Miguel Barbosa', 25, 'Filho', '111.111.111-24'),
            ('Luiza Freitas', 26, 'Filha', '111.111.111-25'),
            ('Heitor Fernandes', 27, 'Filho', '111.111.111-26'),
            ('Cecília Duarte', 28, 'Filha', '111.111.111-27'),
            ('Rafael Monteiro', 29, 'Filho', '111.111.111-28'),
            ('Lara Teixeira', 30, 'Filha', '111.111.111-29'),
            ('Bernardo Borges', 31, 'Filho', '111.111.111-30'),
            ('Isabella Magalhães', 32, 'Filha', '111.111.111-31'),
            ('Lorenzo Lopes', 33, 'Filho', '111.111.111-32'),
            ('Helena Mendes', 34, 'Filha', '111.111.111-33'),
            ('Matheus Nascimento', 35, 'Filho', '111.111.111-34'),
            ('Maria Barros', 36, 'Filha', '111.111.111-35'),
            ('Enzo Correia', 37, 'Filho', '111.111.111-36'),
            ('Alice Sampaio', 38, 'Filha', '111.111.111-37'),
            ('João Andrade', 39, 'Filho', '111.111.111-38'),
            ('Laura Brandão', 40, 'Filha', '111.111.111-39'),
            ('Guilherme Cardoso', 41, 'Filho', '111.111.111-40'),
            ('Sofia Dantas', 42, 'Filha', '111.111.111-41'),
            ('Lucas Figueiredo', 43, 'Filho', '111.111.111-42'),
            ('Isabela Guimarães', 44, 'Filha', '111.111.111-43'),
            ('Felipe Jesus', 45, 'Filho', '111.111.111-44'),
            ('Manuela Leal', 46, 'Filha', '111.111.111-45'),
            ('Davi Machado', 47, 'Filho', '111.111.111-46'),
            ('Helena Noronha', 48, 'Filha', '111.111.111-47'),
            ('Arthur Queiroz', 49, 'Filho', '111.111.111-48'),
            ('Valentina Rocha', 50, 'Filha', '111.111.111-49');

            -- Produtos
            INSERT INTO Produto (nome, descricao, categoria, preco, estoque, status, usuario_id, texto_anunciado) VALUES
            ('Smartphone Galaxy S21', 'Tela AMOLED, 128GB, Câmera 64MP.', 'Eletrônicos', 3500.00, 150, 'Disponível', 1, 'O melhor smartphone do ano!'),
            ('Notebook Dell Inspiron', 'Core i7, 16GB RAM, SSD 512GB.', 'Eletrônicos', 5200.00, 80, 'Disponível', 2, 'Potência e performance para o seu dia.'),
            ('Smart TV LED 50 polegadas', '4K, HDR, Smart Hub.', 'Eletrônicos', 2800.00, 120, 'Disponível', 3, 'Cinema em casa com qualidade superior.'),
            ('Fone de Ouvido Bluetooth', 'Cancelamento de ruído, 30h de bateria.', 'Eletrônicos', 450.00, 300, 'Disponível', 4, 'Música sem interrupções.'),
            ('Câmera DSLR Canon EOS', '24MP, Vídeo 4K, Lente 18-55mm.', 'Eletrônicos', 4800.00, 50, 'Disponível', 5, 'Capture momentos inesquecíveis.'),
            ('Console PlayStation 5', 'SSD ultra-rápido, Ray Tracing.', 'Games', 4999.90, 40, 'Disponível', 6, 'A nova geração de jogos.'),
            ('Jogo FIFA 24', 'Simulador de futebol, edição 2024.', 'Games', 300.00, 500, 'Disponível', 7, 'O jogo mais vendido do ano.'),
            ('Controle Sem Fio Xbox', 'Design ergonômico, bateria de longa duração.', 'Games', 350.00, 250, 'Disponível', 8, 'Precisão e conforto.'),
            ('Mesa de Escritório em L', 'MDF, 1.50m x 1.50m.', 'Móveis', 950.00, 60, 'Disponível', 9, 'Organização e espaço para o trabalho.'),
            ('Cadeira Gamer Ergonômica', 'Apoio lombar, ajuste de altura.', 'Móveis', 1200.00, 90, 'Disponível', 10, 'Conforto para longas sessões.'),
            ('Armário de Cozinha Modulado', '6 portas, 2 gavetas, branco.', 'Móveis', 1800.00, 30, 'Disponível', 11, 'Sua cozinha completa e organizada.'),
            ('Geladeira Frost Free Inox', '400 Litros, Dispenser de água.', 'Eletrodomésticos', 3200.00, 70, 'Disponível', 12, 'Tecnologia e economia.'),
            ('Máquina de Lavar Roupas', '12kg, 15 programas de lavagem.', 'Eletrodomésticos', 1500.00, 100, 'Disponível', 13, 'Roupas limpas e bem cuidadas.'),
            ('Micro-ondas Espelhado', '30 Litros, Função Grill.', 'Eletrodomésticos', 400.00, 200, 'Disponível', 14, 'Praticidade na sua cozinha.'),
            ('Liquidificador Turbo', '1000W, Copo de vidro.', 'Eletrodomésticos', 150.00, 400, 'Disponível', 15, 'Receitas deliciosas em segundos.'),
            ('Ventilador de Coluna', '40cm, 3 velocidades, oscilante.', 'Eletrodomésticos', 120.00, 600, 'Disponível', 16, 'Refresque seu ambiente.'),
            ('Ar Condicionado Split Inverter', '12000 BTUs, Quente/Frio.', 'Eletrodomésticos', 2500.00, 50, 'Disponível', 17, 'Conforto térmico o ano todo.'),
            ('Panela de Pressão Elétrica', '5 Litros, 10 receitas pré-programadas.', 'Eletrodomésticos', 350.00, 150, 'Disponível', 18, 'Cozinhe com segurança e rapidez.'),
            ('Cafeteira Expresso Automática', 'Moedor integrado, 15 Bar.', 'Eletrodomésticos', 1800.00, 40, 'Disponível', 19, 'Café fresco e saboroso.'),
            ('Bicicleta Mountain Bike', 'Aro 29, 21 marchas, freio a disco.', 'Esportes', 1100.00, 80, 'Disponível', 20, 'Aventura e exercício.'),
            ('Tênis de Corrida Nike Air', 'Amortecimento responsivo, leve.', 'Esportes', 550.00, 200, 'Disponível', 21, 'Melhore sua performance.'),
            ('Halteres de Ferro Fundido', 'Kit 10kg, revestimento emborrachado.', 'Esportes', 180.00, 300, 'Disponível', 22, 'Treino de força em casa.'),
            ('Tapete de Yoga Antiderrapante', '6mm, material ecológico.', 'Esportes', 80.00, 500, 'Disponível', 23, 'Pratique com conforto.'),
            ('Livro "A Revolução dos Bichos"', 'Clássico da literatura, George Orwell.', 'Livros', 35.00, 800, 'Disponível', 24, 'Uma sátira atemporal.'),
            ('Kindle Paperwhite', 'Tela antirreflexo, à prova d''água.', 'Eletrônicos', 650.00, 120, 'Disponível', 25, 'Milhares de livros na palma da mão.'),
            ('Mochila para Notebook', 'Resistente à água, compartimento acolchoado.', 'Acessórios', 150.00, 400, 'Disponível', 26, 'Proteção e estilo.'),
            ('Relógio Smartwatch Samsung', 'Monitor de saúde, GPS, bateria de 7 dias.', 'Eletrônicos', 850.00, 180, 'Disponível', 27, 'Conectividade no seu pulso.'),
            ('Drone DJI Mini 3 Pro', 'Câmera 4K, 34 minutos de voo.', 'Eletrônicos', 6500.00, 20, 'Disponível', 28, 'Imagens aéreas profissionais.'),
            ('Impressora Multifuncional Epson', 'Tanque de tinta, Wi-Fi, imprime, copia e digitaliza.', 'Eletrônicos', 900.00, 100, 'Disponível', 29, 'Economia e qualidade de impressão.'),
            ('Teclado Mecânico Gamer', 'Switch Blue, RGB, layout ABNT2.', 'Eletrônicos', 380.00, 250, 'Disponível', 30, 'Resposta tátil e sonora.'),
            ('Mouse Sem Fio Logitech', 'Ergonômico, 1000 DPI.', 'Eletrônicos', 95.00, 500, 'Disponível', 31, 'Conforto e precisão.'),
            ('Webcam Full HD', 'Microfone embutido, correção de luz.', 'Eletrônicos', 180.00, 350, 'Disponível', 32, 'Videochamadas com clareza.'),
            ('Headset Gamer HyperX', 'Som surround 7.1, microfone com cancelamento de ruído.', 'Eletrônicos', 400.00, 200, 'Disponível', 33, 'Imersão total no jogo.'),
            ('Placa de Vídeo RTX 4060', '8GB GDDR6, Ray Tracing.', 'Eletrônicos', 2500.00, 50, 'Disponível', 34, 'Gráficos de última geração.'),
            ('Memória RAM DDR4 16GB', '3200MHz, dissipador de calor.', 'Eletrônicos', 320.00, 150, 'Disponível', 35, 'Acelere seu computador.'),
            ('SSD NVMe 1TB', 'Velocidade de leitura 3500MB/s.', 'Eletrônicos', 450.00, 200, 'Disponível', 36, 'Carregamento ultra-rápido.'),
            ('Roteador Wi-Fi 6', 'Dual Band, 1800Mbps.', 'Eletrônicos', 300.00, 100, 'Disponível', 37, 'Conexão estável e rápida.'),
            ('Caixa de Som Bluetooth JBL', 'À prova d''água, 10h de bateria.', 'Eletrônicos', 550.00, 150, 'Disponível', 38, 'Som potente em qualquer lugar.'),
            ('Power Bank 10000mAh', 'Carregamento rápido, 2 portas USB.', 'Eletrônicos', 120.00, 400, 'Disponível', 39, 'Energia extra para seus dispositivos.'),
            ('Cabo HDMI 4K', '2 metros, alta velocidade.', 'Eletrônicos', 40.00, 600, 'Disponível', 40, 'Conexão de imagem e som.'),
            ('Adaptador USB-C para HDMI', 'Para notebooks e tablets.', 'Eletrônicos', 70.00, 350, 'Disponível', 41, 'Conecte a monitores externos.'),
            ('Mousepad Gamer Grande', 'Superfície speed, base antiderrapante.', 'Eletrônicos', 60.00, 480, 'Disponível', 42, 'Movimentos precisos.'),
            ('Livro "Sapiens"', 'História da humanidade.', 'Livros', 65.00, 400, 'Disponível', 43, 'Uma visão fascinante.'),
            ('Agenda 2024 Executiva', 'Capa de couro, planejamento semanal.', 'Papelaria', 40.00, 700, 'Disponível', 44, 'Organize seu ano.'),
            ('Caneta Tinteiro Profissional', 'Ponta média, escrita suave.', 'Papelaria', 150.00, 120, 'Disponível', 45, 'Para uma escrita elegante.'),
            ('Mochila de Hidratação', '2L, ideal para trilhas e ciclismo.', 'Esportes', 130.00, 200, 'Disponível', 46, 'Mantenha-se hidratado.'),
            ('Luvas de Academia', 'Couro, proteção para as mãos.', 'Esportes', 70.00, 300, 'Disponível', 47, 'Treine com segurança.'),
            ('Barra Fixa de Porta', 'Fácil instalação, suporta até 120kg.', 'Esportes', 110.00, 150, 'Disponível', 48, 'Treino de costas em casa.'),
            ('Piscina Inflável 1000L', 'Fácil de montar e desmontar.', 'Lazer', 250.00, 90, 'Disponível', 49, 'Diversão garantida no verão.'),
            ('Jogo de Cartas UNO', 'Clássico para toda a família.', 'Lazer', 25.00, 800, 'Disponível', 50, 'Noites de jogos.');

            -- Pedidos
            INSERT INTO Pedidos (codigo, status, valor_total, comprador_id, vendedor_id, meio_de_pagamento, numero_de_parcelas, destino_localizacao, origem_localizacao, o_flag_chegada, o_data, d_flag_chegada, d_data, funcionario_cpf, placa_do_veiculo, data_de_entrega, previsao_de_entrega) VALUES
            (1, 'Em Transporte', 4500.00, 2, 1, 'Cartão de Crédito', 3, 'Avenida Brasil, 456, Rio de Janeiro', 'Rua das Flores, 100, São Paulo', TRUE, '2025-11-20 10:00:00', FALSE, NULL, '111.111.111-13', 'ABC-1234', NULL, '2025-11-25'),
            (2, 'Pendente', 8999.90, 1, 3, 'Boleto', 1, 'Rua das Flores, 100, São Paulo', 'Praça da Sé, 789, Salvador', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-11-28'),
            (3, 'Entregue', 1250.00, 4, 5, 'PIX', 1, 'Rua Augusta, 101, Curitiba', 'Avenida Paulista, 202, Belo Horizonte', TRUE, '2025-11-15 09:00:00', TRUE, '2025-11-18 14:30:00', '111.111.111-14', 'DEF-5678', '2025-11-18', '2025-11-19'),
            (4, 'Cancelado', 2800.00, 5, 6, 'Cartão de Débito', 1, 'Avenida Paulista, 202, Belo Horizonte', 'Rua da Consolação, 303, Fortaleza', TRUE, '2025-11-10 11:00:00', FALSE, NULL, NULL, NULL, NULL, '2025-11-15'),
            (5, 'Em Processamento', 950.00, 6, 7, 'Cartão de Crédito', 2, 'Rua da Consolação, 303, Fortaleza', 'Avenida Ipiranga, 404, Porto Alegre', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-01'),
            (6, 'Em Transporte', 350.00, 7, 8, 'PIX', 1, 'Avenida Ipiranga, 404, Porto Alegre', 'Rua Oscar Freire, 505, Recife', TRUE, '2025-11-21 15:00:00', FALSE, NULL, '111.111.111-17', 'GHI-9012', NULL, '2025-11-26'),
            (7, 'Entregue', 180.00, 8, 9, 'Boleto', 1, 'Rua Oscar Freire, 505, Recife', 'Avenida Rio Branco, 606, Manaus', TRUE, '2025-11-12 10:30:00', TRUE, '2025-11-16 11:00:00', '111.111.111-18', 'JKL-3456', '2025-11-16', '2025-11-17'),
            (8, 'Pendente', 3500.00, 9, 10, 'Cartão de Crédito', 5, 'Avenida Rio Branco, 606, Manaus', 'Rua 25 de Março, 707, Goiânia', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-05'),
            (9, 'Em Processamento', 4200.00, 10, 11, 'PIX', 1, 'Rua 25 de Março, 707, Goiânia', 'Avenida Afonso Pena, 808, Belém', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-02'),
            (10, 'Em Transporte', 1100.00, 11, 12, 'Cartão de Débito', 1, 'Avenida Afonso Pena, 808, Belém', 'Rua da Praia, 909, Florianópolis', TRUE, '2025-11-22 14:00:00', FALSE, NULL, '111.111.111-39', 'MNO-7890', NULL, '2025-11-27'),
            (11, 'Entregue', 2300.00, 12, 13, 'Cartão de Crédito', 4, 'Rua da Praia, 909, Florianópolis', 'Avenida Boa Viagem, 111, Natal', TRUE, '2025-11-14 16:00:00', TRUE, '2025-11-19 10:00:00', '111.111.111-40', 'PQR-1234', '2025-11-19', '2025-11-20'),
            (12, 'Pendente', 1500.00, 13, 14, 'Boleto', 1, 'Avenida Boa Viagem, 111, Natal', 'Rua das Laranjeiras, 222, Vitória', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-08'),
            (13, 'Em Processamento', 1800.00, 14, 15, 'PIX', 1, 'Rua das Laranjeiras, 222, Vitória', 'Avenida Beira Mar, 333, São Luís', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-03'),
            (14, 'Em Transporte', 650.00, 15, 16, 'Cartão de Crédito', 1, 'Avenida Beira Mar, 333, São Luís', 'Rua do Comércio, 444, Maceió', TRUE, '2025-11-23 08:00:00', FALSE, NULL, '111.111.111-13', 'STU-5678', NULL, '2025-11-29'),
            (15, 'Entregue', 850.00, 16, 17, 'Cartão de Débito', 1, 'Rua do Comércio, 444, Maceió', 'Avenida Sete de Setembro, 555, Teresina', TRUE, '2025-11-11 13:00:00', TRUE, '2025-11-15 15:00:00', '111.111.111-14', 'VWX-9012', '2025-11-15', '2025-11-16'),
            (16, 'Pendente', 45.00, 17, 18, 'PIX', 1, 'Avenida Sete de Setembro, 555, Teresina', 'Rua da Aurora, 666, João Pessoa', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-10'),
            (17, 'Em Processamento', 29.90, 18, 19, 'Boleto', 1, 'Rua da Aurora, 666, João Pessoa', 'Avenida Agamenon Magalhães, 777, Aracaju', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-04'),
            (18, 'Em Transporte', 7500.00, 19, 20, 'Cartão de Crédito', 6, 'Avenida Agamenon Magalhães, 777, Aracaju', 'Rua XV de Novembro, 888, Campo Grande', TRUE, '2025-11-24 12:00:00', FALSE, NULL, '111.111.111-17', 'YZA-3456', NULL, '2025-11-30'),
            (19, 'Entregue', 5200.00, 20, 21, 'PIX', 1, 'Rua XV de Novembro, 888, Campo Grande', 'Avenida Getúlio Vargas, 999, Cuiabá', TRUE, '2025-11-13 14:00:00', TRUE, '2025-11-17 17:00:00', '111.111.111-18', 'BCD-7890', '2025-11-17', '2025-11-18'),
            (20, 'Pendente', 900.00, 21, 22, 'Cartão de Débito', 1, 'Avenida Getúlio Vargas, 999, Cuiabá', 'Rua Direita, 100, Palmas', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-12'),
            (21, 'Em Processamento', 160.00, 22, 23, 'Cartão de Crédito', 1, 'Rua Direita, 100, Palmas', 'Avenida dos Holandeses, 200, Porto Velho', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-05'),
            (22, 'Em Transporte', 1900.00, 23, 24, 'PIX', 1, 'Avenida dos Holandeses, 200, Porto Velho', 'Rua do Imperador, 300, Boa Vista', TRUE, '2025-11-25 09:30:00', FALSE, NULL, '111.111.111-39', 'EFG-1234', NULL, '2025-12-01'),
            (23, 'Entregue', 450.00, 24, 25, 'Boleto', 1, 'Rua do Imperador, 300, Boa Vista', 'Avenida Constantino Nery, 400, Rio Branco', TRUE, '2025-11-16 11:30:00', TRUE, '2025-11-20 13:00:00', '111.111.111-40', 'HIJ-5678', '2025-11-20', '2025-11-21'),
            (24, 'Pendente', 1100.00, 25, 26, 'Cartão de Crédito', 3, 'Avenida Constantino Nery, 400, Rio Branco', 'Rua da Matriz, 500, Macapá', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-15'),
            (25, 'Em Processamento', 780.00, 26, 27, 'PIX', 1, 'Rua da Matriz, 500, Macapá', 'Alameda dos Anjos, 600, Brasília', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-06'),
            (26, 'Em Transporte', 800.00, 27, 28, 'Cartão de Débito', 1, 'Alameda dos Anjos, 600, Brasília', 'Boulevard Castilhos França, 700, Teresópolis', TRUE, '2025-11-26 10:00:00', FALSE, NULL, '111.111.111-13', 'KLM-9012', NULL, '2025-12-02'),
            (27, 'Entregue', 320.00, 28, 29, 'Cartão de Crédito', 1, 'Boulevard Castilhos França, 700, Teresópolis', 'Caminho das Árvores, 800, Petrópolis', TRUE, '2025-11-17 15:30:00', TRUE, '2025-11-21 16:00:00', '111.111.111-14', 'NOP-3456', '2025-11-21', '2025-11-22'),
            (28, 'Pendente', 550.00, 29, 30, 'Boleto', 1, 'Caminho das Árvores, 800, Petrópolis', 'Doca de Souza Franco, 900, Niterói', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-18'),
            (29, 'Em Processamento', 700.00, 30, 31, 'PIX', 1, 'Doca de Souza Franco, 900, Niterói', 'Estrada do Coco, 10, Lauro de Freitas', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-07'),
            (30, 'Em Transporte', 3800.00, 31, 32, 'Cartão de Crédito', 2, 'Estrada do Coco, 10, Lauro de Freitas', 'Fazenda Grande, 20, Simões Filho', TRUE, '2025-11-27 11:00:00', FALSE, NULL, '111.111.111-17', 'QRS-7890', NULL, '2025-12-03'),
            (31, 'Entregue', 1999.00, 32, 33, 'Boleto', 1, 'Fazenda Grande, 20, Simões Filho', 'Gleba A, 30, Camaçari', TRUE, '2025-11-18 14:00:00', TRUE, '2025-11-22 15:00:00', '111.111.111-18', 'TUV-1234', '2025-11-22', '2025-11-23'),
            (32, 'Pendente', 300.00, 33, 34, 'PIX', 1, 'Gleba A, 30, Camaçari', 'Ilha dos Frades, 40, Itaparica', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-20'),
            (33, 'Em Processamento', 1800.00, 34, 35, 'Cartão de Débito', 1, 'Ilha dos Frades, 40, Itaparica', 'Jardim das Margaridas, 50, Candeias', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-08'),
            (34, 'Em Transporte', 480.00, 35, 36, 'Cartão de Crédito', 1, 'Jardim das Margaridas, 50, Candeias', 'Largo do Pelourinho, 60, Ilhéus', TRUE, '2025-11-28 13:00:00', FALSE, NULL, '111.111.111-39', 'WXY-5678', NULL, '2025-12-04'),
            (35, 'Entregue', 320.00, 36, 37, 'PIX', 1, 'Largo do Pelourinho, 60, Ilhéus', 'Morro de São Paulo, 70, Valença', TRUE, '2025-11-19 16:30:00', TRUE, '2025-11-23 17:00:00', '111.111.111-40', 'ZAB-9012', '2025-11-23', '2025-11-24'),
            (36, 'Pendente', 550.00, 37, 38, 'Boleto', 1, 'Morro de São Paulo, 70, Valença', 'Nova Brasília, 80, Feira de Santana', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-22'),
            (37, 'Em Processamento', 35.00, 38, 39, 'Cartão de Crédito', 1, 'Nova Brasília, 80, Feira de Santana', 'Ondina, 90, Itabuna', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-09'),
            (38, 'Em Transporte', 80.00, 39, 40, 'PIX', 1, 'Ondina, 90, Itabuna', 'Pituba, 100, Jequié', TRUE, '2025-11-29 10:00:00', FALSE, NULL, '111.111.111-13', 'CDE-3456', NULL, '2025-12-05'),
            (39, 'Entregue', 580.00, 40, 41, 'Cartão de Débito', 1, 'Pituba, 100, Jequié', 'Quadra 101, 110, Barreiras', TRUE, '2025-11-20 12:30:00', TRUE, '2025-11-24 14:00:00', '111.111.111-14', 'FGH-7890', '2025-11-24', '2025-11-25'),
            (40, 'Pendente', 150.00, 41, 42, 'Cartão de Crédito', 1, 'Quadra 101, 110, Barreiras', 'Recanto das Emas, 120, Vitória da Conquista', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-24'),
            (41, 'Em Processamento', 400.00, 42, 43, 'Boleto', 1, 'Recanto das Emas, 120, Vitória da Conquista', 'Setor Hoteleiro Norte, 130, Porto Seguro', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-10'),
            (42, 'Em Transporte', 75.00, 43, 44, 'PIX', 1, 'Setor Hoteleiro Norte, 130, Porto Seguro', 'Travessa dos Artistas, 140, Eunápolis', TRUE, '2025-11-30 15:00:00', FALSE, NULL, '111.111.111-17', 'IJK-1234', NULL, '2025-12-06'),
            (43, 'Entregue', 150.00, 44, 45, 'Cartão de Crédito', 1, 'Travessa dos Artistas, 140, Eunápolis', 'Vila dos Pescadores, 150, Teixeira de Freitas', TRUE, '2025-11-21 17:30:00', TRUE, '2025-11-25 18:00:00', '111.111.111-18', 'LMN-5678', '2025-11-25', '2025-11-26'),
            (44, 'Pendente', 30.00, 45, 46, 'Cartão de Débito', 1, 'Vila dos Pescadores, 150, Teixeira de Freitas', 'W3 Sul, 160, Alagoinhas', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-26'),
            (45, 'Em Processamento', 120.00, 46, 47, 'PIX', 1, 'W3 Sul, 160, Alagoinhas', 'Zona Industrial, 170, Paulo Afonso', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-11'),
            (46, 'Em Transporte', 6500.00, 47, 48, 'Cartão de Crédito', 10, 'Zona Industrial, 170, Paulo Afonso', 'Loteamento Aquarius, 180, Santo Antônio de Jesus', TRUE, '2025-12-01 11:00:00', FALSE, NULL, '111.111.111-39', 'OPQ-9012', NULL, '2025-12-07'),
            (47, 'Entregue', 1500.00, 48, 49, 'Boleto', 1, 'Loteamento Aquarius, 180, Santo Antônio de Jesus', 'Parque da Cidade, 190, Jacobina', TRUE, '2025-11-22 13:30:00', TRUE, '2025-11-26 15:00:00', '111.111.111-40', 'RST-3456', '2025-11-26', '2025-11-27'),
            (48, 'Pendente', 950.00, 49, 50, 'PIX', 1, 'Parque da Cidade, 190, Jacobina', 'Rua Chile, 200, Irecê', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-28'),
            (49, 'Em Processamento', 150.00, 50, 1, 'Cartão de Crédito', 1, 'Rua Chile, 200, Irecê', 'Rua das Flores, 100, São Paulo', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-12'),
            (50, 'Em Transporte', 100.00, 1, 2, 'PIX', 1, 'Rua Chile, 200, Irecê', 'Avenida Brasil, 456, Rio de Janeiro', FALSE, NULL, FALSE, NULL, NULL, NULL, NULL, '2025-12-15');

            SELECT setval('pedidos_codigo_seq', 50, true);

            -- Produto_Contem_Pedidos
            INSERT INTO Produto_Contem_Pedidos (produto_id, pedido_codigo, quantidade) VALUES
            (1, 1, 1), (2, 2, 1), (3, 3, 2), (4, 4, 1), (5, 5, 3), (6, 6, 5), (7, 7, 1), (8, 8, 1), (9, 9, 1), (10, 10, 1),
            (11, 11, 1), (12, 12, 1), (13, 13, 1), (14, 14, 2), (15, 15, 1), (16, 16, 10), (17, 17, 1), (18, 18, 1), (19, 19, 1), (20, 20, 2),
            (21, 21, 4), (22, 22, 1), (23, 23, 1), (24, 24, 1), (25, 25, 1), (26, 26, 2), (27, 27, 1), (28, 28, 1), (29, 29, 1), (30, 30, 5),
            (31, 31, 1), (32, 32, 1), (33, 33, 2), (34, 34, 1), (35, 35, 3), (36, 36, 5), (37, 37, 1), (38, 38, 1), (39, 39, 1), (40, 40, 1),
            (41, 41, 1), (42, 42, 1), (43, 43, 1), (44, 44, 2), (45, 45, 1), (46, 46, 10), (47, 47, 1), (48, 48, 1), (49, 49, 1), (50, 50, 2);

            -- Atualização de Gerentes
            UPDATE Unidade SET gerente_cpf = '111.111.111-01' WHERE localizacao = 'Rua das Flores, 100, São Paulo';
            UPDATE Unidade SET gerente_cpf = '111.111.111-03' WHERE localizacao = 'Avenida Brasil, 456, Rio de Janeiro';
            UPDATE Unidade SET gerente_cpf = '111.111.111-05' WHERE localizacao = 'Praça da Sé, 789, Salvador';
            UPDATE Unidade SET gerente_cpf = '111.111.111-07' WHERE localizacao = 'Rua Augusta, 101, Curitiba';
            UPDATE Unidade SET gerente_cpf = '111.111.111-09' WHERE localizacao = 'Avenida Paulista, 202, Belo Horizonte';
            UPDATE Unidade SET gerente_cpf = '111.111.111-11' WHERE localizacao = 'Rua da Consolação, 303, Fortaleza';
            UPDATE Unidade SET gerente_cpf = '111.111.111-13' WHERE localizacao = 'Avenida Ipiranga, 404, Porto Alegre';
            UPDATE Unidade SET gerente_cpf = '111.111.111-15' WHERE localizacao = 'Rua Oscar Freire, 505, Recife';
            UPDATE Unidade SET gerente_cpf = '111.111.111-17' WHERE localizacao = 'Avenida Rio Branco, 606, Manaus';
            UPDATE Unidade SET gerente_cpf = '111.111.111-19' WHERE localizacao = 'Rua 25 de Março, 707, Goiânia';
            UPDATE Unidade SET gerente_cpf = '111.111.111-21' WHERE localizacao = 'Avenida Afonso Pena, 808, Belém';
            UPDATE Unidade SET gerente_cpf = '111.111.111-23' WHERE localizacao = 'Rua da Praia, 909, Florianópolis';
            UPDATE Unidade SET gerente_cpf = '111.111.111-25' WHERE localizacao = 'Avenida Boa Viagem, 111, Natal';
            UPDATE Unidade SET gerente_cpf = '111.111.111-27' WHERE localizacao = 'Rua das Laranjeiras, 222, Vitória';
            UPDATE Unidade SET gerente_cpf = '111.111.111-29' WHERE localizacao = 'Avenida Beira Mar, 333, São Luís';
            UPDATE Unidade SET gerente_cpf = '111.111.111-31' WHERE localizacao = 'Rua do Comércio, 444, Maceió';
            UPDATE Unidade SET gerente_cpf = '111.111.111-33' WHERE localizacao = 'Avenida Sete de Setembro, 555, Teresina';
            UPDATE Unidade SET gerente_cpf = '111.111.111-35' WHERE localizacao = 'Rua da Aurora, 666, João Pessoa';
            UPDATE Unidade SET gerente_cpf = '111.111.111-37' WHERE localizacao = 'Avenida Agamenon Magalhães, 777, Aracaju';
            UPDATE Unidade SET gerente_cpf = '111.111.111-39' WHERE localizacao = 'Rua XV de Novembro, 888, Campo Grande';
            UPDATE Unidade SET gerente_cpf = '111.111.111-41' WHERE localizacao = 'Avenida Getúlio Vargas, 999, Cuiabá';
            UPDATE Unidade SET gerente_cpf = '111.111.111-43' WHERE localizacao = 'Rua Direita, 100, Palmas';
            UPDATE Unidade SET gerente_cpf = '111.111.111-45' WHERE localizacao = 'Avenida dos Holandeses, 200, Porto Velho';
            UPDATE Unidade SET gerente_cpf = '111.111.111-47' WHERE localizacao = 'Rua do Imperador, 300, Boa Vista';
            UPDATE Unidade SET gerente_cpf = '111.111.111-49' WHERE localizacao = 'Avenida Constantino Nery, 400, Rio Branco';
            UPDATE Unidade SET gerente_cpf = '111.111.111-02' WHERE localizacao = 'Rua da Matriz, 500, Macapá';
            UPDATE Unidade SET gerente_cpf = '111.111.111-04' WHERE localizacao = 'Alameda dos Anjos, 600, Brasília';
            UPDATE Unidade SET gerente_cpf = '111.111.111-06' WHERE localizacao = 'Boulevard Castilhos França, 700, Teresópolis';
            UPDATE Unidade SET gerente_cpf = '111.111.111-08' WHERE localizacao = 'Caminho das Árvores, 800, Petrópolis';
            UPDATE Unidade SET gerente_cpf = '111.111.111-10' WHERE localizacao = 'Doca de Souza Franco, 900, Niterói';
            UPDATE Unidade SET gerente_cpf = '111.111.111-12' WHERE localizacao = 'Estrada do Coco, 10, Lauro de Freitas';
            UPDATE Unidade SET gerente_cpf = '111.111.111-14' WHERE localizacao = 'Fazenda Grande, 20, Simões Filho';
            UPDATE Unidade SET gerente_cpf = '111.111.111-16' WHERE localizacao = 'Gleba A, 30, Camaçari';
            UPDATE Unidade SET gerente_cpf = '111.111.111-18' WHERE localizacao = 'Ilha dos Frades, 40, Itaparica';
            UPDATE Unidade SET gerente_cpf = '111.111.111-20' WHERE localizacao = 'Jardim das Margaridas, 50, Candeias';
            UPDATE Unidade SET gerente_cpf = '111.111.111-22' WHERE localizacao = 'Largo do Pelourinho, 60, Ilhéus';
            UPDATE Unidade SET gerente_cpf = '111.111.111-24' WHERE localizacao = 'Morro de São Paulo, 70, Valença';
            UPDATE Unidade SET gerente_cpf = '111.111.111-26' WHERE localizacao = 'Nova Brasília, 80, Feira de Santana';
            UPDATE Unidade SET gerente_cpf = '111.111.111-28' WHERE localizacao = 'Ondina, 90, Itabuna';
            UPDATE Unidade SET gerente_cpf = '111.111.111-30' WHERE localizacao = 'Pituba, 100, Jequié';
            UPDATE Unidade SET gerente_cpf = '111.111.111-32' WHERE localizacao = 'Quadra 101, 110, Barreiras';
            UPDATE Unidade SET gerente_cpf = '111.111.111-34' WHERE localizacao = 'Recanto das Emas, 120, Vitória da Conquista';
            UPDATE Unidade SET gerente_cpf = '111.111.111-36' WHERE localizacao = 'Setor Hoteleiro Norte, 130, Porto Seguro';
            UPDATE Unidade SET gerente_cpf = '111.111.111-38' WHERE localizacao = 'Travessa dos Artistas, 140, Eunápolis';
            UPDATE Unidade SET gerente_cpf = '111.111.111-40' WHERE localizacao = 'Vila dos Pescadores, 150, Teixeira de Freitas';
            UPDATE Unidade SET gerente_cpf = '111.111.111-42' WHERE localizacao = 'W3 Sul, 160, Alagoinhas';
            UPDATE Unidade SET gerente_cpf = '111.111.111-44' WHERE localizacao = 'Zona Industrial, 170, Paulo Afonso';
            UPDATE Unidade SET gerente_cpf = '111.111.111-46' WHERE localizacao = 'Loteamento Aquarius, 180, Santo Antônio de Jesus';
            UPDATE Unidade SET gerente_cpf = '111.111.111-48' WHERE localizacao = 'Parque da Cidade, 190, Jacobina';
            UPDATE Unidade SET gerente_cpf = '111.111.111-50' WHERE localizacao = 'Rua Chile, 200, Irecê';

            COMMIT; 
        """;

        jdbcTemplate.execute(query_sql);
    }
}