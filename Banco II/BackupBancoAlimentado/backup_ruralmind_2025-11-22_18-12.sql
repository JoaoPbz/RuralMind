--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: registrar_auditoria(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.registrar_auditoria() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO log_auditoria (tabela, operacao, data_hora)
    VALUES (TG_TABLE_NAME, TG_OP, NOW());
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.registrar_auditoria() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_cliente integer NOT NULL,
    nome character varying(100) NOT NULL,
    cpf character varying(14) NOT NULL,
    telefone character varying(20) NOT NULL,
    email character varying(100),
    endereco character varying(150) NOT NULL,
    cidade character varying(100) NOT NULL,
    estado character(2) NOT NULL
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cliente_id_cliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cliente_id_cliente_seq OWNER TO postgres;

--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cliente_id_cliente_seq OWNED BY public.cliente.id_cliente;


--
-- Name: fornecedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fornecedor (
    id_fornecedor integer NOT NULL,
    nome character varying(100) NOT NULL,
    cnpj character varying(18) NOT NULL,
    telefone character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    endereco character varying(150) NOT NULL,
    cidade character varying(100) NOT NULL,
    estado character(2) NOT NULL
);


ALTER TABLE public.fornecedor OWNER TO postgres;

--
-- Name: maquina; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.maquina (
    id_maquina integer NOT NULL,
    modelo character varying(100) NOT NULL,
    marca character varying(100) NOT NULL,
    ano integer NOT NULL,
    nova_usada character varying(10) NOT NULL,
    preco_custo numeric(10,2) NOT NULL,
    preco_venda numeric(10,2) NOT NULL,
    status character varying(20) NOT NULL,
    quantia integer NOT NULL,
    id_fornecedor integer NOT NULL,
    CONSTRAINT maquina_nova_usada_check CHECK (((nova_usada)::text = ANY ((ARRAY['nova'::character varying, 'usada'::character varying])::text[]))),
    CONSTRAINT maquina_status_check CHECK (((status)::text = ANY ((ARRAY['disponivel'::character varying, 'fora_de_estoque'::character varying])::text[])))
);


ALTER TABLE public.maquina OWNER TO postgres;

--
-- Name: estoque_maquinas; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.estoque_maquinas AS
 SELECT m.id_maquina,
    m.modelo,
    m.marca,
    m.ano,
    m.nova_usada,
    m.quantia AS estoque_atual,
    m.preco_custo,
    m.preco_venda,
    f.nome AS fornecedor
   FROM (public.maquina m
     JOIN public.fornecedor f ON ((m.id_fornecedor = f.id_fornecedor)))
  ORDER BY m.marca, m.modelo;


ALTER VIEW public.estoque_maquinas OWNER TO postgres;

--
-- Name: fornecedor_id_fornecedor_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.fornecedor_id_fornecedor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.fornecedor_id_fornecedor_seq OWNER TO postgres;

--
-- Name: fornecedor_id_fornecedor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.fornecedor_id_fornecedor_seq OWNED BY public.fornecedor.id_fornecedor;


--
-- Name: log_auditoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log_auditoria (
    id integer NOT NULL,
    tabela character varying(50),
    operacao character varying(10),
    data_hora timestamp without time zone DEFAULT now()
);


ALTER TABLE public.log_auditoria OWNER TO postgres;

--
-- Name: log_auditoria_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.log_auditoria_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.log_auditoria_id_seq OWNER TO postgres;

--
-- Name: log_auditoria_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.log_auditoria_id_seq OWNED BY public.log_auditoria.id;


--
-- Name: maquina_id_maquina_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.maquina_id_maquina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.maquina_id_maquina_seq OWNER TO postgres;

--
-- Name: maquina_id_maquina_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.maquina_id_maquina_seq OWNED BY public.maquina.id_maquina;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    nome character varying(100) NOT NULL,
    login character varying(50) NOT NULL,
    senha character varying(255) NOT NULL,
    tipo character varying(10) NOT NULL,
    CONSTRAINT usuario_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['admin'::character varying, 'vendedor'::character varying])::text[])))
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Name: COLUMN usuario.id_usuario; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.id_usuario IS 'Identificador do Usuário';


