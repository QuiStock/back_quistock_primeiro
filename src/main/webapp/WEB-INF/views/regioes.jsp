<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/landing-assets/asa-roxa.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/quistock.css">
    <link rel="stylesheet" href="${ctx}/css/loading.css?v=2">
    <title>QuiStock - Região</title>
</head>
<body>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="regioes"/></jsp:include>
    </aside>

    <main>
        <h1>Região</h1>

        <c:if test="${not empty erro}">
            <p class="alerta" id="erro" role="alert">
                <c:out value="${erro}"/>
            </p>
        </c:if>

        <c:choose>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar região</button>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">

                    <div id="botoes">
                        <button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button>
                    </div>

                    <div id="tabela" class="tabela-uma-coluna">
                        <div id="cabecalho">
                            <h3>Nome</h3>
                            <h3>Ações</h3>
                        </div>

                        <c:forEach var="item" items="${itens}">
                            <div class="linha">
                                <span><c:out value="${item.nome}"/></span>

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


    <div id="popup-adicionar" class="popup" popover>
        <form method="post" action="${ctx}/regioes" autocomplete="off" novalidate>
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


    <c:forEach var="item" items="${itens}">
        <div id="editar-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/regioes" autocomplete="off" novalidate>
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

    <script src="${ctx}/assets/js/loading.js"></script>
</body>
</html>
