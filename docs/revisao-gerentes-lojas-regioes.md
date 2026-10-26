# Revisão de RegionalManager, Store e RegionType

Revisão estática da branch `feat/loading-screen`, em 25/10/2026.

No código desta branch, a entidade correspondente a GeneralManager chama-se
`RegionalManager`, com tabela `regional_manager` e rota `/regionalManagers`.
Não existe uma segunda implementação chamada `GeneralManager`.

## Escopo e evidência

Foram examinados os três Models, Controllers e DAOs, os JSPs existentes,
a navegação que leva a essas telas e a `ConnectionFactory`.
Não existem Services específicos desses três cadastros: os Controllers
chamam diretamente os DAOs. Isso, por si só, não impede o CRUD.
Login e Admin só aparecem neste relatório quando bloqueiam a compilação
ou recebem incorretamente uma operação da tela de gerentes.

Não houve alteração de código funcional, dependências ou `pom.xml`.
A tentativa de compilação offline parou por dependências de plugin ausentes,
antes de compilar Java. A tentativa posterior com acesso à rede foi interrompida.
Os erros Java abaixo foram identificados pela leitura do código, não por uma
compilação concluída. Não existe `src/test` nesta branch.

Não foram executadas requisições HTTP nem operações no banco. Não há `.env`
local nem as variáveis `url`, `user` e `password` neste processo.
O esquema efetivo do PostgreSQL não foi inspecionado.

## Problemas encontrados

### 1. Crítico: excluir gerente envia a operação para a tabela de admins

- Local: `src/main/webapp/regional-managers.jsp:215`.
- O formulário de exclusão faz POST para `/admins`, com `id` do gerente.
- `AdminServlet.delete`, linha 96, passa esse ID para `AdminDAO.delete`, que
  executa `DELETE FROM super_admin WHERE id = ?`.
- Consequência: se houver um admin com o mesmo ID, ele pode ser excluído;
  o gerente permanece. Sem coincidência de ID, a operação ainda redireciona
  para admins, sem excluir o gerente.
- Ajuste necessário: enviar a exclusão para `/regionalManagers`.
- Reprodução: inspecionar o formulário no HTML/DevTools. Não é necessário
  submeter esse formulário para confirmar o destino incorreto.

### 2. Bloqueador: Controller de gerente não corresponde ao Model

- `RegionalManagerServlet.java:90`: o construtor recebe
  `req.getParameter("region-type")`, que é `String`; o construtor de quatro
  argumentos de `RegionalManager.java:16` exige `int` no último parâmetro.
- `RegionalManagerServlet.java:116`: chama `setRegionType`, mas o Model tem
  `regionTypeName` e `regionTypeId`. O Lombok não gera `setRegionType`.
- Consequência: essa implementação não compila. Para persistir a associação,
  é preciso interpretar e validar o ID, e preencher `regionTypeId`.
- Não basta mudar o nome da região: o DAO grava `getRegionTypeId()`.

### 3. Bloqueador: páginas de cadastro/edição não são encontradas

- `RegionalManagerServlet.java:30,45` encaminha para
  `/crud-regionalManager.jsp`, mas o arquivo existente é `crud-managers.jsp`.
- `StoreServlet.java:65,100` encaminha para `stores.jsp` e `editStore.jsp`;
  nenhum dos dois existe nesta branch.
- `RegionTypeServlet.java:64,94` encaminha para `regionTypes.jsp` e
  `editRegionType.jsp`; nenhum dos dois existe nesta branch.
- Store e RegionType também não têm tratamento específico de GET `action=new`:
  esse valor cai na listagem.
- Consequência: mesmo com o banco acessível, os fluxos visuais estão incompletos.
  Um POST válido pode gravar e redirecionar para uma listagem cujo JSP não existe.
  Portanto, falha na página depois do POST não prova que a gravação falhou.

### 4. Alto: formulário de gerente não consegue editar nem selecionar região

