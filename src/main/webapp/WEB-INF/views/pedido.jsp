<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="pt_BR"/>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/quistock.css">
    <link rel="stylesheet" href="${ctx}/css/loading.css?v=2">
    <title>QuiStock - Pedido</title>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="pedido"/></jsp:include>
    </aside>

    <main>
        <h1>Pedidos</h1>
        <c:if test="${not empty erro}"><p class="alerta" id="erro" role="alert"><c:out value="${erro}"/></p></c:if>
        <c:choose>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar pedido</button>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">
                    <div id="botoes"><button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button></div>
                    <div id="tabela" class="tabela-pedido" style="--colunas: 3; overflow-x: auto">
                        <div id="cabecalho">
                            <h3>Data</h3>
                            <h3>Preço total</h3>
                            <h3>Produto</h3>
                            <h3>Ações</h3>
                        </div>
                        <c:forEach var="item" items="${itens}">
                            <div class="linha">
                                <span><c:out value="${item.orderDate}"/></span>
                                <span><fmt:formatNumber value="${item.totalOrderPrice}" type="currency" currencySymbol="R$"/></span>
                                <span><c:out value="${item.productName}"/></span>
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
        <form method="post" action="${ctx}/orders">
            <h2>Adicionar pedido</h2>
            <input type="hidden" name="action" value="add">
            <div class="campo">
                <label for="add-product">Produto</label>
                <select id="add-product" name="product-id" required>
                    <option value="">Selecione um produto</option>
                    <c:forEach var="produto" items="${produtos}">
                        <option value="${produto.id}"><c:out value="${produto.name}"/></option>
                    </c:forEach>
                </select>
                <c:if test="${empty produtos}"><p role="status">Nenhum produto cadastrado.</p></c:if>
            </div>
            <div class="campo">
                <label for="popup-adicionar-total-order-price">Preço total</label>
                <input id="popup-adicionar-total-order-price" type="number" name="total-order-price" min="0" step="0.01" required>
            </div>
            <div class="acoes-popup">
                <button type="button" class="botao" popovertarget="popup-adicionar" popovertargetaction="hide">Cancelar</button>
                <button type="submit" class="botao primario">Salvar</button>
            </div>
        </form>
    </div>
    <c:forEach var="item" items="${itens}">
        <div id="editar-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/orders">
                <h2>Editar pedido</h2>
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="${item.id}">
                <div class="campo">
                    <label for="editar-${item.id}-product">Produto</label>
                    <select id="editar-${item.id}-product" name="product-id" required>
                        <option value="">Selecione um produto</option>
                        <c:forEach var="produto" items="${produtos}">
                            <option value="${produto.id}" <c:if test="${produto.id eq item.productId}">selected</c:if>><c:out value="${produto.name}"/></option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty produtos}"><p role="status">Nenhum produto cadastrado.</p></c:if>
                </div>
                <div class="campo">
                    <label for="editar-${item.id}-total-order-price">Preço total</label>
                    <input id="editar-${item.id}-total-order-price" type="number" name="total-order-price" min="0" step="0.01" required value="<c:out value='${item.totalOrderPrice}'/>">
                </div>
                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="editar-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao primario">Salvar</button>
                </div>
            </form>
        </div>
        <div id="remover-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/orders">
                <h2>Remover pedido</h2>
                <p>Deseja realmente remover pedido de <strong><c:out value="${item.productName}"/></strong>?</p>
                <input type="hidden" name="action" value="delete">
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
