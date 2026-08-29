drop table if exists super_admin;
drop table if exists loja;
drop table if exists endereco;
drop table if exists regiao;
drop table if exists gerente_regional;
drop table if exists promocao;
drop table if exists aplica;
drop table if exists produto;
drop table if exists historico;
drop table if exists item_pedido;
drop table if exists pedido;
drop table if exists fluxo;
drop table if exists contem;

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

create table if not exists promocao(
    codigo INT generated always as identity primary key,
    percentual NUMERIC NOT NULL,
    dt_fim DATE NOT NULL,
    dt_promo DATE NOT NULL
);

create table if not exists aplica(
    produto_lote int not null,
    promocao_codigo int not null,
    foreign key (produto_lote) references produto(lote),
    foreign key (promocao_codigo) references promocao(codigo)
);

create table if not exists produto(
    lote INT generated always as identity primary key,
    nome varchar(100) not null,
    preco numeric not null,
    validade date not null,
    marca varchar not null,
    linha varchar not null,
    qtd_estoque int not null,
    loja_id int not null,
    foreign key (loja_id) references loja(codigo)
);

create table if not exists historico(
    id int generated always as identity primary key,
    produto_lote int not null,
    foreign key (produto_lote) references produto(lote)
);

create table if not exists item_pedido(
    id int generated always as identity primary key,
    quantidade int not null,
    preco numeric not null,
    produto_lote int not null,
    foreign key (produto_lote) references produto(lote)
);

create table if not exists pedido(
    codigo int generated always as identity primary key,
    dt_pedido date not null,
    item_pedido_id int not null,
    foreign key (item_pedido_id) references item_pedido(id),
    total numeric not null
);

create table if not exists fluxo(
    id INT generated always as identity primary key,
    tipo VARCHAR(10) not null,
    produto_lote int not null,
    foreign key (produto_lote) references produto(lote)
);

create table if not exists contem(
    historico_id int not null,
    fluxo_id int not null,
    foreign key (historico_id) references historico(id),
    foreign key (fluxo_id) references fluxo(id)
);