Arquivo: `src/main/webapp/crud-managers.jsp`.

- Linha 7 envia `action=update`; o Controller aceita `action=edit` no POST
  (`RegionalManagerServlet.java:75`). Com `update`, ele volta à listagem,
  sem atualizar o registro.
- Não existe campo `region-type`; o Controller espera esse parâmetro ao
  cadastrar e editar. Também não carrega opções de `RegionTypeDAO` para o JSP.
- Linha 4 consulta `empty admin`, embora o atributo enviado seja `manager`;
  o título pode continuar como "Adicionar gerente" durante a edição.
- Linha 15 usa `this.closest('dialog').close()`, mas o formulário é acessado
  como página e não há um diálogo envolvendo-o. Cancelar gera erro JavaScript.
- Ajuste necessário: alinhar ação, atributos, campo de região e fluxo de retorno.

### 5. Alto: listagem de gerentes usa propriedade inexistente

- `regional-managers.jsp:211` acessa `${manager.regionType}`.
- O Model e o JOIN do DAO disponibilizam `regionTypeName` e `regionTypeId`.
- Consequência: com pelo menos um gerente, a resolução da propriedade pode
  interromper a renderização do JSP. Com lista vazia, o erro fica escondido.
- Ajuste necessário: exibir `regionTypeName` e testar com lista preenchida.

### 6. Alto: validações do servidor estão ausentes

- Os três Controllers repassam texto dos parâmetros ao Model/DAO sem validar
  obrigatoriedade, conteúdo em branco, formato de e-mail ou limites do banco.
- `required` e `type=email` do HTML não protegem requisições feitas diretamente.
- Não há verificação prévia de existência da loja ao cadastrar/editar região,
  nem de existência da região ao cadastrar/editar gerente.
- Se houver constraints no banco, dados inválidos podem resultar em erro SQL
  e HTTP 500. Sem constraints suficientes, podem ser persistidos.
- IDs numéricos negativos não são rejeitados pela aplicação.
- O servidor deve rejeitar entradas inválidas antes de gravar. As constraints
  do banco continuam necessárias para garantir integridade e concorrência.
- A regra de unicidade de e-mail do gerente e de nome da região precisa ser
  confirmada no esquema/regra de negócio; não foi presumida nesta revisão.

### 7. Alto: IDs e ações inválidos não têm tratamento consistente

- `RegionalManagerServlet.java:35`: `Integer.parseInt` no GET sem tratamento
  de `NumberFormatException`.
- `RegionalManagerServlet.java:73`: POST sem `action` faz `switch(null)`.
- No mesmo Controller, ação desconhecida chama `doGet`, aparentando sucesso.
- `StoreServlet.java:94,113,132` e
  `RegionTypeServlet.java:73,88,104,106,121`: conversões de ID podem lançar
  `NumberFormatException` sem tratamento funcional.
- Store e RegionType não têm `default` no POST: uma ação ausente ou desconhecida
  termina sem operação, normalmente com resposta vazia de sucesso.
- Critério sugerido para os testes: HTTP 400 e nenhum registro alterado.

### 8. Alto: atualização/exclusão podem informar sucesso sem alterar nada

- Os três DAOs ignoram o número de linhas retornado por `executeUpdate()`.
- Store e RegionType não verificam existência antes do UPDATE/DELETE.
- As buscas para edição de Store e RegionType encaminham `null` ao JSP,
  sem resposta explícita de registro não encontrado.
- Gerente verifica existência antes de editar, mas não antes de excluir;
  também não verifica se a linha desapareceu entre a leitura e o UPDATE.
- Consequência: um ID inexistente pode terminar em redirect de sucesso.
- Critério sugerido: HTTP 404 para registro inexistente e nenhum falso sucesso;
  conferir também a quantidade de linhas afetadas no DAO.

### 9. Alto: relacionamentos e conflitos SQL não têm fluxo de erro para o usuário

