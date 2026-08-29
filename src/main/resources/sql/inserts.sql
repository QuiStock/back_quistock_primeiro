-- primeiro
insert into super_admin(nome, email, senha) values('Bruno Siqueira Broslavschi Pagan', 'brunopagan1909@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Henry de Sá Rodrigues', 'henrydsrodrigues07@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Pedro Morais Lionço', 'pedromlionco11@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Nathália Melo Humpel', 'nathaliagerminare@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Stefany Hikaru Iwashita', 'hikaruiwaa@gmail.com', 'fundamento revista marinheiro gentileza');
 
-- segundo
insert into super_admin(nome, email, senha) values('Rodrigo Bolgheroni', 'rodrigobolgheroni1@gmail.com', 'ornitorrinco lixo bateria filme');
insert into super_admin(nome, email, senha) values('Beatriz Frisina Battista', 'beatriz.battista2010@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Luigi Azevedo Biondo', 'luigibiondo2207@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Lucas Kluska Donini', 'lkd.lucas@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Enzo Christowam Joaquim', 'enzojoaquim85@gmail.com', 'fundamento revista marinheiro gentileza');
insert into super_admin(nome, email, senha) values('Isabella Candido de Camargo', 'isacandido.camargo009@gmail.com', 'fundamento revista marinheiro gentileza');


--loja
insert into loja(email, senha) values(
    'swiftpiracicabax@gmail.com', 'jobatis2k26@'
);
insert into loja(email, senha) values(
    'swiftamparocimao@gmail.com', 'pitsam2k20@'
);


--endereço
insert into endereco(cep, pais, rua, numero, cidade, estado, loja_id) values(
    '13489-999', 'Brasil', 'Limeira', 167, 'São Paulo', 'SP', 1
);
insert into endereco(cep, pais, rua, numero, cidade, estado, loja_id) values(
    '13900-400', 'Brasil', 'Avenida Bernardino de Campos', 169, 'Amparo', 'SP', 2
);


--região
insert into regiao(nome, loja_id) values(
    'Norte', 1
);
insert into regiao(nome, loja_id) values(
    'Sul', 2
);


--promoção
insert into promocao(percentual, dt_fim, dt_promo) values(
    20, '2026-09-10', '2026-09-01'
);
insert into promocao(percentual, dt_fim, dt_promo) values(
    10, '2026-09-01', '2026-08-20'
);
insert into promocao(percentual, dt_fim, dt_promo) values(
    50, '2026-08-25', '2026-08-30'
);
insert into promocao(percentual, dt_fim, dt_promo) values(
    90, '2026-08-26', '2026-08-28'
);


--gerente regional
insert into gerente_regional(nome, email, senha, regiao_id) values(
    'Jéssica Morais', 'jessica1990@gmail.com', 'J10011990@', 1
);
insert into gerente_regional(nome, email, senha, regiao_id) values(
    'Victor Melo', 'victorMelo0912@gmail.com', 'meloO.', 1
);
insert into gerente_regional(nome, email, senha, regiao_id) values(
    'Fernanda Rodrigues', 'ferodrigues2020@gmail.com', '2001fe@', 2
);


--produto
insert into produto values (
    122323, 'Nuggets de Frango', 9.99, '2026-11-29', 'Seara', 'Industrializados', 67, 2
);

insert into produto values (
    122324, 'Hambúrguer Bovino', 18.90, '2026-12-15', 'Friboi', 'Bovinos', 45, 1
);

insert into produto values (
    122325, 'Filé de Frango', 24.99, '2026-10-28', 'Seara', 'Aves', 38, 2
);

insert into produto values (
    122326, 'Picanha Bovina', 69.90, '2026-12-05', 'Friboi', 'Bovinos', 22, 1
);

insert into produto values (
    122327, 'Linguiça Toscana', 21.50, '2026-11-18', 'Friboi', 'Suínos', 31, 2
);

insert into produto values (
    122328, 'Almôndegas ao Molho', 16.90, '2026-10-30', 'Swift', 'Pratos Prontos', 27, 1
);

insert into produto values (
    122329, 'Batata Palito Congelada', 14.99, '2027-01-20', 'Swift', 'Congelados', 52, 2
);

insert into produto values (
    122330, 'Costela Bovina', 49.90, '2026-12-12', 'Friboi', 'Bovinos', 18, 1
);

insert into produto values (
    122331, 'Frango Empanado', 19.90, '2026-11-25', 'Swift', 'Industrializados', 41, 2
);

insert into produto values (
    122332, 'Carne Moída Bovina', 29.90, '2026-12-01', 'Friboi', 'Bovinos', 35, 1
);


--Aplica
insert into aplica values(
    122325, 4
);

insert into aplica values(
    122327, 4
);

insert into aplica values(
    122323, 1
);

insert into aplica values(
    122331, 2
);

insert into aplica values(
    122325, 3
);


--historico
insert into historico(produto_lote) values(
    122325
);
insert into historico(produto_lote) values(
    122327
);
insert into historico(produto_lote) values(
    122323
);
insert into historico(produto_lote) values(
    122331
);


--item pedido
insert into item_pedido(quantidade, preco, produto_lote) values(
    2, 49.98, 122325
);
insert into item_pedido(quantidade, preco, produto_lote) values(
    3, 56.70, 122324
);

insert into item_pedido(quantidade, preco, produto_lote) values(
    1, 69.90, 122326
);


--pedido
insert into pedido(dt_pedido, item_pedido_id, total) values(
    '2026-08-29', 2, 56.70
);
insert into pedido(dt_pedido, item_pedido_id, total) values(
    '2026-08-29', 1, 49.80
);
insert into pedido(dt_pedido, item_pedido_id, total) values(
    '2026-08-29', 3, 69.90
);


--fluxo
insert into fluxo(tipo, produto_lote) values(
    'vermelho', 122325
);

insert into fluxo(tipo, produto_lote) values(
    'vermelho', 122327
);

insert into fluxo(tipo, produto_lote) values(
    'vermelho', 122331
);

insert into fluxo(tipo, produto_lote) values(
    'amarelo', 122323
);

insert into fluxo(tipo, produto_lote) values(
    'amarelo', 122332
);

insert into fluxo(tipo, produto_lote) values(
    'amarelo', 122326
);

insert into fluxo(tipo, produto_lote) values(
    'amarelo', 122330
);

insert into fluxo(tipo, produto_lote) values(
    'verde', 122328
);

insert into fluxo(tipo, produto_lote) values(
    'verde', 122324
);

insert into fluxo(tipo, produto_lote) values(
    'verde', 122329
);


--contem
insert into contem(historico_id, fluxo_id) values(
    2, 1
);
