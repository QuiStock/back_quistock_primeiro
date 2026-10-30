<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form class="form-admin" action="${pageContext.request.contextPath}/admins" method="post" novalidate>
    <h2>${empty admin ? 'Adicionar admin' : 'Editar admin'}</h2>

    <input type="hidden" value="${admin.id}" name="id">
    <input type="hidden" value="${empty admin ? 'add' : 'update'}" name="action">

    <div class="campo">
        <label for="admin-nome">Nome</label>
        <input id="admin-nome" type="text" name="name" maxlength="100" value="${admin.name}" required>
    </div>
    <div class="campo">
        <label for="admin-email">Email</label>
        <input id="admin-email" type="email" name="email" maxlength="150" value="${admin.email}" required>
    </div>
    <div class="campo">
        <label for="admin-senha">${empty admin ? 'Senha' : 'Nova senha (vazio mantém a atual)'}</label>
        <input id="admin-senha" type="password" name="password" ${empty admin ? 'required' : ''}>
    </div>

    <div class="acoes-popup">
        <button type="button" class="botao" onclick="this.closest('dialog').close()">Cancelar</button>
        <button type="submit" class="botao primario">Salvar</button>
    </div>
</form>
