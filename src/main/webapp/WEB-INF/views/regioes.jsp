<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    TELA: Região   |   Servlet: /regioes   |   Arquivo: WEB-INF/views/regioes.jsp
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
    <title>QuiStock - Região</title>
</head>
<body>

    <%-- Menu lateral (copiado em cada página: aqui não existe include) --%>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <nav class="links">
            <a href="${ctx}/inicio"><img src="${ctx}/assets/icons/inicio.svg" alt="">Início</a>
            <a href="${ctx}/admins"><img src="${ctx}/assets/icons/admin.svg" alt="">Admins</a>
            <a href="${ctx}/gerentes"><img src="${ctx}/assets/icons/gerente.svg" alt="">Gerente</a>
            <a href="${ctx}/faq"><img src="${ctx}/assets/icons/faq.svg" alt="">FAQ</a>
            <a href="${ctx}/regioes" class="ativo"><img src="${ctx}/assets/icons/regiao.svg" alt="">Região</a>
            <a href="${ctx}/stores">Lojas</a>
            <a href="${ctx}/anotacoes"><img src="${ctx}/assets/icons/anotacoes.svg" alt="">Anotações</a>
        </nav>
    </aside>

    <main>
        <h1>Região</h1>

        <%-- O Servlet manda o erro pela URL. --%>
        <c:if test="${not empty erro}">
            <p class="alerta" id="erro" role="alert">
                <c:choose>
                    <c:when test="${erro eq 'vazio'}">Preencha todos os campos antes de salvar.</c:when>
                    <c:when test="${erro eq 'tamanho'}">Algum campo ultrapassou o tamanho permitido.</c:when>
                    <c:when test="${erro eq 'duplicado'}">Esse nome já está cadastrado.</c:when>
                    <c:when test="${erro eq 'vinculo'}">Esta região possui registros vinculados.</c:when>
                    <c:otherwise>Não foi possível concluir a operação. Tente novamente.</c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <c:choose>

            <%-- Sem registros: tela "Área vazia" --%>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar região</button>
                </div>
            </c:when>

            <%-- Com registros: botão Adicionar (fora da lista) + cabeçalho + linhas --%>
            <c:otherwise>
                <div id="lista">

                    <div id="botoes">
                        <button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button>
                    </div>

                    <%-- regiao tem so id e nome --%>
                    <div id="tabela" style="--colunas: 2">
                        <div id="cabecalho">
                            <h3>ID</h3>
                            <h3>Nome</h3>
                            <h3>Ações</h3>
                        </div>

                        <c:forEach var="item" items="${itens}">
                            <div class="linha">
                                <span><c:out value="${item.id}"/></span>
                                <span><c:out value="${item.name}"/></span>

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
        <form method="post" action="${ctx}/regioes" autocomplete="off">
            <h2>Adicionar região</h2>
            <input type="hidden" name="acao" value="salvar">
            <input type="hidden" name="id" value="">

            <div class="campo">
                <label for="add-nome">Nome</label>
                <input id="add-nome" type="text" name="nome" maxlength="100" required autofocus>
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
            <form method="post" action="${ctx}/regioes" autocomplete="off">
                <h2>Editar região</h2>
                <input type="hidden" name="acao" value="salvar">
                <input type="hidden" name="id" value="${item.id}">

                <div class="campo">
                    <label for="editar-${item.id}-nome">Nome</label>
                    <input id="editar-${item.id}-nome" type="text" name="nome" maxlength="100" required autofocus value="<c:out value='${item.nome}'/>">
                </div>
                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="editar-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao primario">Salvar</button>
                </div>
            </form>
        </div>

        <%-- REMOVER: pede confirmação mostrando o nome do registro --%>
        <div id="remover-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/regioes">
                <h2>Remover</h2>
                <p>Deseja realmente remover <strong><c:out value="${item.nome}"/></strong>? Esta ação não pode ser desfeita.</p>
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
