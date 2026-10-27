<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    TELA: Início   |   Servlet: /inicio   |   Arquivo: WEB-INF/views/inicio.jsp

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
    <link rel="stylesheet" href="${ctx}/css/loading.css">
    <title>QuiStock - Início</title>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

    <%-- Menu lateral (copiado em cada página: aqui não existe include) --%>
    <aside>
        <img src="${ctx}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <nav class="links">
            <a href="${ctx}/inicio" class="ativo"><img src="${ctx}/assets/icons/inicio.svg" alt="">Início</a>
            <c:if test="${sessionScope.loggedUser.type eq 'ADMIN'}">
                <a href="${ctx}/admins"><img src="${ctx}/assets/icons/admin.svg" alt="">Admins</a>
                <a href="${ctx}/gerentes"><img src="${ctx}/assets/icons/gerente.svg" alt="">Gerente</a>
                <a href="${ctx}/faq"><img src="${ctx}/assets/icons/faq.svg" alt="">FAQ</a>
                <a href="${ctx}/regioes"><img src="${ctx}/assets/icons/regiao.svg" alt="">Região</a>
                <a href="${ctx}/stores">Lojas</a>
                <a href="${ctx}/anotacoes"><img src="${ctx}/assets/icons/anotacoes.svg" alt="">Anotações</a>
            </c:if>
        </nav>
    </aside>

    <main class="centralizado">
        <h2 class="boas-vindas">Bem vindo(a), <c:out value="${nome}"/></h2>

        <div id="acoes-inicio">
            <c:if test="${sessionScope.loggedUser.type eq 'ADMIN'}">
                <a class="botao verde" href="${ctx}/admins">Ver administradores</a>
            </c:if>
            <%-- "${ctx}/" é a raiz do sistema: lá abre o index da landing page --%>
            <a class="botao contorno" href="${ctx}/">Voltar para a landing</a>
        </div>
    </main>

    <script src="${ctx}/assets/js/loading.js"></script>
</body>
</html>
