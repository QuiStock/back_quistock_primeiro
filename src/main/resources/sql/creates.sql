drop table if exists super_admin;
drop table if exists loja;
drop table if exists endereco;
drop table if exists regiao;
drop table if exists gerente_regional;

create table if not exists super_admin(
    codigo int generated always as identity primary key,
    nome varchar(150) not null,
    email varchar(320) not null,
    senha varchar(50) not null
    );

create table if not exists loja(
    codigo int generated always as identity primary key,
    email varchar(320) not null,
    senha varchar(50)
    );

create table if not exists endereco(
    codigo int generated always as identity primary key,
    cep varchar(12),
    pais varchar(50),
    rua varchar(150),
    numero int,
    cidade varchar(100),
    estado varchar(50),
    loja_id int,
    constraint fk_loja
    foreign key (loja_id) references loja(codigo)
    on delete cascade
    );

create table if not exists regiao(
    codigo int generated always as identity primary key,
    nome varchar(100),
    loja_id int,
    constraint fk_loja_regiao
    foreign key (loja_id) references loja(codigo)
    on delete cascade
    );

create table if not exists gerente_regional(
    codigo int generated always as identity primary key,
    nome varchar(60),
    email varchar(320),
    senha varchar(50),
    regiao_id int,
    constraint fk_regiao
    foreign key (regiao_id) references regiao(codigo)
    on delete cascade
    );