- Relacionamento pressuposto pelos DAOs:
  `regional_manager.region_type_id -> region_type.id` e
  `region_type.store_id -> store.id`.
- Se o banco impedir excluir uma loja com regiões ou uma região com gerentes,
  a exceção chega como HTTP 500. Não há mensagem funcional nem recuperação do
  formulário com os valores preenchidos.
- Duplicidade e limites de coluna também não recebem tratamento específico.
- O Controller de gerente repassa `SQLException.getMessage()` ao cliente
  (`RegionalManagerServlet.java:55,82`), expondo detalhes do banco.
- Critério sugerido: mensagem clara de conflito, sem gravação parcial ou
  exclusão dos filhos. Para requisições diretas, HTTP 409 é uma opção.
- A política exata de exclusão depende das FKs reais. Não há evidência para
  afirmar que o banco atual usa RESTRICT, CASCADE ou SET NULL.

### 10. Alto: dados de gerente são exibidos sem escape e a senha é exposta

- `regional-managers.jsp:209,210` escreve nome/e-mail diretamente com EL.
- `crud-managers.jsp:9-11` insere valores em atributos HTML sem escape.
- Conteúdo controlado pelo cadastro pode ser interpretado como HTML/atributos.
  Usar escape apropriado, por exemplo `c:out` nos locais correspondentes.
- O formulário usa `type=text` e preenche a senha atual no HTML.
- `StoreDAO` e `RegionalManagerDAO` gravam a senha recebida diretamente,
  sem transformação visível. Isso deve ser considerado no tratamento desses
  cadastros; uma mudança de armazenamento precisa manter compatibilidade
  com o consumidor da senha, fora do escopo desta revisão.
- Na edição, os Controllers substituem a senha pelo parâmetro recebido;
  definir e testar a regra para senha vazia, em vez de apagar silenciosamente.

### 11. Médio: navegação e modal de gerentes estão quebrados

- `regional-managers.jsp:183,184` e `home-admin.jsp:58,59` apontam Loja e Região
  para `loja.jsp`/`regiao.jsp`, arquivos inexistentes, em vez de `/stores`
  e `/regionTypes`.
- `regional-managers.jsp:227,247` procura `modal-admin`, inexistente, e chama
  `addEventListener` em `null`. Não há links com `data-modal` nem `<dialog>`
  correspondentes ao script.
- Consequência: links não completam a navegação e o console apresenta erro.

### 12. Integração com o banco ainda não é verificável neste checkout

- Os DAOs esperam tabelas/colunas em inglês, descritas no roteiro abaixo.
- Não existe DDL local nesta branch para confirmar o contrato.
- O DDL disponível na referência local `origin/main`, em
  `src/main/resources/sql/creates.sql`, usa `loja`, `regiao`,
  `gerente_regional`, `codigo`, `nome` e `senha`.
- Se esse DDL ainda representar o banco usado, os DAOs não são compatíveis.
  Isso é uma incompatibilidade com o script histórico, não confirmação de
  incompatibilidade com o banco remoto atual.
- `ConnectionFactory` depende de `url`, `user` e `password`, vindos do ambiente
  ou do `.env` procurado pelo processo. O diretório de execução do servidor
  precisa ser considerado; um `.env` no projeto pode não ser encontrado pelo
  processo do servidor.
- Os DAOs usam `PreparedStatement` para os valores e `try-with-resources`,
  pontos corretos no gerenciamento das consultas/conexões.

### 13. Bloqueios externos que impedem gerar o WAR inteiro

Citados apenas porque impedem publicar e testar os três cadastros:

- `DAO/AdminDAO.java:76`: expressão `return rs.next() ?` incompleta.
- `Service/LoginService.java:40`: variável `admins` não declarada.
- `Service/LoginService.java:43`: `new LoginResult(true, "ADMIN")` incompatível
  com o Model atual, que recebe um `LoggedUser`.
- `Controller/LoginServlet.java:46` e chamadas seguintes: `getType()` não existe
  em `LoginResult`.

