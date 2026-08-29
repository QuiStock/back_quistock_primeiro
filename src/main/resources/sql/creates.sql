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
    codigo int generated always as identity primary key CHECK (codigo >= 0),
    nome varchar(150) not null,
    email varchar(320) not null unique,
    senha varchar(50) not null
    );

create table if not exists loja(
    codigo int generated always as identity primary key CHECK (codigo >= 0),
    email varchar(320) not null unique,
    senha varchar(50) not null
    );

create table if not exists endereco(
    codigo int generated always as identity primary key CHECK (codigo >= 0),
    cep varchar(11) not null,
    pais varchar(50) not null,
    rua varchar(150) not null,
    numero int not null CHECK (numero >= 0),
    cidade varchar(100) not null,
    estado varchar(50) not null,
    loja_id int not null,
    constraint fk_loja
    foreign key (loja_id) references loja(codigo)
    on delete cascade
);

create table if not exists regiao(
    codigo int generated always as identity primary key CHECK (codigo >= 0),
    nome varchar(100) not null,
    loja_id int not null CHECK (loja_id >= 0),
    constraint fk_loja_regiao
    foreign key (loja_id) references loja(codigo)
    on delete cascade
    );

create table if not exists promocao(
    codigo INT generated always as identity primary key Check (codigo >= 0),
    percentual NUMERIC NOT NULL Check (percentual >= 0 and percentual < 100),
    dt_fim DATE NOT NULL,
    dt_promo DATE NOT NULL
);    

create table if not exists gerente_regional(
    codigo int generated always as identity primary key CHECK (codigo >= 0),
    nome varchar(60) not null,
    email varchar(320) not null,
    senha varchar(50) not null,
    regiao_id int not null Check (regiao_id >= 0),
    constraint fk_regiao
    foreign key (regiao_id) references regiao(codigo)
    on delete cascade
    );

create table if not exists produto(
    lote INT primary key check(lote >= 0),
    nome varchar(100) not null,
    preco numeric(10,2) not null check(preco >= 0),
    validade date not null,
    marca varchar not null,
    linha varchar not null,
    qtd_estoque int not null default 0 check(qtd_estoque >= 0),
    loja_id int not null check(loja_id >= 0),
    foreign key (loja_id) references loja(codigo)
);

create table if not exists aplica(
    produto_lote int not null check(produto_lote >= 0),
    promocao_codigo int not null check(promocao_codigo >= 0),
    foreign key (produto_lote) references produto(lote),
    foreign key (promocao_codigo) references promocao(codigo)
);

create table if not exists historico(
    id int generated always as identity primary key check(id >= 0),
    produto_lote int not null check(produto_lote >= 0),
    foreign key (produto_lote) references produto(lote)
);

create table if not exists item_pedido(
    id int generated always as identity primary key check(id >= 0),
    quantidade int not null default 0 check(quantidade >= 0),
    preco numeric(10,2) not null check(preco >= 0),
    produto_lote int not null check(produto_lote >= 0),
    foreign key (produto_lote) references produto(lote)
);

create table if not exists pedido(
    codigo int generated always as identity primary key check(codigo >= 0),
    dt_pedido date not null,
    item_pedido_id int not null check(item_pedido_id >= 0),
    foreign key (item_pedido_id) references item_pedido(id),
    total numeric(10,2) not null check(total >= 0)
);

create table if not exists fluxo(
    id INT generated always as identity primary key check(id >= 0),
    tipo VARCHAR(10) not null,
    produto_lote int not null check(produto_lote >= 0),
    foreign key (produto_lote) references produto(lote)
);

create table if not exists contem(
    historico_id int not null check(historico_id >= 0),
    fluxo_id int not null,
    foreign key (historico_id) references historico(id),
    foreign key (fluxo_id) references fluxo(id)
);