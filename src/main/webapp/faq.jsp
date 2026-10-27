<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
    <style>

        .esquerda {
            display: flex;
            width: 50%;
            height: 100%;
        }

        .direita {
            display: flex;
            width: 50%;
            height: 100%;
            justify-content: center;
            align-items: center;
        }

    </style>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

    <div class="esquerda">
        <aside>
        <a href="${pageContext.request.contextPath}/inicio">Início</a>
        <a href="${pageContext.request.contextPath}/admins">Admins</a>
        <a href="${pageContext.request.contextPath}/regionalManagers">Gerente</a>
        <a href="${pageContext.request.contextPath}/stores">Loja</a>
        <a href="${pageContext.request.contextPath}/regioes">Região</a>
        <a href="${pageContext.request.contextPath}/faq.jsp">Faq</a>
    </aside>
    </div>
    <div class="direita">
        <table>
            <tr>
                <td>

                </td>
            </tr>
        </table>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