Não foram feitas correções nem ampliada a revisão desses fluxos.

## Roteiro exato de testes

Os passos a seguir são um roteiro para execução; não representam testes
aprovados nesta revisão. Os casos de aceitação dependem de resolver os
bloqueios de compilação e de disponibilizar os JSPs. Quando indicado, é possível
testar o POST diretamente mesmo que a listagem ainda esteja quebrada.

### A. Preparar o ambiente e confirmar o esquema

1. Use um banco de teste e o servidor Jakarta já usado pelo projeto.
2. Configure `url`, `user` e `password` para o processo desse servidor,
   conforme `.env.example`. Não é necessário editar o `pom.xml`.
3. Compile/publice pela configuração já existente na IDE. Se as dependências
   não estiverem disponíveis localmente, a compilação ficará pendente;
   nenhum comando de download faz parte deste roteiro.
4. Defina no PowerShell a URL real da aplicação. Exemplo: se o WAR foi publicado
   como `demo-1.0-SNAPSHOT.war` na porta 8080:

```powershell
$base = 'http://localhost:8080/demo-1.0-SNAPSHOT'
```

Se a IDE usa outro contexto ou porta, substitua o valor. O repositório não
define uma URL de publicação única. Use `curl.exe`, não o alias `curl` do
PowerShell. Os comandos não usam `-L`: o HTTP 302 do POST deve ser conferido
separadamente do GET seguinte, que pode falhar por falta de JSP.

5. No editor SQL conectado ao mesmo banco, execute somente estas consultas
   de diagnóstico:

```sql
SELECT current_database(), current_schema(), current_user;
SHOW search_path;

SELECT table_schema, table_name, column_name, data_type,
       is_nullable, column_default, is_identity, character_maximum_length
FROM information_schema.columns
WHERE table_name IN ('store', 'region_type', 'regional_manager',
                     'loja', 'regiao', 'gerente_regional')
ORDER BY table_schema, table_name, ordinal_position;

SELECT n.nspname AS schema_name, t.relname AS table_name,
       c.conname, pg_get_constraintdef(c.oid) AS definition
FROM pg_constraint c
JOIN pg_class t ON t.oid = c.conrelid
JOIN pg_namespace n ON n.oid = t.relnamespace
WHERE t.relname IN ('store', 'region_type', 'regional_manager')
ORDER BY n.nspname, t.relname, c.conname;
```

Contrato esperado pelo código atual:

| Tabela | Colunas usadas |
| --- | --- |
| `store` | `id`, `email`, `password` |
| `region_type` | `id`, `name`, `store_id` |
| `regional_manager` | `id`, `name`, `email`, `password`, `region_type_id` |

Os INSERTs omitem `id`; confirme que o banco o gera automaticamente.
Confira as FKs e os limites nas consultas acima. Se as tabelas estiverem em
outro schema, confirme que esse schema está no `search_path` da conexão da
aplicação. Pare aqui se o contrato for diferente: os testes seguintes usam
os nomes em inglês dos DAOs, não criam nem renomeiam tabelas.

### B. Criar duas lojas e conferir cadastro/listagem

Execute os dois cadastros, uma única vez:

```powershell
curl.exe -i "$base/stores" --data-urlencode 'action=register' --data-urlencode 'storeEmail=qa.loja.a@example.com' --data-urlencode 'storePassword=TesteLocal123!'
curl.exe -i "$base/stores" --data-urlencode 'action=register' --data-urlencode 'storeEmail=qa.loja.b@example.com' --data-urlencode 'storePassword=TesteLocal123!'
curl.exe -i "$base/stores"
```

Esperado: cada POST retorna 302 para `stores`; a consulta abaixo encontra uma
linha para cada e-mail; a listagem retorna 200 e mostra ambas as lojas.
Atualmente o GET encaminha para `stores.jsp`, inexistente.

