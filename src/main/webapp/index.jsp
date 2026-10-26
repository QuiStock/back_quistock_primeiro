<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>


    <form action="${pageContext.request.contextPath}/login" method="post">

        <input type="email" name="email" placeholder="email">
        <input type="password" name = "password" placeholder="senha">

        <input type="submit">
    </form>

    <c:if test="${not empty error}"><p style="color:red">${error}</p></c:if>
</body>
</html>