<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/landing-assets/asa-roxa.svg">
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quistock.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css?v=2">
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

    <aside>
        <img src="${pageContext.request.contextPath}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="inicio"/></jsp:include>
    </aside>
    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
