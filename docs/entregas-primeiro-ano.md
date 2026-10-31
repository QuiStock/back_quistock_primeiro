# Entregas e conferência — 1º ano

Conferência em 31/10/2026 com base no documento **Requisitos por disciplina — Interdisciplinar — Maio2026**, de 14/05/2026, páginas 1 a 5 (somente a seção do 1º ano). A inclusão do perfil da escola foi dispensada pela equipe para esta entrega.

Este índice relaciona requisitos às evidências disponíveis. **Evidência no código** significa implementação identificada por inspeção; não substitui uma demonstração com o banco real. **A confirmar** significa que o material não foi localizado neste repositório e pode existir em outra entrega da equipe.

## Desenvolvimento 1

| Requisito | Situação | Evidência / pendência |
| --- | --- | --- |
| M1 — Landing page sobre o projeto | Evidência no código | [Landing da aplicação](../src/main/webapp/index.html). |
| M2 — Telas de cadastro, consulta, edição e exclusão | Evidência no código | [Administradores](../src/main/webapp/admins.jsp), [gerentes](../src/main/webapp/regional-managers.jsp), [regiões](../src/main/webapp/WEB-INF/views/regioes.jsp), [lojas](../src/main/webapp/WEB-INF/views/lojas.jsp), [produtos](../src/main/webapp/WEB-INF/views/products.jsp), [pedidos](../src/main/webapp/WEB-INF/views/pedido.jsp) e [promoções](../src/main/webapp/WEB-INF/views/promocao.jsp). |
| M3 — Repositório e publicação estática | Publicação a validar após deploy | [Repositório](https://github.com/QuiStock/back_quistock_primeiro) e [site público](https://quistock.github.io/back_quistock_primeiro/). O Pages já estava ativo; foi adicionada uma entrada HTML na raiz para exibir a landing no lugar do README. |
| Extra — Responsividade e fidelidade ao protótipo | Parcialmente verificável | [CSS](../src/main/webapp/css/); confirmar visualmente em celular e desktop e comparar com o protótipo de UX. |
| Documento de entregas com links | Disponível para o escopo conferido | Este índice. Incluir os materiais externos ainda não fornecidos antes da entrega final. |

O GitHub Pages publica a apresentação estática. As telas JSP e os CRUDs funcionais precisam do servidor Java e do PostgreSQL, conforme o [guia de execução](../README.md#como-executar).

## Programação Orientada a Objetos

| Requisito | Situação | Evidência / pendência |
| --- | --- | --- |
| CRUD administrativo em pelo menos três tabelas | Evidência no código | [AdminServlet](../src/main/java/Controller/AdminServlet.java), [RegionalManagerServlet](../src/main/java/Controller/RegionalManagerServlet.java), [RegionTypeServlet](../src/main/java/Controller/RegionTypeServlet.java) e [StoreServlet](../src/main/java/Controller/StoreServlet.java). Demonstrar cada operação com o banco real. |
| Projeto Java no IntelliJ com responsabilidades separadas | Evidência no código | [Controller](../src/main/java/Controller), [DAO](../src/main/java/DAO) e [Model](../src/main/java/Model). O pacote `Controller` exerce o papel dos servlets; confirmar com o professor se exige literalmente o nome `servlet`. |
| PostgreSQL por JDBC | Evidência no código | [ConnectionFactory](../src/main/java/DAO/ConnectionFactory.java) e [dependência PostgreSQL](../pom.xml). |
| Acesso web por HTML e CSS | Evidência no código | [Interface web](../src/main/webapp). |
| Servlet para requisições HTTP | Evidência no código | [Servlets em Controller](../src/main/java/Controller), com `doGet`, `doPost` e `@WebServlet`. |
| Extra — CRUD em mais três tabelas | A confirmar com o professor | Há sete módulos CRUD, mas pedidos e promoções compartilham a tabela `suggestion`: são seis tabelas distintas (`super_admin`, `regional_manager`, `region_type`, `store`, `product`, `suggestion`). Confirmar se as seis atendem ao critério de tabelas administrativas. |
| Extra — Alterar/excluir por diferentes campos | Sem evidência suficiente | A interface exibe os dados ao usuário, mas as alterações e exclusões usam principalmente IDs. Não considerar automaticamente atendido. |
| Extra — Diferentes filtros nas listagens e correlação entre tabelas | Parcial | Há JOINs e associação de produtos, lojas e regiões nos [DAOs](../src/main/java/DAO); não foram identificados filtros de listagem selecionáveis pelo usuário. |

## Sistemas Operacionais

| Requisito | Situação | Evidência / pendência |
| --- | --- | --- |
| Planilha: tarefas por integrante e quantidade entregue | A confirmar | Fornecer o arquivo ou link da planilha real da equipe. Histórico de commits não comprova todas as tarefas do projeto. |
| Planilha: percentual de conclusão e tarefas por situação | A confirmar | Conferir fórmulas e critérios de conclusão na planilha. |
| Planilha: prazos e manipulação de datas | A confirmar | Conferir datas previstas, entregas e cálculos de prazo. |
| Planilha: validação de dados e proteção de edição | A confirmar | Demonstrar as restrições e a proteção das células de cálculo. |
| Variáveis de ambiente para banco e APIs utilizadas | Evidência no código para o banco | [Modelo de ambiente](../.env.example), [carregamento](../src/main/java/DAO/ConnectionFactory.java) e [instruções](../README.md#4-configure-a-conexão). Não foi identificada integração com API externa neste aplicativo Java. |
| Git/GitHub e inclusão dos integrantes | Parcialmente verificável | [Histórico real](https://github.com/QuiStock/back_quistock_primeiro/commits/main/) e [contribuidores](https://github.com/QuiStock/back_quistock_primeiro/graphs/contributors). Confirmar a lista de membros com acesso nas configurações: autor de commit e colaborador são coisas diferentes. |
| Conta da escola como colaboradora | Dispensado pela equipe | Exceção solicitada para a entrega deste ano. |
| Extra — Gráficos na planilha | A confirmar | Depende da planilha real. |
| Extra — REGEX para CPF e/ou celular | Não identificado | Há REGEX de e-mail, nomes e senhas, mas não há campos de CPF/celular no escopo atual. Isso não comprova o extra específico. |

### Cuidados aplicados ao repositório

- `.env` e suas variantes locais ignorados, preservando `.env.example` como modelo público.
- Metadados de Eclipse e IntelliJ retirados do índice Git, preservando os arquivos locais.
- O histórico anterior continua existindo. A remoção do índice não apaga dados que tenham sido publicados em commits antigos.

## Lógica de Programação

| Requisito | Situação | Evidência / pendência |
| --- | --- | --- |
| M1 — Organização, indentação e nomes | Parcial | Há separação de camadas. Persistem nomes legados, como `store_id`, `Sugestion` e pacotes com inicial maiúscula; a conformidade final com o padrão das aulas precisa de revisão. |
| M2 — Comentários e organização de arquivos | Evidência parcial | As camadas estão organizadas e há comentários. Muitos comentários são genéricos e não explicam regras; quantidade de comentários não garante qualidade. |
| M3 — Tratamento de exceções | Evidência no código, com ressalvas | Os servlets tratam erros de banco e validação. [RegionTypeServlet](../src/main/java/Controller/RegionTypeServlet.java) ainda converte IDs diretamente sem tratamento específico de `NumberFormatException`. |
| Extra — Validação e nomenclatura | Parcial | [Validações de produto](../src/main/java/Controller/ProductServlet.java), [pedido](../src/main/java/Controller/OrderServlet.java), [promoção](../src/main/java/Controller/SaleServlet.java) e [contas](../src/main/java/Controller/AdminServlet.java). Revisar limites e nomes legados. |

## Banco de Dados 1

| Requisito | Situação | Evidência / pendência |
| --- | --- | --- |
| Modelo conceitual | A confirmar | Fornecer o diagrama ou link. |
| Modelo lógico | A confirmar | Fornecer o diagrama ou link. |
| Script SQL de criação | Não disponível nesta branch | Fornecer o DDL compatível com os DAOs atuais, incluindo `suggestions_vw`. Não reconstruir o esquema a partir de suposições. |
| Banco independente do 2º ano | A confirmar | Confirmar a separação dos bancos utilizados pela equipe. |
| Extra — DEFAULT e CHECK | A confirmar | Conferir no DDL real. |
| Extra — DataLoad inicial | A confirmar | Fornecer uma carga fictícia, compatível com o DDL e sem senhas reais. |
| Extra — Convenção de nomenclatura | A confirmar | Fornecer a convenção aprovada pela equipe e verificar sua aplicação nos modelos e scripts. |

## Outras disciplinas do 1º ano

| Disciplina | Materiais a confirmar |
| --- | --- |
| Introdução à IA | Memorial com etapas, tarefas, prompts reais, resultados, ferramentas, trabalho sem IA, validação crítica, limitações, privacidade e responsabilidade. O extra exige um GEM especializado com proposta e instruções documentadas. |
| UX | Apresentação de pesquisa qualitativa/quantitativa, CSD, benchmark, desk research, persona, empatia, jornada, sitemap, testes, protótipo web de alta fidelidade e análise heurística. Extras: priorização pós-testes, UI Kit e V2. |
| Metodologia | Termos de abertura e encerramento, ferramenta de planejamento e cronograma. Extras: recursos e alinhamento com empresas do grupo. |

Esses materiais não foram localizados neste repositório; isso não significa que a equipe não os tenha produzido. Vincular as entregas reais e verificar acesso aos links com a conta do avaliador.

## Antes da apresentação

1. Validar a landing publicada e seus recursos no endereço público.
2. Rodar a aplicação Java com o banco correto e demonstrar cadastro, listagem, edição e exclusão.
3. Conferir os membros com acesso ao GitHub; não é necessário adicionar a conta da escola nesta entrega.
4. Vincular e conferir a planilha de SOP e os documentos externos.
5. Separar os mínimos comprovados dos extras ainda incompletos. Não há garantia de nota apenas pela inspeção do repositório.
