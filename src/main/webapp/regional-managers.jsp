<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

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
            <h1>Gerentes regionais</h1>
            <a class="btn-adicionar" data-modal
               href="${pageContext.request.contextPath}/regionalManagers?action=new">Adicionar gerente</a>
        </div>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Região</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="manager" items="${managers}">
                <tr>
                    <td>${manager.id}</td>
                    <td>${manager.name}</td>
                    <td>${manager.email}</td>
                    <td>${manager.regionTypeName}</td>
                    <td class="acoes">
                        <a data-modal
                           href="${pageContext.request.contextPath}/regionalManagers?action=edit&id=${manager.id}">Editar</a>

                        <form action="${pageContext.request.contextPath}/regionalManagers" method="post">
                            <input type="hidden" name="id" value="${manager.id}">

                            <button type="submit" name="action" value="delete">Deletar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <dialog id="modal-manager"></dialog>
    </main>
    <script>
        const modal = document.getElementById('modal-manager');

        document.querySelectorAll('a[data-modal]').forEach(function (link) {
            link.addEventListener('click', function (e) {
                e.preventDefault();
                abrirModal(link.href);
            });
        });

        async function abrirModal(url) {
            window.mostrarLoading();

            try {
                const resp = await fetch(url);
                if (!resp.ok) {
                    alert('Não foi possível carregar o formulário.');
                    return;
                }
                modal.innerHTML = await resp.text();
                modal.showModal();
            } catch (e) {
                alert('Não foi possível carregar o formulário.');
            } finally {
                window.esconderLoading();
            }
        }

        // fecha ao clicar fora (no fundo escuro)
        modal.addEventListener('click', function (e) {
            if (e.target === modal) modal.close();
        });
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
