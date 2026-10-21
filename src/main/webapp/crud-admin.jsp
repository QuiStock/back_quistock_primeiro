<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/admins" method="post">

        <input type="hidden" value="${admin.id}" name="id">
        <input type="hidden" value="${empty admin ? 'add' : 'update'}" name="action">

        <input type="text" name="name" placeholder="nome" value="${admin.name}" required>
        <input type="email" name="email" placeholder="email" value="${admin.email}" required>
        <input type="text" name="password" placeholder="senha" value="${admin.password}" required>

        <input type="submit">

        <a href="${pageContext.request.contextPath}/admins">Voltar</a>

    </form>
</body>
</html>
