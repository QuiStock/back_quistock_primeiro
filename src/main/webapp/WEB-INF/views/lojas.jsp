<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${ctx}/css/quistock.css">
    <link rel="stylesheet" href="${ctx}/css/loading.css">
    <title>QuiStock - Lojas</title>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="lojas"/></jsp:include>
    </aside>

    <main>
        <h1>Lojas</h1>

        <c:if test="${param.erro eq 'duplicado'}">
            <p class="alerta" id="erro" role="alert">Esse email já está cadastrado.</p>
        </c:if>
        <c:if test="${param.erro eq 'vinculo'}">
            <p class="alerta" id="erro" role="alert">Esta loja possui registros vinculados.</p>
        </c:if>
        <c:if test="${param.erro eq 'regiao'}">
            <p class="alerta" id="erro" role="alert">Selecione uma região válida.</p>
        </c:if>

        <c:choose>
            <c:when test="${empty storesList}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar loja</button>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">
                    <div id="botoes">
                        <button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button>
                    </div>

                    <%-- mostra os dados da loja sem mandar a senha pro navegador --%>
                    <div id="tabela" style="--colunas: 4">
                        <div id="cabecalho">
                            <h3>ID</h3>
                            <h3>Email</h3>
                            <h3>Senha</h3>
                            <h3>Região ID</h3>
                            <h3>Ações</h3>
                        </div>

                        <c:forEach var="item" items="${storesList}">
                            <div class="linha">
                                <span><c:out value="${item.id}"/></span>
                                <span><c:out value="${item.email}"/></span>
                                <span>••••••</span>
                                <span><c:out value="${item.regionTypeId}"/></span>
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
        <form method="post" action="${ctx}/stores" autocomplete="off">
            <h2>Adicionar loja</h2>
            <input type="hidden" name="action" value="register">

            <div class="campo">
                <label for="add-email">Email</label>
                <input id="add-email" type="email" name="storeEmail" maxlength="320" required autofocus>
            </div>
            <div class="campo">
                <label for="add-senha">Senha</label>
                <input id="add-senha" type="password" name="storePassword" required>
            </div>

            <%-- manda o id da regiao escolhida pro servlet --%>
            <div class="campo">
                <label for="add-regiao">Região</label>
                <select id="add-regiao" name="regionTypeId" required>
                    <option value="">Selecione</option>
                    <c:forEach var="regiao" items="${regioes}">
                        <option value="${regiao.id}"><c:out value="${regiao.id}"/> - <c:out value="${regiao.name}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="acoes-popup">
                <button type="button" class="botao" popovertarget="popup-adicionar" popovertargetaction="hide">Cancelar</button>
                <button type="submit" class="botao primario">Salvar</button>
            </div>
        </form>
    </div>

    <c:forEach var="item" items="${storesList}">
        <div id="editar-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/stores" autocomplete="off">
                <h2>Editar loja</h2>
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="storeId" value="${item.id}">

                <div class="campo">
                    <label for="editar-${item.id}-email">Email</label>
                    <input id="editar-${item.id}-email" type="email" name="storeEmail" maxlength="320" required value="<c:out value='${item.email}'/>">
                </div>
                <div class="campo">
                    <label for="editar-${item.id}-senha">Nova senha (opcional)</label>
                    <input id="editar-${item.id}-senha" type="password" name="storePassword">
                </div>

                <div class="campo">
                    <label for="editar-${item.id}-regiao">Região</label>
                    <select id="editar-${item.id}-regiao" name="regionTypeId" required>
                        <c:forEach var="regiao" items="${regioes}">
                            <option value="${regiao.id}" <c:if test="${regiao.id eq item.regionTypeId}">selected</c:if>><c:out value="${regiao.id}"/> - <c:out value="${regiao.name}"/></option>
                        </c:forEach>
                    </select>
                </div>

                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="editar-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao primario">Salvar</button>
                </div>
            </form>
        </div>

        <div id="remover-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/stores">
                <h2>Remover loja</h2>
                <p>Deseja realmente remover <strong><c:out value="${item.email}"/></strong>?</p>
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="storeId" value="${item.id}">
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