--
-- Name: COLUMN usuario.nome; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.nome IS 'Nome completo do Usuário';


--
-- Name: COLUMN usuario.login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.login IS 'Nome de login do Usuário';


--
-- Name: COLUMN usuario.senha; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.senha IS 'Senha do Usuário';


--
-- Name: COLUMN usuario.tipo; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.tipo IS 'Tipo de Usuário: admin ou vendedor';


--
-- Name: venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda (
    id_venda integer NOT NULL,
    id_cliente integer NOT NULL,
    id_usuario integer NOT NULL,
    data_venda date NOT NULL,
    observacao character varying(500),
    lucro numeric(10,2) NOT NULL
);


ALTER TABLE public.venda OWNER TO postgres;

--
-- Name: venda_maquina; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda_maquina (
    id_venda_maquina integer NOT NULL,
    id_venda integer NOT NULL,
    id_maquina integer NOT NULL,
    quantidade integer DEFAULT 1 NOT NULL,
    preco_unitario numeric(10,2) NOT NULL,
    subtotal numeric(10,2) NOT NULL
);


ALTER TABLE public.venda_maquina OWNER TO postgres;

--
-- Name: COLUMN venda_maquina.preco_unitario; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venda_maquina.preco_unitario IS 'Preço unitário na venda';


--
-- Name: COLUMN venda_maquina.subtotal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venda_maquina.subtotal IS 'Subtotal calculado (quantidade × preço unitário)';


--
-- Name: relatorio_vendas; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.relatorio_vendas AS
 SELECT v.id_venda,
    v.data_venda,
    c.nome AS nome_cliente,
    u.nome AS nome_vendedor,
    COALESCE(sum((vm.preco_unitario * (vm.quantidade)::numeric)), (0)::numeric) AS faturamento,
    COALESCE(sum(((vm.preco_unitario - m.preco_custo) * (vm.quantidade)::numeric)), (0)::numeric) AS lucro_liquido
   FROM ((((public.venda v
     JOIN public.cliente c ON ((v.id_cliente = c.id_cliente)))
     JOIN public.usuario u ON ((v.id_usuario = u.id_usuario)))
     JOIN public.venda_maquina vm ON ((v.id_venda = vm.id_venda)))
     JOIN public.maquina m ON ((vm.id_maquina = m.id_maquina)))
  GROUP BY v.id_venda, v.data_venda, c.nome, u.nome
  ORDER BY v.data_venda DESC;


ALTER VIEW public.relatorio_vendas OWNER TO postgres;

--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_id_usuario_seq OWNER TO postgres;

--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_id_usuario_seq OWNED BY public.usuario.id_usuario;


--
-- Name: venda_id_venda_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venda_id_venda_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venda_id_venda_seq OWNER TO postgres;

--
-- Name: venda_id_venda_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venda_id_venda_seq OWNED BY public.venda.id_venda;


--
-- Name: venda_maquina_id_venda_maquina_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venda_maquina_id_venda_maquina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venda_maquina_id_venda_maquina_seq OWNER TO postgres;

--
-- Name: venda_maquina_id_venda_maquina_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venda_maquina_id_venda_maquina_seq OWNED BY public.venda_maquina.id_venda_maquina;


--
-- Name: cliente id_cliente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente ALTER COLUMN id_cliente SET DEFAULT nextval('public.cliente_id_cliente_seq'::regclass);


--
-- Name: fornecedor id_fornecedor; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor ALTER COLUMN id_fornecedor SET DEFAULT nextval('public.fornecedor_id_fornecedor_seq'::regclass);


--
-- Name: log_auditoria id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_auditoria ALTER COLUMN id SET DEFAULT nextval('public.log_auditoria_id_seq'::regclass);


--
-- Name: maquina id_maquina; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maquina ALTER COLUMN id_maquina SET DEFAULT nextval('public.maquina_id_maquina_seq'::regclass);


--
-- Name: usuario id_usuario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuario_id_usuario_seq'::regclass);


--
-- Name: venda id_venda; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda ALTER COLUMN id_venda SET DEFAULT nextval('public.venda_id_venda_seq'::regclass);


