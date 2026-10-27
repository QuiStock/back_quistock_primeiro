<%--
  Created by IntelliJ IDEA.
  User: 20260038-ieg
  Date: 16/10/2026
  Time: 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            display: flex;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        /* ===== SIDEBAR ===== */
        aside {
            width: 220px;
            min-height: 100vh;
            background-color: #1f2937;
            display: flex;
            flex-direction: column;
            padding: 20px 0;
            position: sticky;
            top: 0;
            height: 100vh;
        }

        aside a {
            color: #d1d5db;
            text-decoration: none;
            padding: 12px 24px;
            transition: background-color 0.2s, color 0.2s;
        }

        aside a:hover {
            background-color: #374151;
            color: #fff;
        }

    </style>
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

    <aside>
        <a href="${pageContext.request.contextPath}/inicio">Início</a>
        <a href="${pageContext.request.contextPath}/admins">Admins</a>
        <a href="${pageContext.request.contextPath}/regionalManagers">Gerentes</a>
        <a href="${pageContext.request.contextPath}/stores">Loja</a>
        <a href="${pageContext.request.contextPath}/regioes">Região</a>
        <a href="${pageContext.request.contextPath}/faq.jsp">Faq</a>
    </aside>
    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