```sql
SELECT id, email FROM store
WHERE email IN ('qa.loja.a@example.com', 'qa.loja.b@example.com')
ORDER BY id;
```

Anote os IDs reais no PowerShell; os valores abaixo são exemplos e devem ser
substituídos pelos IDs retornados:

```powershell
$lojaA = 101
$lojaB = 102
```

No navegador, abra a URL `$base/stores`. Confira estado vazio em um banco de
teste sem lojas e estado preenchido depois dos cadastros. A senha não deve
aparecer na tabela nem no HTML da listagem.

### C. Editar loja

```powershell
curl.exe -i "$base/stores?action=edit&storeId=$lojaA"
curl.exe -i "$base/stores" --data-urlencode 'action=edit' --data-urlencode "storeId=$lojaA" --data-urlencode 'storeEmail=qa.loja.a.editada@example.com' --data-urlencode 'storePassword=TesteLocal456!'
```

Esperado: GET 200 com a loja correta; POST 302; mesmo ID com novo e-mail.
O GET atual depende de `editStore.jsp`, inexistente. Confirme a gravação:

```sql
SELECT id, email FROM store
WHERE email IN ('qa.loja.a@example.com', 'qa.loja.a.editada@example.com');
```

Deve existir somente a versão editada, com o ID original. Atualize a página
de listagem e confirme que não ocorre novo POST nem criação duplicada.
Para conferir a troca de senha na implementação atual, sem imprimir a senha:

```sql
SELECT id, password = 'TesteLocal456!' AS senha_atualizada
FROM store WHERE email = 'qa.loja.a.editada@example.com';
```

Esperado no armazenamento atual: `senha_atualizada = true`. Esse teste de
igualdade direta só vale enquanto a senha for gravada como recebida pelo DAO;
se o armazenamento passar a usar hash, a verificação deve usar o verificador
de senha correspondente, sem exigir igualdade com o texto original.

### D. Cadastrar e listar duas regiões

```powershell
curl.exe -i "$base/regionTypes" --data-urlencode 'action=register' --data-urlencode 'regionTypeName=QA Norte' --data-urlencode "regionTypeStoreId=$lojaA"
curl.exe -i "$base/regionTypes" --data-urlencode 'action=register' --data-urlencode 'regionTypeName=QA Sul' --data-urlencode "regionTypeStoreId=$lojaB"
curl.exe -i "$base/regionTypes"
```

Esperado: POSTs 302, regiões vinculadas às respectivas lojas e GET 200.
Atualmente a listagem depende de `regionTypes.jsp`, inexistente.

```sql
SELECT r.id, r.name, r.store_id, s.email AS store_email
FROM region_type r
JOIN store s ON s.id = r.store_id
WHERE r.name IN ('QA Norte', 'QA Sul')
ORDER BY r.id;
```

Anote os IDs reais, substituindo os exemplos:

```powershell
$regiaoNorte = 201
$regiaoSul = 202
```

### E. Editar região, incluindo troca de loja

```powershell
curl.exe -i "$base/regionTypes?action=edit&regionTypeId=$regiaoNorte"
curl.exe -i "$base/regionTypes" --data-urlencode 'action=edit' --data-urlencode "regionTypeId=$regiaoNorte" --data-urlencode 'regionTypeName=QA Norte Editada' --data-urlencode "regionTypeStoreId=$lojaB"
```

Esperado: GET carrega nome e loja originais; POST 302; mesmo ID agora aponta
para `$lojaB`. O GET atual depende de `editRegionType.jsp`, inexistente.
Repita o JOIN da seção D, filtrando também `QA Norte Editada`, para conferir.

### F. Cadastrar, listar e abrir edição de gerente

O POST abaixo segue a ação e o nome de parâmetro esperados pelo Controller.
Ele só poderá executar depois de corrigir a incompatibilidade com o Model.

