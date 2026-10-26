# MEU_ESTILO.md — Padrão de programação do QuiStock

> **Finalidade:** orientar o ChatGPT/Codex a criar, editar e corrigir códigos seguindo o jeito que eu programo. Estas regras foram inferidas a partir dos meus exemplos reais de **Java Web (DAO, Servlet e Model)** e **LPR (Java com classes, listas, arquivos e console)**. Os arquivos mais recentes do projeto têm prioridade quando houver diferenças.

## 1. Minha forma de programar

- Prefiro código **simples, direto, sequencial e fácil de acompanhar**, sem abstrações ou padrões complexos que não sejam necessários.
- Se já existe uma estrutura funcionando, prefiro **continuar o padrão existente** em vez de reescrever tudo.
- Divido a implementação em **métodos com responsabilidades claras**, usando nomes que descrevem a ação.
- Gosto de declarar as variáveis perto do início do trecho em que serão usadas e depois executar as etapas em ordem.
- Uso bastante `if`, `else`, `switch`, `while`, `for` e `for-each`, sem tentar transformar tudo em recursos mais avançados do Java.
- Meu estilo é de estudante de back-end: o código deve ser **entendível e coerente com o que já aprendi**, não parecer uma arquitetura muito elaborada.
- Costumo deixar **linhas em branco para separar etapas** dentro de um método.

## 2. Como escrevo comentários (MUITO IMPORTANTE)

Meus comentários são **curtos, informais e explicam a intenção** do bloco que vem em seguida. Geralmente são em português, começando com `//`, sem formalidades e, muitas vezes, sem acentos ou pontuação final.

### Exemplos reais do meu estilo

```java
//Metodo pra imprimir todos os gerentes
//try pra listar tudo de todos os gerentes
//laço pra repetir sempre q tiver gerente na fila
//metodo pra criar um gerente novo
//metodo pra encontrar gerente que deseja ser alterado
//metodo pra atualizar o gerente que encontrou no metodo passado
//metodo pra excluir gerente do banco de acordo com o id
```

```java
//encaminha pra pagina gerentes
//doGet pra ver qual das duas ações vai fazer
//averigua qual action que é e redireciona para o servlet correto
//doPost pra alterar tabela, seja criando, editando ou deletando
//pegando info do forms
//adicionadno as info num objeto do gerente
//criando o gerente novo no db
//mostra o formulario pro usuario editar o gerente
//definindo os valores novos do gerente
//editando o gerente
```

```java
//entrada
//processo
```

Também uso comentários **no fim da linha** para esclarecer alguma consequência:

```java
if (action == null) action = "regionalManagersList";
request.setAttribute("regionalManager", regionalManager); // agora manda o objeto inteiro
response.sendRedirect("regionalManagers"); //redireciona pra regionalManagers.jsp dnv
return null; //se nao encontrar nenhum gerente com esse id retorna null
```

### Regras para imitar meus comentários

1. Prefira `//` a documentação longa em Javadoc.
2. Escreva **como alguém explicando o próprio código para um colega**, por exemplo: `//pegando os dados do formulario` ou `//verifica qual action foi enviada`.
3. Coloque o comentário **antes do método, condição ou bloco importante**; use comentário na mesma linha só quando ajudar.
4. Não faça comentários enormes, acadêmicos ou técnicos demais.
5. Não comente cada comando óbvio; comente as **etapas** e o propósito dos métodos.
6. Pode usar naturalmente palavras como `pra`, `info`, `forms`, `db` e `q`, mas **não invente erros de português de propósito**. O importante é manter a informalidade.
7. Preserve meus comentários existentes, salvo quando estiverem incorretos por causa de uma mudança.

## 3. Formatação, sintaxe e nomes

- Identação predominante: **4 espaços** por nível.
- Chaves normalmente na **mesma linha** de classes, métodos, `if`, `while`, `for`, `switch` e `try`.
- Mantenho espaços e linhas em branco para separar partes lógicas.
- Prefiro código explícito e compreensível a expressões compactas difíceis de ler.
- Nomes de classes em `PascalCase`; métodos e variáveis em `camelCase`.
- **Java Web:** nomes de classes, métodos e atributos geralmente em **inglês**, acompanhando as entidades e o banco (`RegionalManagerDAO`, `createRegionalManager`, `regionalManagersList`, `regionTypeId`).
- **LPR / exercícios:** nomes em **português** (`buscarJogo`, `valorTotal`, `cliente`, `jogos`, `itens`, `escolha`).
- Aceito nomes simples como `conn`, `sttmt`, `result`, `sql`, `input`, `infos`, `fw` e `pw` quando cabem no contexto.
- Uso `List<T>` com `ArrayList<>` frequentemente.
- Não imponha regras de formatação novas se o arquivo existente tiver pequenas variações minhas (por exemplo, `}catch`, `} catch`, `throws SQLException{` ou `while(x < 1){`).

