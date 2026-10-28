<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    TELA: Anotações   |   Servlet: /anotacoes   |   Arquivo: WEB-INF/views/anotacoes.jsp
--%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/quistock.css">
    <title>QuiStock - Anotações</title>
</head>
<body>

    <%-- Menu lateral (copiado em cada página: aqui não existe include) --%>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="anotacoes"/></jsp:include>
    </aside>

    <main>
        <h1>Anotações</h1>

        <%-- Mensagem de erro: o Servlet guarda o código na sessão e ele aparece só uma vez.
             O CSS (#erro) faz a mensagem sumir sozinha depois de 4 segundos. --%>
        <c:if test="${not empty erro}">
            <p class="alerta" id="erro" role="alert">
                <c:choose>
                    <c:when test="${erro eq 'vazio'}">Preencha todos os campos antes de salvar.</c:when>
                    <c:when test="${erro eq 'tamanho'}">Algum campo ultrapassou o tamanho permitido.</c:when>
                    <c:when test="${erro eq 'email'}">Informe um email válido.</c:when>
                    <c:when test="${erro eq 'duplicado'}">Esse email já está cadastrado.</c:when>
                    <c:otherwise>Não foi possível concluir a operação. Tente novamente.</c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <c:choose>

            <%-- Sem registros: tela "Área vazia" --%>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar anotação</button>
                </div>
            </c:when>

            <%-- Com registros: botão Adicionar (fora da lista) + cabeçalho + linhas --%>
            <c:otherwise>
                <div id="lista">

                    <div id="botoes">
                        <button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button>
                    </div>

                    <div id="tabela" style="--colunas: 3">
                        <div id="cabecalho">
                            <h3>Título</h3>
                            <h3>Descrição</h3>
                            <h3>Tópico</h3>
                            <h3>Ações</h3>
                        </div>

                        <c:forEach var="item" items="${itens}">
                            <div class="linha">
                                <span><c:out value="${item.titulo}"/></span>
                                <span><c:out value="${item.descricao}"/></span>
                                <span><c:out value="${item.topico}"/></span>

                                <%-- Botões desta linha: abrem os popups "editar-ID" e "remover-ID" --%>
                                <div class="acoes-linha">
                                    <button type="button" class="botao pequeno" popovertarget="editar-${item.id}"><img src="${ctx}/assets/icons/editar.svg" alt="">Editar</button>
                                    <button type="button" class="botao pequeno perigo" popovertarget="remover-${item.id}"><img src="${ctx}/assets/icons/remover.svg" alt="">Remover</button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>

        </c:choose>
    </main>


    <%-- ================= POPUP: ADICIONAR (formulário vazio) ================= --%>
    <div id="popup-adicionar" class="popup" popover>
        <form method="post" action="${ctx}/anotacoes" autocomplete="off">
            <h2>Adicionar anotação</h2>
            <input type="hidden" name="acao" value="salvar">
            <input type="hidden" name="id" value="">

            <div class="campo">
                <label for="add-titulo">Título</label>
                <input id="add-titulo" type="text" name="titulo" maxlength="150" required autofocus>
            </div>
            <div class="campo">
                <label for="add-descricao">Descrição</label>
                <textarea id="add-descricao" name="descricao" maxlength="500" rows="4" required></textarea>
            </div>
            <div class="campo">
                <label for="add-topico">Tópico</label>
                <input id="add-topico" type="text" name="topico" maxlength="100" required>
            </div>

            <div class="acoes-popup">
                <button type="button" class="botao" popovertarget="popup-adicionar" popovertargetaction="hide">Cancelar</button>
                <button type="submit" class="botao primario">Salvar</button>
            </div>
        </form>
    </div>


    <%-- ============ POPUPS DE CADA LINHA (um par Editar + Remover por registro) ============ --%>
    <c:forEach var="item" items="${itens}">

        <%-- EDITAR: o formulário já vem preenchido com os dados deste registro --%>
        <div id="editar-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/anotacoes" autocomplete="off">
                <h2>Editar anotação</h2>
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${item.id}">

                <div class="campo">
                    <label for="editar-${item.id}-titulo">Título</label>
                    <input id="editar-${item.id}-titulo" type="text" name="titulo" maxlength="150" required autofocus value="<c:out value='${item.titulo}'/>">
                </div>
                <div class="campo">
                    <label for="editar-${item.id}-descricao">Descrição</label>
                    <textarea id="editar-${item.id}-descricao" name="descricao" maxlength="500" rows="4" required><c:out value="${item.descricao}"/></textarea>
                </div>
                <div class="campo">
                    <label for="editar-${item.id}-topico">Tópico</label>
                    <input id="editar-${item.id}-topico" type="text" name="topico" maxlength="100" required value="<c:out value='${item.topico}'/>">
                </div>

                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="editar-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao primario">Salvar</button>
                </div>
            </form>
        </div>

        <%-- REMOVER: pede confirmação mostrando o nome do registro --%>
        <div id="remover-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/anotacoes">
                <h2>Remover</h2>
                <p>Deseja realmente remover <strong><c:out value="${item.titulo}"/></strong>? Esta ação não pode ser desfeita.</p>
                <input type="hidden" name="acao" value="remover">
                <input type="hidden" name="id" value="${item.id}">
                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="remover-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao perigo-solido">Remover</button>
                </div>
            </form>
        </div>

    </c:forEach>

</body>
</html>