```powershell
curl.exe -i "$base/regionalManagers?action=new"
curl.exe -i "$base/regionalManagers" --data-urlencode 'action=add' --data-urlencode 'name=QA Gerente Árvore' --data-urlencode 'email=qa.gerente@example.com' --data-urlencode 'password=TesteLocal123!' --data-urlencode "region-type=$regiaoNorte"
curl.exe -i "$base/regionalManagers"
```

Esperado: formulário de novo gerente acessível, seleção de região obrigatória,
POST 302 e listagem 200 mostrando nome com acento e nome da região.
O nome da região deve ser `QA Norte Editada`, obtido pelo relacionamento.
Hoje o GET de novo gerente aponta para JSP inexistente e a listagem usa
uma propriedade inexistente do Model.

```sql
SELECT m.id, m.name, m.email, m.region_type_id,
       r.name AS region_name, r.store_id
FROM regional_manager m
JOIN region_type r ON r.id = m.region_type_id
WHERE m.email = 'qa.gerente@example.com';
```

Anote o ID real, substituindo o exemplo:

```powershell
$gerente = 301
curl.exe -i "$base/regionalManagers?action=edit&id=$gerente"
```

Esperado: gerente correto, região atual selecionada e senha atual ausente
do HTML. Confirme que a listagem funciona tanto vazia quanto preenchida.

### G. Editar gerente e trocar região

```powershell
curl.exe -i "$base/regionalManagers" --data-urlencode 'action=edit' --data-urlencode "id=$gerente" --data-urlencode 'name=QA Gerente Editado' --data-urlencode 'email=qa.gerente.editado@example.com' --data-urlencode 'password=TesteLocal456!' --data-urlencode "region-type=$regiaoSul"
```

Esperado: POST 302, mesmo ID, novo nome/e-mail e `region_type_id` de QA Sul.
Repita a consulta da seção F com o novo e-mail. Depois repita pelo formulário
do navegador: ambos os caminhos devem produzir a mesma alteração.
Hoje o formulário envia `update`, diferente do `edit` aceito no Controller.

Confira também a troca de senha no armazenamento atual:

```sql
SELECT id, password = 'TesteLocal456!' AS senha_atualizada
FROM regional_manager WHERE email = 'qa.gerente.editado@example.com';
```

Esperado: `senha_atualizada = true`, com a mesma ressalva sobre hash da seção C.

Também renomeie uma região já associada e confira que o novo nome aparece na
listagem de gerentes sem editar o gerente: o DAO consulta o nome pelo JOIN.

### H. Testar validação diretamente, sem depender do HTML

Execute em banco de teste. Cada caso rejeitado deve manter o estado anterior
das três tabelas. O comportamento sugerido é 400 para entrada inválida,
404 para registro inexistente e 409 para conflito de integridade.
Se o produto usar páginas de erro com outro contrato, preserve a rejeição
clara e a ausência de alterações, em vez de tratar HTTP 200 vazio como sucesso.
Se um caso inválido for aceito, registre o ID criado para removê-lo depois;
os comandos de limpeza da seção J cobrem os IDs dos casos válidos.

Exemplos prontos para executar:

```powershell
# E-mail inválido e senha vazia: deve rejeitar sem cadastrar loja.
curl.exe -i "$base/stores" --data-urlencode 'action=register' --data-urlencode 'storeEmail=email-invalido' --data-urlencode 'storePassword='

# Nome em branco: deve rejeitar sem cadastrar região.
curl.exe -i "$base/regionTypes" --data-urlencode 'action=register' --data-urlencode 'regionTypeName=   ' --data-urlencode "regionTypeStoreId=$lojaA"

# Nome em branco: deve rejeitar sem cadastrar gerente.
curl.exe -i "$base/regionalManagers" --data-urlencode 'action=add' --data-urlencode 'name=   ' --data-urlencode 'email=qa.invalido@example.com' --data-urlencode 'password=TesteLocal123!' --data-urlencode "region-type=$regiaoSul"

# ID não numérico: deve retornar 400.
curl.exe -i "$base/stores?action=edit&storeId=abc"
curl.exe -i "$base/regionTypes?action=edit&regionTypeId=abc"
curl.exe -i "$base/regionalManagers?action=edit&id=abc"

# Ação ausente/desconhecida: deve retornar 400, sem alteração.
curl.exe -i "$base/stores" --data-urlencode 'storeEmail=qa.acao@example.com'
curl.exe -i "$base/regionTypes" --data-urlencode 'action=inexistente'
curl.exe -i "$base/regionalManagers" --data-urlencode 'name=QA Sem Acao'
```

