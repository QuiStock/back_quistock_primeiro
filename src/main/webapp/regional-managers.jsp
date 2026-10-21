<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <aside>
        <a href="home.html">Início</a>
        <a href="${pageContext.request.contextPath}/admins">Admins</a>
        <a href="${pageContext.request.contextPath}/regionalManagers">Gerentes</a>
        <a href="loja.jsp">Loja</a>
        <a href="regiao.jsp">Região</a>
        <a href="pedido.jsp">Pedidos</a>
        <a href="faq.jsp">Faq</a>
    </aside>

    <main>
        <div class="topo">
            <h1>Admins</h1>
            <a class="btn-adicionar" href="${pageContext.request.contextPath}/admins?action=new">Adicionar admin</a>
        </div>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="admin" items="${managers}">
                <tr>
                    <td>${admin.id}</td>
                    <td>${admin.name}</td>
                    <td>${admin.email}</td>
                    <td class="acoes">
                        <a href="${pageContext.request.contextPath}/admins?action=edit&id=${admin.id}">Editar</a>

                        <form action="${pageContext.request.contextPath}/admins" method="post">
                            <input type="hidden" name="id" value="${admin.id}">

                            <button type="submit" name="action" value="delete">Deletar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</body>
</html>
