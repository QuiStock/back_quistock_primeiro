<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <title>QuiStock - Promoção</title>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="promocao"/></jsp:include>
    </aside>

    <main>
        <h1>Promoções</h1>
        <c:if test="${not empty erro}"><p class="alerta" id="erro" role="alert"><c:out value="${erro}"/></p></c:if>
        <c:choose>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" popovertarget="popup-adicionar">Adicionar promoção</button>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">
                    <div id="botoes"><button type="button" class="botao" popovertarget="popup-adicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button></div>
                    <div id="tabela" class="tabela-promocao" style="--colunas: 4; overflow-x: auto">
                        <div id="cabecalho">
                            <h3>Data da promoção</h3>
                            <h3>Desconto (%)</h3>
                            <h3>Válida até</h3>
                            <h3>Produto</h3>
                            <h3>Ações</h3>
                        </div>
                        <c:forEach var="item" items="${itens}">
                            <div class="linha">
                                <span><c:out value="${item.sugestionDate}"/></span>
                                <span><c:out value="${item.discountPercentage}"/>%</span>
                                <span><c:out value="${item.promotionValidUntil}"/></span>
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
        <form method="post" action="${ctx}/sales" novalidate>
            <h2>Adicionar promoção</h2>
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
                <label for="popup-adicionar-discountPercentage">Desconto (%)</label>
                <input id="popup-adicionar-discountPercentage" type="number" name="discountPercentage" min="0.01" max="100" step="0.01" required>
            </div>
            <div class="campo">
                <label for="popup-adicionar-validade">Validade</label>
                <input id="popup-adicionar-validade" type="date" name="promotion-valid-until" required>
            </div>
            <div class="acoes-popup">
                <button type="button" class="botao" popovertarget="popup-adicionar" popovertargetaction="hide">Cancelar</button>
                <button type="submit" class="botao primario">Salvar</button>
            </div>
        </form>
    </div>
    <c:forEach var="item" items="${itens}">
        <div id="editar-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/sales" novalidate>
                <h2>Editar promoção</h2>
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
                    <label for="editar-${item.id}-discountPercentage">Desconto (%)</label>
                    <input id="editar-${item.id}-discountPercentage" type="number" name="discountPercentage" min="0.01" max="100" step="0.01" required value="<c:out value='${item.discountPercentage}'/>">
                </div>
                <div class="campo">
                    <label for="editar-${item.id}-validade">Validade</label>
                    <input id="editar-${item.id}-validade" type="date" name="promotion-valid-until" required value="${item.promotionValidUntil}">
                </div>
                <div class="acoes-popup">
                    <button type="button" class="botao" popovertarget="editar-${item.id}" popovertargetaction="hide">Cancelar</button>
                    <button type="submit" class="botao primario">Salvar</button>
                </div>
            </form>
        </div>
        <div id="remover-${item.id}" class="popup" popover>
            <form method="post" action="${ctx}/sales">
                <h2>Remover promoção</h2>
                <p>Deseja realmente remover promoção de <strong><c:out value="${item.productName}"/></strong>?</p>
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
