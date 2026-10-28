<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quistock.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
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
