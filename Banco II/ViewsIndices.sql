-- cria views e índices
CREATE OR REPLACE VIEW relatorio_vendas AS
SELECT v.id_venda, v.data_venda, c.nome AS nome_cliente, u.nome AS nome_vendedor,
       COALESCE(SUM(vm.preco_venda * vm.quantidade), 0) AS faturamento,
       COALESCE(SUM((vm.preco_venda - m.preco_custo) * vm.quantidade), 0) AS lucro_liquido
FROM venda v
JOIN cliente c ON v.id_cliente = c.id_cliente
JOIN usuario u ON v.id_usuario = u.id_usuario
JOIN venda_maquina vm ON v.id_venda = vm.id_venda
JOIN maquina m ON vm.id_maquina = m.id_maquina
GROUP BY v.id_venda, v.data_venda, c.nome, u.nome
ORDER BY v.data_venda DESC;

CREATE OR REPLACE VIEW estoque_maquinas AS
SELECT m.id_maquina, m.modelo, m.marca, m.ano, m.nova_usada, m.quantia AS estoque_atual,
       m.preco_custo, m.preco_venda, f.nome AS fornecedor
FROM maquina m
JOIN fornecedor f ON m.id_fornecedor = f.id_fornecedor
ORDER BY m.marca, m.modelo;

CREATE INDEX IF NOT EXISTS idx_venda_data_venda ON venda (data_venda);
CREATE INDEX IF NOT EXISTS idx_cliente_nome ON cliente (LOWER(nome));
CREATE INDEX IF NOT EXISTS idx_maquina_modelo ON maquina (LOWER(modelo));
CREATE INDEX IF NOT EXISTS idx_venda_maquina_venda ON venda_maquina (id_venda);
CREATE INDEX IF NOT EXISTS idx_fornecedor_nome ON fornecedor (LOWER(nome));
