<%@ page import="DAO.AdminDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
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

        table {
            display: grid;
            grid-template-columns: auto auto auto;
            background-color: azure;
            gap: 5px;
        }

    </style>
</head>
<body>
<div class="esquerda">
    <aside>
        <a href="home.html">Início</a>
        <a href="${pageContext.request.contextPath}/admins">Admins</a>
        <a href="gerente.jsp">Gerente</a>
        <a href="loja.jsp">Loja</a>
        <a href="regiao.jsp">Região</a>
        <a href="pedido.jsp">Pedidos</a>
        <a href="faq.jsp">Faq</a>
    </aside>
</div>
<div class="direita">
    <a href="crud-admin.jsp">Adicionar admin</a>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
            </tr>
        </thead>


        <c:forEach var="admin" items="${admins}">
            <div class="linha-admin">
                <tr>
                    <td>${admin.id}</td>
                    <td>${admin.name}</td>
                    <td>${admin.email}</td>
                </tr>

                <form action="${pageContext.request.contextPath}/admins" method="post">

                    <input type="hidden" name="id" value="${admin.id}">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" value="delete">
                    
                    <input type="submit" name="action" value="edit">Editar</input>
                    <input type="submit" name="action" value="delete">deletar</input>
                </form>

            </div>
        </c:forEach>
    </table>

</div>
</body>
</html>