--
-- Name: venda_maquina id_venda_maquina; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_maquina ALTER COLUMN id_venda_maquina SET DEFAULT nextval('public.venda_maquina_id_venda_maquina_seq'::regclass);


--
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (id_cliente, nome, cpf, telefone, email, endereco, cidade, estado) FROM stdin;
1	Marco	54243181381	49999999999	Marco@gmail.com	Rua Barão Branco, nº 231	Brasilia	DF
3	Loide	12904787273	49777777777	Loide@gmail.com	Rua Anita Garibaldi, nº 661	Romelândia	SC
2	Welington	12341805827	49888888888	Welington123@gmail.com	Interior, nº s/n	Romelândia	SC
4	Lucio	51039509122	49666666666	LucioGames@gmail.com	Rua Voto Dourado, nº s/n	Palmitos	SC
5	Henri	12998741666	49555555555	HenriDB@gmail.com	Rua Relicario, nº 666	São Paulo	SP
6	Dante	12071687264	49444444444	DanteMiau@gmail.com	Rua Prismalina, nº 312	Anchieta	SC
7	Cesar	98149102717	49333333333	KaiserAN@gmail.com	Rua Gauderial, nº 003	São Paula	SC
\.


--
-- Data for Name: fornecedor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.fornecedor (id_fornecedor, nome, cnpj, telefone, email, endereco, cidade, estado) FROM stdin;
1	Triton Fertilance	11111111111111	49238109741	TritonComercial@gmail.com	Distrito Industrial, nº 124	Espumoso	RS
2	Imasa	22222222222222	49178924190	ImasaComercial@gmail.com	DIstrito Industrial, nº 981	Espumoso	RS
3	Haramaq	33333333333333	49128301812	HaramaqIND@gmail.com	Distrito Dourado, nº 411	Taquara	RS
4	Plantimar	44444444444444	49128301981	PlantimarMaquinas@gmail.com	Distrito Vermelho, nº 312	São Paulo	SP
\.


--
-- Data for Name: log_auditoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.log_auditoria (id, tabela, operacao, data_hora) FROM stdin;
1	maquina	INSERT	2025-11-22 17:55:41.475463
2	maquina	INSERT	2025-11-22 17:56:43.540332
3	maquina	INSERT	2025-11-22 17:57:24.144462
4	maquina	INSERT	2025-11-22 17:58:30.753762
5	maquina	INSERT	2025-11-22 18:06:10.593405
6	venda	INSERT	2025-11-22 18:07:36.046907
7	maquina	UPDATE	2025-11-22 18:07:36.046907
8	venda	INSERT	2025-11-22 18:08:50.953803
9	maquina	UPDATE	2025-11-22 18:08:50.953803
10	maquina	UPDATE	2025-11-22 18:08:50.953803
11	venda	INSERT	2025-11-22 18:09:48.777406
12	maquina	UPDATE	2025-11-22 18:09:48.777406
13	venda	INSERT	2025-11-22 18:11:17.166121
14	maquina	UPDATE	2025-11-22 18:11:17.166121
15	maquina	UPDATE	2025-11-22 18:11:17.166121
16	venda	INSERT	2025-11-22 18:11:54.525015
17	maquina	UPDATE	2025-11-22 18:11:54.525015
\.


--
-- Data for Name: maquina; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.maquina (id_maquina, modelo, marca, ano, nova_usada, preco_custo, preco_venda, status, quantia, id_fornecedor) FROM stdin;
2	Esparramador Rotax1200	Triton	2023	usada	12000.00	18000.00	disponivel	1	1
4	Vagão Forrageiro	Haramaq	2025	nova	45000.00	60000.00	disponivel	1	3
5	Semeadeira 11 Linhas V/I	Plantimar	2022	usada	30000.00	38000.00	fora_de_estoque	0	4
1	Carretão Dell5000 5Ton	Triton	2025	nova	35000.00	45000.00	disponivel	2	1
3	Plantadeira PhxPlus 7Linhas	Imasa	2025	nova	54000.00	70000.00	disponivel	2	2
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (id_usuario, nome, login, senha, tipo) FROM stdin;
1	Administrador	admin	123	admin
2	Joao Pedro	Joao	123	admin
3	Everton	Everton	123	admin
4	Douglas	Douglas	123	admin
5	Otilia	Otilia	123	admin
6	Roberson	Roberson	123	admin
\.