## 4. Meu padrão de Java Web — QuiStock

### Arquitetura

Sigo o **MVC ensinado na escola** e separo os arquivos em `Model`, `DAO` e `Controller`, com páginas `JSP` para a interface.

- **Model:** representa as informações da tabela ou entidade.
- **DAO:** executa os comandos SQL e faz a comunicação com o banco.
- **Controller (Servlet):** recebe os dados dos formulários, verifica a `action`, chama o DAO e encaminha ou redireciona as páginas.
- **JSP:** mostra formulários e dados. Analise exemplos reais antes de inferir meu estilo de HTML/JSP; os códigos enviados aqui não incluem JSP.
- Não criar `Service`, repository adicional, DTO, mapper, framework ou camadas novas sem necessidade ou pedido explícito.

### DAO — padrões observados

1. Classe com nome da entidade seguido de `DAO`.
2. Instancio `ConnectionFactory` como atributo da classe.
3. Cada operação de CRUD fica em um método diferente.
4. Declaro a consulta em `String sql = "..."` dentro do método.
5. Uso `try-with-resources` com `Connection` e `PreparedStatement`, e `ResultSet` quando preciso ler dados.
6. Uso parâmetros `?` e `sttmt.setString(...)` / `sttmt.setInt(...)` nas consultas.
7. Uso `executeQuery()` para `SELECT` e `executeUpdate()` para `INSERT`, `UPDATE` e `DELETE`.
8. No `SELECT`, crio o objeto, preencho com setters e adiciono a uma `List` ou retorno o objeto.
9. Em busca por ID, retorno `null` quando não encontro registro.
10. Os métodos do DAO podem declarar `throws SQLException`, deixando o Servlet tratar o erro.

Trecho representativo do meu código:

```java
//Metodo pra imprimir todos os gerentes
public List<RegionalManager> readRegionalManagers() throws SQLException {

    List<RegionalManager> regionalManagers = new ArrayList<>();

    String sql = "SELECT * FROM regional_manager";

    //try pra listar tudo de todos os gerentes
    try (Connection conn = connectionFactory.getConnection();
         PreparedStatement sttmt = conn.prepareStatement(sql);
         ResultSet result = sttmt.executeQuery()) {

        //laço pra repetir sempre q tiver gerente na fila
        while (result.next()) {
            RegionalManager regionalManager = new RegionalManager();
            regionalManager.setId(result.getInt("id"));
            regionalManager.setName(result.getString("name"));
            regionalManager.setEmail(result.getString("email"));
            regionalManagers.add(regionalManager);
        }
    }
    return regionalManagers;
}
```

> Trecho **ilustrativo reduzido** do método original, mantendo meu formato. Consulte os arquivos originais para campos adicionais.

### Controller / Servlet — padrões observados

1. Uso `@WebServlet("/caminho")` em cima da classe.
2. O Servlet `extends HttpServlet` e possui um DAO como atributo.
3. Separo `doGet` (listagem e exibição dos formulários) de `doPost` (criar, editar e excluir).
4. Leio `request.getParameter("action")` e uso `switch` com `case`, `break` e `default`.
5. Crio métodos `private` para cada ação (`read...`, `create...`, `showUpdateForm`, `update...`, `delete...`).
6. Pego os dados do formulário com `request.getParameter`, faço conversões simples como `Integer.parseInt`, monto o Model com setters e chamo o DAO.
7. Para mostrar páginas, uso `request.setAttribute`, `RequestDispatcher` e `dispatcher.forward`.
8. Depois de salvar, atualizar ou deletar, geralmente uso `response.sendRedirect` para voltar à listagem.
9. Trato `SQLException` com `try/catch` e `throw new ServletException("Mensagem", e)`.
10. Defino `request.setCharacterEncoding("UTF-8")` no `doPost`.

Trecho representativo do meu estilo:

```java
//doGet pra ver qual das duas ações vai fazer
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    String action = request.getParameter("action");
    if (action == null) action = "regionalManagersList";

    //averigua qual action que é e redireciona para o servlet correto
    switch (action){
        case "edit":
            showUpdateForm(request, response); //aparecer a interface pra editar
            break;
        default:
            readRegionalManagers(request, response);
    }
}
```

### Model — padrões observados

