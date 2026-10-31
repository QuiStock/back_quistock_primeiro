<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/landing-assets/asa-roxa.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/loading.css">
    <title>Login | QuiStock</title>
</head>
<body>
    <div id="container">

        <div id="left-wrapper">
            <form action="${pageContext.request.contextPath}/login" method="POST" id="left">
                <h1>Login</h1>
                <input type="email" id="emailAdmin" placeholder="Email" name="email" required>
                <input type="password" id="senhaAdmin" placeholder="Senha" name="password" required>
                <button type="submit">Entrar</button>
                <c:if test="${not empty error}"><p class="login-error" role="alert"><c:out value="${error}" /></p></c:if>
            </form>    

                <div id="low">
                    <p id="pequeno">Nos acompanhe</p>
                    <a href="https://www.instagram.com/quistockk/" target="_blank" rel="noopener">
                        <img src="landing-assets/login-assets/instagram-preto.svg" alt="Instagram">
                    </a>
                    <a href="mailto:quistockinterdisciplinar@gmail.com" target="_blank">
                        <img src="landing-assets/login-assets/email.svg" alt="Email">
                    </a>
                </div>
        </div>

        <div id="right">
            <div id="box">
                <img src="landing-assets/logo-branca.png" alt="">
                <h1>Bem-vindo de volta!</h1>
                <p>Acesse o QuiStock para monitorar seus produtos em tempo real. Digite suas credenciais ao lado e continue evoluindo sua operação hoje mesmo.</p>
            </div>

            <div id="interacao">
                <div id="box2"><p id="fala">Já tem cadastro?</p></div>
                <div id="botoes">
                   <button type="button" class="escolha" id="escolha1">Não tenho</button>
                    <button type="button" class="escolha" id="escolha2">Tenho, obrigado(a)</button> 
                </div>
                
            </div>
            
        </div>
        <img src="landing-assets/login-assets/fefo-alegre.png" alt="fefo" class="fefox">
    </div>

    <script src="script/login.js" defer></script>
    <script src="assets/js/loading.js"></script>
    <script src="script/public-loading.js"></script>
</body>
</html>
