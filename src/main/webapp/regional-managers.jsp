<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <title>Gerentes regionais</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quistock.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css?v=2">
</head>
<body>
    <div id="loading" class="loading hidden" aria-hidden="true">
        <div class="chaotic-orbit"></div>
    </div>

    <aside>
        <img src="${pageContext.request.contextPath}/assets/logo-branca.png" alt="QuiStock" id="logo">
        <jsp:include page="/WEB-INF/views/menu.jsp"><jsp:param name="ativo" value="gerentes"/></jsp:include>
    </aside>

    <main>
        <h1>Gerentes regionais</h1>
        <c:if test="${not empty erro}"><p class="alerta" id="erro" role="alert"><c:out value="${erro}"/></p></c:if>
        <c:choose>
            <c:when test="${empty managers}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <a class="botao verde" data-modal href="${pageContext.request.contextPath}/regionalManagers?action=new">Adicionar gerente</a>
                </div>
            </c:when>
            <c:otherwise>
                <div id="lista">
                    <div id="botoes">
                        <a class="botao" data-modal href="${pageContext.request.contextPath}/regionalManagers?action=new"><img src="${pageContext.request.contextPath}/assets/icons/adicionar.svg" alt="">Adicionar</a>
                    </div>
                    <div id="tabela" class="tabela-tres-colunas">
                        <div id="cabecalho">
                            <h3>Nome</h3>
                            <h3>Email</h3>
                            <h3>Região</h3>
                            <h3>Ações</h3>
                        </div>
                        <c:forEach var="manager" items="${managers}">
                            <div class="linha">
                                <span><c:out value="${manager.name}"/></span>
                                <span><c:out value="${manager.email}"/></span>
                                <span><c:out value="${manager.regionTypeName}"/></span>
                                <div class="acoes-linha">
                                    <a class="botao pequeno" data-modal href="${pageContext.request.contextPath}/regionalManagers?action=edit&id=${manager.id}"><img src="${pageContext.request.contextPath}/assets/icons/editar.svg" alt="">Editar</a>
                                    <form action="${pageContext.request.contextPath}/regionalManagers" method="post">
                                        <input type="hidden" name="id" value="${manager.id}">
                                        <button class="botao pequeno perigo" type="submit" name="action" value="delete"><img src="${pageContext.request.contextPath}/assets/icons/remover.svg" alt="">Deletar</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

        <dialog id="modal-manager" class="popup"></dialog>
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

        modal.addEventListener('click', function (e) {
            if (e.target === modal) modal.close();
        });
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
</body>
</html>
