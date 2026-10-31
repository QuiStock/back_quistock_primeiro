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
    <title>QuiStock - Início</title>
</head>
<body>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="inicio"/></jsp:include>
    </aside>

    <main class="centralizado">
        <h2 class="boas-vindas">Boas-vindas, <c:out value="${nome}"/></h2>

        <div id="acoes-inicio">
            <c:choose>
                <c:when test="${sessionScope.loggedUser.type eq 'MANAGER'}">
                    <a class="botao verde" href="${ctx}/regioes">Ver regiões</a>
                </c:when>
                <c:otherwise>
                    <a class="botao verde" href="${ctx}/admins">Ver administradores</a>
                </c:otherwise>
            </c:choose>
            <a class="botao contorno" href="${ctx}/">Voltar para a landing</a>
        </div>
    </main>

    <script src="${ctx}/assets/js/loading.js"></script>
</body>
</html>
