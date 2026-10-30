<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/css/quistock.css?v=produtos-3">
    <link rel="stylesheet" href="${ctx}/css/loading.css?v=2">
    <title>QuiStock - Produtos</title>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true"><div class="chaotic-orbit"></div></div>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="produtos"/></jsp:include>
    </aside>
    <main>
        <h1>Produtos</h1>
        <c:if test="${not empty erro}"><p class="alerta" id="erro" role="alert"><c:out value="${erro}"/></p></c:if>
        <c:choose>
            <c:when test="${empty products}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <a class="botao verde" data-modal href="${ctx}/products?action=new">Adicionar produto</a>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">
                    <div id="botoes">
                        <a class="botao" data-modal href="${ctx}/products?action=new"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</a>
                    </div>
                    <div id="tabela" class="tabela-produtos">
                        <div id="cabecalho">
                            <h3>Lote</h3><h3>Nome</h3><h3>Preço</h3>
                            <h3>Validade</h3><h3>Quantidade</h3><h3>Ações</h3>
                        </div>
                        <c:forEach var="product" items="${products}">
                            <div class="linha">
                                <span><c:out value="${product.batch}"/></span>
                                <span><c:out value="${product.name}"/></span>
                                <span><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="R$"/></span>
                                <span><c:if test="${not empty product.expirationDate}">${product.expirationDate.dayOfMonth}/${product.expirationDate.monthValue}/${product.expirationDate.year}</c:if></span>
                                <span>${product.quantity}</span>
                                <div class="acoes-linha">
                                    <a class="botao pequeno" data-modal href="${ctx}/products?action=edit&id=${product.id}"><img src="${ctx}/assets/icons/editar.svg" alt="">Editar</a>
                                    <button class="botao pequeno perigo" type="button" popovertarget="remover-${product.id}"><img src="${ctx}/assets/icons/remover.svg" alt="">Remover</button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <dialog id="modal-product" class="popup" aria-labelledby="titulo-produto"></dialog>
    </main>
    <c:forEach var="product" items="${products}">
        <div id="remover-${product.id}" class="popup" popover>
            <form action="${ctx}/products" method="post">
                <h2>Remover produto</h2>
                <p>Deseja remover <strong><c:out value="${product.name}"/></strong>?</p>
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${product.id}">
                <div class="acoes-popup">
                    <button class="botao" type="button" popovertarget="remover-${product.id}" popovertargetaction="hide">Cancelar</button>
                    <button class="botao perigo-solido" type="submit">Remover</button>
                </div>
            </form>
        </div>
    </c:forEach>
    <script src="${ctx}/assets/js/loading.js"></script>
    <script>
        const modal = document.getElementById('modal-product');
        document.querySelectorAll('a[data-modal]').forEach(function (link) {
            link.addEventListener('click', async function (event) {
                event.preventDefault();
                window.mostrarLoading();
                try {
                    const response = await fetch(link.href);
                    if (!response.ok || response.redirected) throw new Error('Erro ao carregar formulário');
                    modal.innerHTML = await response.text();
                    modal.showModal();
                } catch (error) {
                    alert('Não foi possível carregar o formulário. Atualize a página e tente novamente.');
                } finally {
                    window.esconderLoading();
                }
            });
        });
        modal.addEventListener('click', function (event) {
            if (event.target === modal) modal.close();
        });
    </script>
</body>
</html>
