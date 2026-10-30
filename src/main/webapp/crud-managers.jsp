<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<form class="form-admin" action="${pageContext.request.contextPath}/regionalManagers" method="post" novalidate>
    <h2>${empty manager ? 'Adicionar gerente' : 'Editar gerente'}</h2>

    <input type="hidden" value="${manager.id}" name="id">
    <input type="hidden" value="${empty manager ? 'add' : 'edit'}" name="action">

    <div class="campo">
        <label for="gerente-nome">Nome</label>
        <input id="gerente-nome" type="text" name="name" value="${manager.name}" required>
    </div>
    <div class="campo">
        <label for="gerente-email">Email</label>
        <input id="gerente-email" type="email" name="email" value="${manager.email}" required>
    </div>
    <div class="campo">
        <label for="gerente-senha">${empty manager ? 'Senha' : 'Nova senha (vazio mantém a atual)'}</label>
        <input id="gerente-senha" type="password" name="password" ${empty manager ? 'required' : ''}>
    </div>

    <div class="campo">
        <label for="gerente-regiao">Região</label>
        <select id="gerente-regiao" name="region-type" required>
            <option value="">Selecione a região</option>
            <c:forEach var="r" items="${regionTypes}">
                <option value="${r.id}" ${r.id == manager.regionTypeId ? 'selected' : ''}>${r.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="acoes-popup">
        <button type="button" class="botao" onclick="this.closest('dialog').close()">Cancelar</button>
        <button type="submit" class="botao primario">Salvar</button>
    </div>
</form>
