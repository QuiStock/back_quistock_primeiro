<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <%@ include file="/WEB-INF/fragmentos/head.jspf" %>
    <title>QuiStock - Início</title>
</head>
<body>

    <%@ include file="/WEB-INF/fragmentos/menu.jspf" %>

    <main class="centralizado">
        <h2 class="boas-vindas">Bem vindo(a), <c:out value="${nome}"/></h2>

        <div id="acoes-inicio">
            <a class="botao verde" href="${ctx}/admins">Ver administradores</a>
            <%-- TODO: trocar "#" pelo endereço da landing page quando ela existir --%>
            <a class="botao contorno" href="#">Voltar para a landing</a>
        </div>
    </main>

</body>
</html>