--
-- Data for Name: venda; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venda (id_venda, id_cliente, id_usuario, data_venda, observacao, lucro) FROM stdin;
1	4	2	2025-11-22	N°serie:1229\nEntrega Até 25/11	18000.00
2	5	2	2025-11-22	N°Serie\nCarretão:129\nVagão:5182\nEntrega Até 26/11	105000.00
3	2	2	2025-11-22	Entrega Até 27/11	140000.00
4	7	3	2025-11-22	n°serie\nSemeadeira:2913\nCarretão:1801\nEntrega Até 29/11	83000.00
5	1	4	2025-11-22	n°serie:3891\nEntrega Até 28/11	70000.00
\.


--
-- Data for Name: venda_maquina; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venda_maquina (id_venda_maquina, id_venda, id_maquina, quantidade, preco_unitario, subtotal) FROM stdin;
1	1	2	1	18000.00	18000.00
2	2	4	1	60000.00	60000.00
3	2	1	1	45000.00	45000.00
4	3	3	2	70000.00	140000.00
5	4	5	1	38000.00	38000.00
6	4	1	1	45000.00	45000.00
7	5	3	1	70000.00	70000.00
\.


--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 7, true);


--
-- Name: fornecedor_id_fornecedor_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.fornecedor_id_fornecedor_seq', 4, true);


--
-- Name: log_auditoria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_auditoria_id_seq', 17, true);


--
-- Name: maquina_id_maquina_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.maquina_id_maquina_seq', 5, true);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 6, true);


--
-- Name: venda_id_venda_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venda_id_venda_seq', 5, true);


--
-- Name: venda_maquina_id_venda_maquina_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venda_maquina_id_venda_maquina_seq', 7, true);


--
-- Name: cliente cliente_cpf_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_cpf_key UNIQUE (cpf);


--
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- Name: fornecedor fornecedor_cnpj_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor
    ADD CONSTRAINT fornecedor_cnpj_key UNIQUE (cnpj);


--
-- Name: fornecedor fornecedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor
    ADD CONSTRAINT fornecedor_pkey PRIMARY KEY (id_fornecedor);


--
-- Name: log_auditoria log_auditoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log_auditoria
    ADD CONSTRAINT log_auditoria_pkey PRIMARY KEY (id);


--
-- Name: maquina maquina_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maquina
    ADD CONSTRAINT maquina_pkey PRIMARY KEY (id_maquina);


--
-- Name: usuario usuario_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_login_key UNIQUE (login);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: venda_maquina venda_maquina_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_maquina
    ADD CONSTRAINT venda_maquina_pkey PRIMARY KEY (id_venda_maquina);


--
-- Name: venda venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT venda_pkey PRIMARY KEY (id_venda);


--
-- Name: idx_cliente_nome; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_cliente_nome ON public.cliente USING btree (lower((nome)::text));


--
-- Name: idx_fornecedor_nome; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_fornecedor_nome ON public.fornecedor USING btree (lower((nome)::text));


--
-- Name: idx_maquina_modelo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_maquina_modelo ON public.maquina USING btree (lower((modelo)::text));


--
-- Name: idx_venda_data_venda; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_venda_data_venda ON public.venda USING btree (data_venda);


--
-- Name: idx_venda_maquina_venda; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_venda_maquina_venda ON public.venda_maquina USING btree (id_venda);


--
-- Name: maquina trg_auditoria_maquina; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_auditoria_maquina AFTER INSERT OR DELETE OR UPDATE ON public.maquina FOR EACH ROW EXECUTE FUNCTION public.registrar_auditoria();


--
-- Name: venda trg_auditoria_venda; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_auditoria_venda AFTER INSERT OR DELETE OR UPDATE ON public.venda FOR EACH ROW EXECUTE FUNCTION public.registrar_auditoria();