- Campos `private` com tipos simples como `int` e `String`.
- Em Java Web, tenho usado **Lombok** (`@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor`) para evitar getters, setters e construtores manuais.
- O Model tende a ser simples, sem lógica complexa.
- Em exercícios de LPR sem Lombok, escrevo construtores, getters, setters e às vezes `toString()` manualmente.

## 5. Meu padrão de LPR — Java básico

- Organizo o programa em **entrada, processo e saída**, frequentemente marcando com comentários como `//entrada` e `//processo`.
- Uso `Scanner` para entrada do usuário e leitura de arquivo, e `FileWriter` + `PrintWriter` para gravar arquivos.
- Converto texto com `Integer.parseInt`, `Double.parseDouble` e `String.valueOf`.
- Uso `List`, `ArrayList`, `while`, `for-each`, `if/else`, `continue` e `break` para controlar o programa.
- Tenho o hábito de fazer uma função auxiliar simples quando algo precisa ser localizado ou reutilizado, como `buscarJogo`.
- Crio objetos com `new`, uso getters para acessar informações e somo valores com operadores diretos, como `valorTotal += ...`.
- Em códigos introdutórios, posso usar um `try/catch` geral no `main` com `System.out.println(e.getMessage())`, mas isso **não deve substituir** um tratamento adequado quando a situação exigir.
- Os Models dos exercícios costumam ser POJOs com atributos privados, construtor, getters, setters e `toString()`.

Trecho representativo:

```java
public static String buscarJogo(List<Jogo> jogos, String escolha){

    for (Jogo jogox : jogos){
        if (escolha.equals(String.valueOf(jogox.getCodigo()))){
            return jogox.getTitulo();
        }
        if(escolha.equals(jogox.getTitulo())){
            return jogox.getTitulo();
        }
    }
    return null;
}
```

## 6. O que NÃO copiar automaticamente

Meu estilo de escrita é uma referência, **não uma obrigação de repetir problemas**. Preserve minha forma de programar, mas corrija ou sinalize problemas reais, principalmente:

- **Senhas:** exemplos antigos incluem campo `password` lido e gravado diretamente. Não adote armazenamento ou exposição de senha em texto puro como padrão; use tratamento seguro apropriado ao projeto.
- **Validação:** `Integer.parseInt(request.getParameter(...))` pode falhar com entradas inválidas; adicione validação quando necessária.
- **Nulos:** acesso a métodos de objetos/strings sem checar `null` pode gerar erro.
- **Exceções:** evite engolir erros e use mensagens úteis sem expor dados sensíveis.
- **SQL:** continue usando consultas parametrizadas, nunca concatene entrada do usuário diretamente nas queries.
- **Comentários:** não reproduza erros de digitação deliberadamente; preserve tom e simplicidade.
- **Lógica:** não copie `if/else` redundante, loops desnecessários ou bugs só por se parecerem com meu código.
- **Segurança e consistência:** qualquer ajuste necessário deve ser o menor possível e explicado de forma curta.

## 7. Checklist para gerar ou alterar meus códigos

Antes de entregar, confira:

- [ ] Segui a estrutura que já existe no QuiStock?
- [ ] Usei nomes consistentes com o arquivo (inglês no Java Web; português onde o projeto usa português)?
- [ ] Mantive o código simples e sem camadas extras?
- [ ] Coloquei comentários curtos em português informal nos blocos importantes, como faço?
- [ ] Mantive a organização visual, as linhas em branco e a indentação do arquivo?
- [ ] Usei métodos e fluxos semelhantes aos que já conheço?
- [ ] Evitei mudar trechos não relacionados?
- [ ] Verifiquei possíveis erros e riscos sem imitar práticas inseguras?
- [ ] Se não tive acesso ao arquivo real, deixei claro que a alteração é sugerida, não aplicada?

## 8. Como o assistente deve responder

- Para dúvidas simples, **resposta simples**.
- Para erro de código, diga **qual é o problema e como corrigir**.
- Para alteração pedida, **faça a mudança**, se tiver acesso; senão, entregue o trecho ou arquivo pronto para substituir.
- Após alterações, informe de modo curto: **o que mudou, arquivos afetados e pendências relevantes**.
- Só explique detalhadamente se eu pedir.
- Se novos códigos meus contradisserem alguma inferência deste arquivo, **atualize este guia seguindo os exemplos mais recentes**, sem modificar meu código original apenas para padronizá-lo.

---

**Origem das referências:** `RegionalManagerDAO`, `RegionalManagerServlet`, `RegionalManager` (Java Web); `Main`, `Jogo`, `ItemLocacao`, `Locacao` (LPR). Guia baseado nos exemplos fornecidos, não em uma análise completa do repositório.