Para completar a cobertura, repita as requisições válidas de cadastro E edição
das seções B–G com uma variação por vez:

| Caso | Variação exata | Resultado a conferir |
| --- | --- | --- |
| Campos obrigatórios | Remover cada parâmetro de texto obrigatório, depois enviar `''` e depois `'   '` | Rejeição sem escrita; sem HTTP 500 |
| E-mail | Enviar `email-invalido` em `storeEmail` e em `email` | Rejeição nos dois cadastros |
| Espaços no texto | Enviar nome/e-mail com espaços externos | Regra explícita de normalização; senha não deve ser alterada silenciosamente por trim |
| Limite das colunas | Repetir um caractere até o limite retornado pela consulta A; depois limite + 1 | No limite funciona; excedente é rejeitado sem truncamento silencioso |
| ID da operação | Em `storeId`, `regionTypeId` ou `id`, usar `abc`, omitir, usar `-1` e `2147483648` | 400, nenhuma alteração |
| ID inexistente | Usar um inteiro positivo que a consulta por ID confirme ausente | GET de edição, POST de edição e exclusão retornam 404 |
| Loja da região | Em `regionTypeStoreId`, omitir, usar `abc`, `-1` e ID positivo ausente | Rejeição e nenhuma região órfã |
| Região do gerente | Em `region-type`, omitir, usar `abc`, `-1` e ID positivo ausente | Rejeição e nenhum gerente órfão |
| Ações | Em cada endpoint, omitir `action` e enviar `action=inexistente` no POST | 400 sem sucesso vazio |
| E-mail duplicado | Repetir e-mail de loja; para gerente, repetir se a regra real exigir unicidade | Rejeição clara se houver regra; conferir constraint e quantidade de linhas |
| Senha na edição | Enviar vazia e depois omitir o parâmetro | Aplicar regra definida de preservar ou rejeitar; não apagar a senha sem intenção |
| Escape HTML | No nome de gerente, enviar `QA <b>literal</b> "aspas"` | Nome exibido como texto literal, inclusive no formulário, sem criar elementos/atributos |
| Acentos | Enviar `João Região São Luís` | Mesmo texto no banco, formulário e listagem |

Para construir um valor longo no PowerShell e enviá-lo com curl:

```powershell
# Substitua 100 pelo limite real da coluna consultado em A.
$nomeLongo = 'A' * 101
curl.exe -i "$base/regionTypes" --data-urlencode 'action=register' --data-urlencode "regionTypeName=$nomeLongo" --data-urlencode "regionTypeStoreId=$lojaA"
```

Na implementação atual, vários testes acima ainda falham: não há validação
de conteúdo no servidor, parte dos IDs gera exceção e ações inválidas podem
resultar em página de listagem ou resposta vazia.

### I. Excluir com dependentes e testar integridade

Neste ponto, as regiões de teste estão associadas à loja B e o gerente está
associado à região Sul. Confira os JOINs antes de executar.

```powershell
curl.exe -i "$base/stores" --data-urlencode 'action=delete' --data-urlencode "storeId=$lojaB"
curl.exe -i "$base/regionTypes" --data-urlencode 'action=delete' --data-urlencode "regionTypeId=$regiaoSul"
```

Se a política for impedir exclusão com dependentes: ambos devem ser rejeitados
com mensagem clara, preservando loja, regiões e gerente. Hoje as violações
de FK seriam tratadas como erro genérico. Se o banco permitir cascata,
documente e confirme essa política antes deste teste; não a presuma como
comportamento desejado da tela.

