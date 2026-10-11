<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Service.LoginService" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>


    <form action="${pageContext.request.contextPath}/login" method="post">

        <input type="email" name="email">
        <input type="text" name = "password">

        <input type="submit">
    </form>


</body>
</html>