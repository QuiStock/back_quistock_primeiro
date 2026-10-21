<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Se usar Tomcat 9 / javax, troque a linha acima por:
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    TEMPLATE ÚNICO das telas de lista (Admins, Gerente, FAQ, Região, Anotações).
    O Servlet envia três coisas:
      tela        -> título, nome no singular, caminho e campos da tela
      itens       -> a lista de registros vinda do banco
      menuAtivo   -> qual item do menu fica destacado
--%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <%@ include file="/WEB-INF/fragmentos/head.jspf" %>
    <title>QuiStock - <c:out value="${tela.titulo}"/></title>
</head>
<body>

    <%@ include file="/WEB-INF/fragmentos/menu.jspf" %>

    <main>
        <h1><c:out value="${tela.titulo}"/></h1>

        <%-- Mensagem de erro (o Servlet redireciona com ?erro=codigo) --%>
        <c:if test="${not empty param.erro}">
            <p class="alerta" role="alert">
                <c:choose>
                    <c:when test="${param.erro eq 'vazio'}">Preencha todos os campos antes de salvar.</c:when>
                    <c:when test="${param.erro eq 'tamanho'}">Algum campo ultrapassou o tamanho permitido.</c:when>
                    <c:when test="${param.erro eq 'email'}">Informe um email válido.</c:when>
                    <c:when test="${param.erro eq 'duplicado'}">Esse email já está cadastrado.</c:when>
                    <c:otherwise>Não foi possível concluir a operação. Tente novamente.</c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <c:choose>

            <%-- Sem registros: tela "Área vazia" --%>
            <c:when test="${empty itens}">
                <div id="vazio">
                    <h2>Área vazia</h2>
                    <button type="button" class="botao verde" id="btnAdicionar">Adicionar <c:out value="${tela.singular}"/></button>
                </div>
            </c:when>

            <%-- Com registros: botões + cabeçalho + linhas --%>
            <c:otherwise>
                <div id="lista">
                    <%-- Aviso exibido quando Editar/Remover é clicado sem nada selecionado --%>
                    <p id="aviso" class="alerta" role="alert" hidden></p>

                    <div id="botoes">
                        <button type="button" class="botao" id="btnAdicionar"><img src="${ctx}/assets/icons/adicionar.svg" alt="">Adicionar</button>
                        <button type="button" class="botao" id="btnEditar"><img src="${ctx}/assets/icons/editar.svg" alt="">Editar</button>
                        <button type="button" class="botao perigo" id="btnRemover"><img src="${ctx}/assets/icons/remover.svg" alt="">Remover</button>
                    </div>

                    <div id="tabela" style="--colunas: ${tela.totalCampos}">
                        <div id="cabecalho">
                            <c:forEach var="campo" items="${tela.campos}">
                                <h3><c:out value="${campo.rotulo}"/></h3>
                            </c:forEach>
                        </div>

                        <c:forEach var="item" items="${itens}">
                            <div class="linha" data-id="${item.id}">
                                <c:forEach var="campo" items="${tela.campos}">
                                    <span data-campo="${campo.nome}"><c:out value="${item[campo.nome]}"/></span>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>

        </c:choose>
    </main>

    <%@ include file="/WEB-INF/fragmentos/popup.jspf" %>

    <script src="${ctx}/js/crud.js"></script>
</body>
</html>