--
-- Name: maquina maquina_id_fornecedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maquina
    ADD CONSTRAINT maquina_id_fornecedor_fkey FOREIGN KEY (id_fornecedor) REFERENCES public.fornecedor(id_fornecedor);


--
-- Name: venda venda_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT venda_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);


--
-- Name: venda venda_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT venda_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- Name: venda_maquina venda_maquina_id_maquina_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_maquina
    ADD CONSTRAINT venda_maquina_id_maquina_fkey FOREIGN KEY (id_maquina) REFERENCES public.maquina(id_maquina);


--
-- Name: venda_maquina venda_maquina_id_venda_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_maquina
    ADD CONSTRAINT venda_maquina_id_venda_fkey FOREIGN KEY (id_venda) REFERENCES public.venda(id_venda) ON DELETE CASCADE;


--
-- Name: TABLE cliente; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.cliente TO administrador;
GRANT ALL ON TABLE public.cliente TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.cliente TO vendedor;
GRANT SELECT ON TABLE public.cliente TO consulta;


--
-- Name: SEQUENCE cliente_id_cliente_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.cliente_id_cliente_seq TO administrador;
GRANT ALL ON SEQUENCE public.cliente_id_cliente_seq TO gerente;


--
-- Name: TABLE fornecedor; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.fornecedor TO administrador;
GRANT ALL ON TABLE public.fornecedor TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.fornecedor TO vendedor;
GRANT SELECT ON TABLE public.fornecedor TO consulta;


--
-- Name: TABLE maquina; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.maquina TO administrador;
GRANT ALL ON TABLE public.maquina TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.maquina TO vendedor;
GRANT SELECT ON TABLE public.maquina TO consulta;


--
-- Name: TABLE estoque_maquinas; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.estoque_maquinas TO administrador;
GRANT ALL ON TABLE public.estoque_maquinas TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.estoque_maquinas TO vendedor;
GRANT SELECT ON TABLE public.estoque_maquinas TO consulta;


--
-- Name: SEQUENCE fornecedor_id_fornecedor_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.fornecedor_id_fornecedor_seq TO administrador;
GRANT ALL ON SEQUENCE public.fornecedor_id_fornecedor_seq TO gerente;


--
-- Name: SEQUENCE maquina_id_maquina_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.maquina_id_maquina_seq TO administrador;
GRANT ALL ON SEQUENCE public.maquina_id_maquina_seq TO gerente;


--
-- Name: TABLE usuario; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.usuario TO administrador;
GRANT ALL ON TABLE public.usuario TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.usuario TO vendedor;
GRANT SELECT ON TABLE public.usuario TO consulta;


--
-- Name: TABLE venda; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.venda TO administrador;
GRANT ALL ON TABLE public.venda TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.venda TO vendedor;
GRANT SELECT ON TABLE public.venda TO consulta;


--
-- Name: TABLE venda_maquina; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.venda_maquina TO administrador;
GRANT ALL ON TABLE public.venda_maquina TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.venda_maquina TO vendedor;
GRANT SELECT ON TABLE public.venda_maquina TO consulta;


--
-- Name: TABLE relatorio_vendas; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.relatorio_vendas TO administrador;
GRANT ALL ON TABLE public.relatorio_vendas TO gerente;
GRANT SELECT,INSERT,UPDATE ON TABLE public.relatorio_vendas TO vendedor;
GRANT SELECT ON TABLE public.relatorio_vendas TO consulta;


--
-- Name: SEQUENCE usuario_id_usuario_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.usuario_id_usuario_seq TO administrador;
GRANT ALL ON SEQUENCE public.usuario_id_usuario_seq TO gerente;


--
-- Name: SEQUENCE venda_id_venda_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.venda_id_venda_seq TO administrador;
GRANT ALL ON SEQUENCE public.venda_id_venda_seq TO gerente;


--
-- Name: SEQUENCE venda_maquina_id_venda_maquina_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.venda_maquina_id_venda_maquina_seq TO administrador;
GRANT ALL ON SEQUENCE public.venda_maquina_id_venda_maquina_seq TO gerente;


--
-- PostgreSQL database dump complete
--

