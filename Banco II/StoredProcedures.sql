CREATE OR REPLACE PROCEDURE atualizar_status_maquinas()
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE maquina
    SET status = CASE
                    WHEN quantia > 0 THEN 'disponivel'
                    ELSE 'fora_de_estoque'
                 END;
END;
$$;
-------------
CREATE OR REPLACE PROCEDURE adicionar_estoque(
    p_id INT,
    p_qtd INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE maquina
    SET quantia = quantia + p_qtd
    WHERE id_maquina = p_id;
END;
$$;
