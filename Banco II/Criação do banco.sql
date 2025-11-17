-- ======================================================
-- CRIAÇÃO DAS TABELAS PRINCIPAIS DO SISTEMA RURALMIND
-- ======================================================

-- Tabela: Usuario
-- Armazena os dados de login e perfil dos usuários do sistema.
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK(tipo IN ('admin', 'vendedor'))
);
COMMENT ON COLUMN usuario.id_usuario IS 'Identificador do Usuário';
COMMENT ON COLUMN usuario.nome IS 'Nome completo do Usuário';
COMMENT ON COLUMN usuario.login IS 'Nome de login do Usuário';
COMMENT ON COLUMN usuario.senha IS 'Senha do Usuário';
COMMENT ON COLUMN usuario.tipo IS 'Tipo de Usuário: "admin" ou "vendedor"';


-- Tabela: Cliente
-- Contém os dados dos clientes cadastrados.
CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    endereco VARCHAR(150) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL
);
COMMENT ON COLUMN cliente.id_cliente IS 'Identificador do Cliente';
COMMENT ON COLUMN cliente.nome IS 'Nome completo do Cliente';
COMMENT ON COLUMN cliente.cpf IS 'CPF do Cliente';
COMMENT ON COLUMN cliente.telefone IS 'Telefone de contato do Cliente';
COMMENT ON COLUMN cliente.email IS 'Email do Cliente (opcional)';
COMMENT ON COLUMN cliente.endereco IS 'Endereço completo do Cliente';
COMMENT ON COLUMN cliente.cidade IS 'Cidade do Cliente';
COMMENT ON COLUMN cliente.estado IS 'Estado (UF) do Cliente';


-- Tabela: Fornecedor
-- Registra informações de fornecedores vinculados às máquinas.
CREATE TABLE fornecedor (
    id_fornecedor SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    endereco VARCHAR(150) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL
);
COMMENT ON COLUMN fornecedor.id_fornecedor IS 'Identificador do Fornecedor';
COMMENT ON COLUMN fornecedor.nome IS 'Nome da Empresa ou Fornecedor';
COMMENT ON COLUMN fornecedor.cnpj IS 'CNPJ do Fornecedor';
COMMENT ON COLUMN fornecedor.telefone IS 'Telefone de contato';
COMMENT ON COLUMN fornecedor.email IS 'Email do Fornecedor';
COMMENT ON COLUMN fornecedor.endereco IS 'Endereço completo do Fornecedor';
COMMENT ON COLUMN fornecedor.cidade IS 'Cidade do Fornecedor';
COMMENT ON COLUMN fornecedor.estado IS 'Estado (UF) do Fornecedor';


-- Tabela: Maquina
-- Contém o cadastro de máquinas disponíveis para venda.
CREATE TABLE maquina (
    id_maquina SERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    nova_usada VARCHAR(10) NOT NULL CHECK(nova_usada IN ('nova', 'usada')),
    preco_custo NUMERIC(10, 2) NOT NULL,
    preco_venda NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK(status IN ('disponivel', 'fora_de_estoque')),
    quantia INT NOT NULL,
    id_fornecedor INT NOT NULL REFERENCES fornecedor(id_fornecedor)
);
COMMENT ON COLUMN maquina.id_maquina IS 'Identificador da Máquina';
COMMENT ON COLUMN maquina.modelo IS 'Modelo da Máquina';
COMMENT ON COLUMN maquina.marca IS 'Marca da Máquina';
COMMENT ON COLUMN maquina.ano IS 'Ano de fabricação';
COMMENT ON COLUMN maquina.nova_usada IS 'Situação da Máquina: nova ou usada';
COMMENT ON COLUMN maquina.preco_custo IS 'Preço de custo unitário';
COMMENT ON COLUMN maquina.preco_venda IS 'Preço de venda unitário';
COMMENT ON COLUMN maquina.status IS 'Status: disponível ou fora de estoque';
COMMENT ON COLUMN maquina.quantia IS 'Quantidade disponível no estoque';


-- Tabela: Venda
-- Registra as informações principais de cada venda.
CREATE TABLE venda (
    id_venda SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL REFERENCES cliente(id_cliente),
    id_usuario INT NOT NULL REFERENCES usuario(id_usuario),
    data_venda DATE NOT NULL,
    observacao VARCHAR(500),
    lucro NUMERIC(10, 2) NOT NULL
);
COMMENT ON COLUMN venda.id_venda IS 'Identificador da Venda';
COMMENT ON COLUMN venda.id_cliente IS 'Cliente vinculado à Venda';
COMMENT ON COLUMN venda.id_usuario IS 'Usuário responsável pela Venda';
COMMENT ON COLUMN venda.data_venda IS 'Data da Venda';
COMMENT ON COLUMN venda.observacao IS 'Observações adicionais da Venda';
COMMENT ON COLUMN venda.lucro IS 'Lucro total da Venda';


-- Tabela: Venda_Maquina
-- Tabela associativa que relaciona as máquinas com cada venda.
CREATE TABLE venda_maquina (
    id_venda_maquina SERIAL PRIMARY KEY,
    id_venda INT NOT NULL REFERENCES venda(id_venda) ON DELETE CASCADE,
    id_maquina INT NOT NULL REFERENCES maquina(id_maquina),
    quantidade INT NOT NULL DEFAULT 1,
    preco_venda NUMERIC(10, 2) NOT NULL
);
COMMENT ON COLUMN venda_maquina.id_venda_maquina IS 'Identificador do item vendido';
COMMENT ON COLUMN venda_maquina.id_venda IS 'Identificador da venda vinculada';
COMMENT ON COLUMN venda_maquina.id_maquina IS 'Identificador da máquina vendida';
COMMENT ON COLUMN venda_maquina.quantidade IS 'Quantidade vendida';
COMMENT ON COLUMN venda_maquina.preco_venda IS 'Preço unitário na venda';

