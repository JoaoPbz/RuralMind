# 🌾 RuralMind 
Sistema de Gestão de Implementos Agrícolas


## 📘 Visão Geral

O RuralMind é um sistema desktop destinado à gestão de vendas e estoque de implementos agrícolas.
Ele integra conteúdos das disciplinas de:

Programação II (Java + Swing)

Banco de Dados II (PostgreSQL)

Engenharia de Software

O sistema permite organizar clientes, fornecedores, máquinas, vendedores e vendas, mantendo controle seguro de estoque, auditoria e permissões de usuários.

## 🚜 Funcionalidades Principais

Autenticação com perfis admin e vendedor

Cadastro completo:

Clientes

Fornecedores

Vendedores

Máquinas

Controle automático de estoque

Sistema completo de vendas

Relatórios internos

Backup e restauração integrados ao PostgreSQL

Auditoria automática de operações

Herança, encapsulamento e polimorfismo aplicados

Interface gráfica desenvolvida com Swing

## 🛠️ Tecnologias Utilizadas

Java 17+

Swing

PostgreSQL 

JDBC

DBeaver

Visual Paradigm

Git / GitHub

## 🧱 Arquitetura do Sistema

Organizado em camadas independentes:

Model – classes que representam entidades do sistema

DAO – operações SQL e integração via JDBC

View – todas as telas Swing

Util – classes auxiliares (BackupBD, ConexaoBD, etc.)

## 📁 Estrutura do Projeto

```
/Banco II
├── BackupBancoAlimentado
│ └── BackupRuralmind.sql
│ (Backup com alguns dados adicionados para testes)
│
├── ScriptsSeparados
│ ├── TriggersProcedures.sql
│ ├── UsuariosPermissões.sql -- Scripts isolados para visualização
│ └── ViewsIndices.sql
│
├── Criacao do banco completa.sql -- Script definitivo com criação e todos os scripts inclusos para execução unica
│
├── Diagrama Relacional.vpp
└── Diagrama Relacional.png

/Engenharia de Software
├── Caso_de_Uso.pdf
├── Atividades.pdf
├── Estados.pdf
├── Sequencia.pdf
├── Classes.pdf
├── DER.pdf
│
└── diagramas_imagens
├── caso_de_uso.png
├── atividades.png
├── estados.png
├── sequencia.png
├── classes.png
└── der.png

/Programação II
├── Sistema
│ ├── src
│ ├── bin
│ ├── lib
│ └── (outros arquivos e diretórios do projeto)
│
└── JavaDoc
└── javadoc.zip

README.md
```
# 🧩 Documentação de Engenharia de Software
## 📌 Diagrama de Caso de Uso

Representa todas as funcionalidades acessíveis ao usuário (admin e vendedor).
Mostra o que o sistema faz, incluindo cadastros, vendas, relatórios, permissões e backup.

## 📌 Diagrama de Atividades

Mostra o fluxo operacional das funções principais:
cadastro, login, venda, atualização de estoque e backup.
Representa como cada operação acontece internamente.

## 📌 Diagrama de Estados

Foca nos estados possíveis da máquina, como:

cadastrada

disponível

indisponível

vendida

excluída logicamente

Mostra as mudanças de estado ao longo do uso.

## 📌 Diagrama de Sequência

Mostra a comunicação entre classes e objetos durante processos, como:

Ex:Venda

Login → Validação → Tela Principal

Venda → Atualização de estoque → Registro no banco

Cadastro → DAO → Banco de dados

## 📌 Diagrama de Classes

Representa a estrutura orientada a objetos:
classes, atributos, métodos, associações, cardinalidades e herança:

Pessoa → Cliente e Usuario

## 📌 Modelo Entidade-Relacionamento (DER)

Mostra a estrutura lógica do banco, com tabelas, chaves primárias/estrangeiras e relacionamentos.
