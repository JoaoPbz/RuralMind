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
