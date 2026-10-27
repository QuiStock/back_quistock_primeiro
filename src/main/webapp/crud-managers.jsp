<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/loading.css">
<form class="form-admin" action="${pageContext.request.contextPath}/regionalManagers" method="post">
    <h2>${empty manager ? 'Adicionar gerente' : 'Editar gerente'}</h2>

    <input type="hidden" value="${manager.id}" name="id">
    <input type="hidden" value="${empty manager ? 'add' : 'edit'}" name="action">

    <input type="text" name="name" placeholder="Nome" value="${manager.name}" required>
    <input type="email" name="email" placeholder="Email" value="${manager.email}" required>
    <input type="password" name="password"
           placeholder="${empty manager ? 'Senha' : 'Nova senha (vazio mantém a atual)'}"
    ${empty manager ? 'required' : ''}>

    <select name="region-type" required>
        <option value="">Selecione a região</option>
        <c:forEach var="r" items="${regionTypes}">
            <option value="${r.id}" ${r.id == manager.regionTypeId ? 'selected' : ''}>${r.name}</option>
        </c:forEach>
    </select>

    <div class="botoes">
        <button type="submit" class="salvar">Salvar</button>
        <button type="button" class="cancelar" onclick="this.closest('dialog').close()">Cancelar</button>
    </div>
</form>
<script src="${pageContext.request.contextPath}/assets/js/loading.js"></script>
