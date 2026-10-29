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
        <div id="lista">
            <div id="tabela" class="tabela-consulta tabela-promocao">
                <div id="cabecalho">
                    <h3>Data</h3>
                    <h3>Desconto</h3>
                    <h3>Validade</h3>
                    <h3>Produto</h3>
                </div>
                <c:forEach var="item" items="${itens}">
                    <div class="linha">
                        <span><c:out value="${item.data}"/></span>
                        <span><c:out value="${item.desconto}"/></span>
                        <span><c:out value="${item.validade}"/></span>
                        <span><c:out value="${item.produto}"/></span>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${empty itens}"><p class="mensagem-vazia">Nenhuma promoção cadastrada.</p></c:if>
        </div>
    </main>
    <script src="${ctx}/assets/js/loading.js"></script>
</body>
</html>
