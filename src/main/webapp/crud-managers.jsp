<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form class="form-admin" action="${pageContext.request.contextPath}/regionalManagers" method="post">
    <h2>${empty admin ? 'Adicionar gerente' : 'Editar gerente'}</h2>

    <input type="hidden" value="${manager.id}" name="id">
    <input type="hidden" value="${empty manager ? 'add' : 'update'}" name="action">

    <input type="text" name="name" placeholder="Nome" value="${manager.name}" required>
    <input type="email" name="email" placeholder="Email" value="${manager.email}" required>
    <input type="text" name="password" placeholder="Senha" value="${manager.password}" required>

    <div class="botoes">
        <button type="submit" class="salvar">Salvar</button>
        <button type="button" class="cancelar" onclick="this.closest('dialog').close()">Cancelar</button>
    </div>
</form>