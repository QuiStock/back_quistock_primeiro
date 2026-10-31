<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/landing-assets/asa-roxa.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error500.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css?v=2">
</head>
<body>
<img src="${pageContext.request.contextPath}/landing-assets/error-assets/500_dissolucao.svg" alt="500">

<p id="mensagem">Ocorreu um erro no servidor. Tente novamente mais tarde.</p>

<form action="${pageContext.request.contextPath}${empty sessionScope.loggedUser ? '/' : '/inicio'}" method="get"><button type="submit">Voltar ao início</button></form>

    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
