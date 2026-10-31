<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/landing-assets/asa-roxa.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>408</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error408.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css?v=2">
</head>
<body>

<div class="container">

    <h1>4</h1>
    <div>
        <img src="${pageContext.request.contextPath}/landing-assets/error-assets/clock-pointer.svg" alt="Ponteiro do relógio">
    </div>

    <h1>8</h1>

</div>

<p>O tempo limite da solicitação foi atingido.</p>

<form action="${pageContext.request.contextPath}${empty sessionScope.loggedUser ? '/' : '/inicio'}" method="get"><button type="submit">Voltar ao início</button></form>

    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
