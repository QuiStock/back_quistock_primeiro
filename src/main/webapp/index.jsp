<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Service.LoginService" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
</head>
<body>

<div id="loading" class="loading hidden" aria-hidden="true">
    <div class="chaotic-orbit"></div>
</div>


    <form action="${pageContext.request.contextPath}/login" method="post">

        <input type="email" name="email" placeholder="email">
        <input type="password" name = "password" placeholder="senha">

        <input type="submit">
    </form>

    <c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>

    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