Confirme ausência de registros órfãos:

```sql
SELECT r.id, r.store_id
FROM region_type r LEFT JOIN store s ON s.id = r.store_id
WHERE s.id IS NULL;

SELECT m.id, m.region_type_id
FROM regional_manager m LEFT JOIN region_type r ON r.id = m.region_type_id
WHERE r.id IS NULL;
```

Esperado: nenhuma linha nas duas consultas, se ambos os vínculos forem
obrigatórios conforme o contrato adotado.

### J. Excluir sem dependentes, na ordem correta

Use apenas os IDs de teste anotados. Não utilize o botão atual de exclusão de
gerente enquanto ele apontar para `/admins`.

```powershell
curl.exe -i "$base/regionalManagers" --data-urlencode 'action=delete' --data-urlencode "id=$gerente"
curl.exe -i "$base/regionTypes" --data-urlencode 'action=delete' --data-urlencode "regionTypeId=$regiaoNorte"
curl.exe -i "$base/regionTypes" --data-urlencode 'action=delete' --data-urlencode "regionTypeId=$regiaoSul"
curl.exe -i "$base/stores" --data-urlencode 'action=delete' --data-urlencode "storeId=$lojaA"
curl.exe -i "$base/stores" --data-urlencode 'action=delete' --data-urlencode "storeId=$lojaB"
```

Esperado: cada POST retorna 302 para a listagem correta e remove somente o ID
informado. Confirme a ausência nas consultas das seções B, D e F, considerando
os nomes/e-mails editados. Repita cada DELETE: o resultado sugerido é 404,
sem redirect enganoso. Hoje os DAOs ignoram a contagem de linhas e podem
redirecionar mesmo sem excluir nada.

Após corrigir o destino do formulário de gerente, repita a exclusão pelo
navegador com um novo registro de teste e confira no painel Network que o POST
vai para `/regionalManagers`. Nenhuma operação nessa tela deve escrever em
`super_admin`.

### K. Validar o fluxo completo do webapp

1. Abra `home-admin.jsp` e use os links Gerentes, Loja e Região. Confira que
   chegam aos Controllers, respeitando o contexto real da aplicação.
2. Em cada listagem, cadastre pelo formulário e confira o novo registro no
   banco e na tela. Hoje não há formulários de Store/RegionType nesta branch.
3. Abra Editar e confira o registro e vínculo selecionados. Altere, salve,
   confira o banco e recarregue: não deve haver novo cadastro por atualização
   de página.
4. Abra Editar novamente, mude campos e cancele. Deve voltar à listagem sem
   escrever no banco e sem erro JavaScript.
5. Envie um formulário inválido. Deve haver mensagem compreensível e
   preservação dos campos não sensíveis; a senha não deve voltar no HTML.
6. Confira o console: a implementação atual de gerentes tem erro de modal
   inexistente. Depois do ajuste, abrir, salvar e cancelar não devem gerar erro.
7. Confira o HTML: nomes/e-mails escapados e senha atual ausente. Teste nomes
   com acentos e com o marcador HTML literal descrito na seção H.
8. Exclua apenas registros de teste, respeitando dependências. Confirme o
   endpoint no Network e a ausência no banco. Teste também listas vazias.

## Ordem de resolução sugerida

1. Corrigir o destino da exclusão de gerente.
2. Resolver incompatibilidades Controller/Model e os bloqueios de compilação
   do WAR, sem alterar dependências.
3. Confirmar o esquema real e a política de vínculos/exclusão no PostgreSQL.
4. Alinhar os JSPs, propriedades, parâmetros, ações e links de navegação.
5. Implementar validação, tratamento de inexistência/conflitos, escape de saída
   e tratamento da senha no CRUD.
6. Executar o roteiro, registrando status HTTP e estado do banco a cada passo.
