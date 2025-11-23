-- ======================================================
-- TRIGGERS E AUDITORIA
-- ======================================================

CREATE TABLE IF NOT EXISTS log_auditoria (
    id SERIAL PRIMARY KEY,
    tabela VARCHAR(50),
    operacao VARCHAR(10),
    data_hora TIMESTAMP DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION registrar_auditoria()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO log_auditoria (tabela, operacao, data_hora)
    VALUES (TG_TABLE_NAME, TG_OP, NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_auditoria_venda
AFTER INSERT OR UPDATE OR DELETE ON venda
FOR EACH ROW EXECUTE FUNCTION registrar_auditoria();

CREATE OR REPLACE TRIGGER trg_auditoria_maquina
AFTER INSERT OR UPDATE OR DELETE ON maquina
FOR EACH ROW EXECUTE FUNCTION registrar_auditoria();
