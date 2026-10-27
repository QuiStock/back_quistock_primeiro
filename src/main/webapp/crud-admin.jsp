<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form class="form-admin" action="${pageContext.request.contextPath}/admins" method="post">
    <h2>${empty admin ? 'Adicionar admin' : 'Editar admin'}</h2>

    <input type="hidden" value="${admin.id}" name="id">
    <input type="hidden" value="${empty admin ? 'add' : 'update'}" name="action">

    <input type="text" name="name" placeholder="Nome" value="${admin.name}" required>
    <input type="email" name="email" placeholder="Email" value="${admin.email}" required>
    <input type="password" name="password"
           placeholder="${empty admin ? 'Senha' : 'Nova senha (vazio mantém a atual)'}"
    ${empty admin ? 'required' : ''}>

    <div class="botoes">
        <button type="submit" class="salvar">Salvar</button>
        <button type="button" class="cancelar" onclick="this.closest('dialog').close()">Cancelar</button>
    </div>
</form>