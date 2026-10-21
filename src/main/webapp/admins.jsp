<%@ page import="DAO.AdminDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admins</title>
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

        /* ===== CONTEÚDO ===== */
        main {
            flex: 1;
            padding: 32px;
            overflow-x: auto;
        }

        .topo {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .btn-adicionar {
            background-color: #2563eb;
            color: #fff;
            text-decoration: none;
            padding: 10px 16px;
            border-radius: 6px;
            font-size: 14px;
        }

        .btn-adicionar:hover {
            background-color: #1d4ed8;
        }

        /* ===== TABELA ===== */
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 16px;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
        }

        thead th {
            background-color: azure;
            font-size: 14px;
        }

        tbody tr:hover {
            background-color: #f9fafb;
        }

        .acoes form {
            display: flex;
            gap: 8px;
        }

        .acoes button {
            border: none;
            border-radius: 4px;
            padding: 6px 12px;
            color: #fff;
            cursor: pointer;
            font-size: 13px;
        }

        .acoes a {
            display: inline-block;
            border: none;
            border-radius: 4px;
            padding: 6px 12px;
            color: #fff;
            cursor: pointer;
            font-size: 13px;
            text-decoration: none;
            background-color: #f59e0b;
        }

        .acoes button[value="edit"] {
            background-color: #f59e0b;
        }

        .acoes button[value="delete"] {
            background-color: #dc2626;
        }

        <!--Popup-->
            dialog {
                width: min(280px, 60vw);   /* no máximo 420px; em tela pequena, 90% da largura */
                height: auto;              /* altura só do conteúdo */
                max-height: 60vh;          /* nunca passa de 90% da altura da tela */
                overflow-y: auto;
                margin: auto;              /* centraliza */
                padding: 24px;
                border: none;
                border-radius: 8px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
            }

        dialog::backdrop {
            background: rgba(0, 0, 0, 0.5);
        }

        .form-admin {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .form-admin input {
            padding: 10px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
        }

        .form-admin .botoes {
            display: flex;
            gap: 8px;
            justify-content: flex-end;
        }

        .form-admin button {
            border: none;
            border-radius: 6px;
            padding: 8px 14px;
            color: #fff;
            cursor: pointer;
        }

        .form-admin .salvar { background-color: #2563eb; }
        .form-admin .cancelar { background-color: #6b7280; }
    </style>
</head>
<body>

<aside>
    <a href="home.html">Início</a>
    <a href="${pageContext.request.contextPath}/admins">Admins</a>
    <a href="${pageContext.request.contextPath}/regionalManagers">Gerente</a>
    <a href="loja.jsp">Loja</a>
    <a href="regiao.jsp">Região</a>
    <a href="pedido.jsp">Pedidos</a>
    <a href="faq.jsp">Faq</a>
</aside>

<main>
    <div class="topo">
        <h1>Admins</h1>
        <a class="btn-adicionar" data-modal
           href="${pageContext.request.contextPath}/admins?action=new">Adicionar admin</a>
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
        <c:forEach var="admin" items="${admins}">
            <tr>
                <td>${admin.id}</td>
                <td>${admin.name}</td>
                <td>${admin.email}</td>
                <td class="acoes">
                    <a data-modal
                       href="${pageContext.request.contextPath}/admins?action=edit&id=${admin.id}">Editar</a>

                    <form action="${pageContext.request.contextPath}/admins" method="post">
                        <input type="hidden" name="id" value="${admin.id}">

                        <button type="submit" name="action" value="delete">Deletar</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <dialog id="modal-admin"></dialog>

    <script>
        const modal = document.getElementById('modal-admin');

        document.querySelectorAll('a[data-modal]').forEach(function (link) {
            link.addEventListener('click', function (e) {
                e.preventDefault();
                abrirModal(link.href);
            });
        });

        async function abrirModal(url) {
            const resp = await fetch(url);
            if (!resp.ok) {
                alert('Não foi possível carregar o formulário.');
                return;
            }
            modal.innerHTML = await resp.text();
            modal.showModal();
        }

        // fecha ao clicar fora (no fundo escuro)
        modal.addEventListener('click', function (e) {
            if (e.target === modal) modal.close();
        });
    </script>
</main>
</body>
</html>