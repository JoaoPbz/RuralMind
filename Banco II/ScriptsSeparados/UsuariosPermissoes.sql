-- ======================================================
-- CRIAÇÃO DE USUÁRIOS E PERMISSÕES
-- ======================================================

CREATE USER administrador WITH PASSWORD 'admin123';
CREATE USER gerente WITH PASSWORD 'gerente123';
CREATE USER vendedor WITH PASSWORD 'vendedor123';
CREATE USER consulta WITH PASSWORD 'consulta123';


-- Permissões amplas
GRANT ALL PRIVILEGES ON DATABASE ruralmind TO administrador, gerente;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO administrador, gerente;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO administrador, gerente;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO administrador, gerente;

-- Permissões restritas
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA public TO vendedor;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO consulta;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO vendedor, consulta;

