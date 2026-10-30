<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<form class="form-admin" action="${pageContext.request.contextPath}/products" method="post">
    <h2 id="titulo-produto">${empty product ? 'Adicionar produto' : 'Editar produto'}</h2>
    <input type="hidden" name="action" value="${empty product ? 'new' : 'edit'}">
    <c:if test="${not empty product}"><input type="hidden" name="id" value="${product.id}"></c:if>

    <div class="campo">
        <label for="produto-nome">Nome</label>
        <input id="produto-nome" name="name" value="<c:out value='${product.name}'/>" required>
    </div>
    <c:if test="${empty product}">
    <div class="campo">
        <label for="produto-marca">Marca</label>
        <input id="produto-marca" name="brand" value="<c:out value='${product.brand}'/>" required>
    </div>
    <div class="campo">
        <label for="produto-linha">Linha</label>
        <input id="produto-linha" name="line" value="<c:out value='${product.line}'/>" required>
    </div>
    </c:if>
    <div class="campo">
        <label for="produto-lote">Lote</label>
        <input id="produto-lote" name="batch" value="<c:out value='${product.batch}'/>" required>
    </div>
    <div class="campo">
        <label for="produto-preco">Preço (R$)</label>
        <input id="produto-preco" type="number" name="price" min="0" step="0.01" value="${product.price}" required>
    </div>
    <div class="campo">
        <label for="produto-quantidade">Quantidade em estoque</label>
        <input id="produto-quantidade" type="number" name="quantity" min="0" step="1" value="${product.quantity}" required>
    </div>
    <div class="campo">
        <label for="produto-validade">Validade</label>
        <input id="produto-validade" type="date" name="expirationDate" value="${product.expirationDate}" required>
    </div>
    <c:if test="${empty product}">
    <div class="campo">
        <label for="produto-loja">Loja</label>
        <c:choose>
            <c:when test="${empty product}">
                <select id="produto-loja" name="store_id" required>
                    <option value="">Selecione a loja</option>
                    <c:forEach var="store" items="${stores}">
                        <option value="${store.id}"><c:out value="${store.email}"/></option>
                    </c:forEach>
                </select>
                <c:if test="${empty stores}"><p>Cadastre uma loja antes de adicionar produtos.</p></c:if>
            </c:when>
            <c:otherwise>
                <input id="produto-loja" value="<c:out value='${product.store_Name}'/>" readonly>
            </c:otherwise>
        </c:choose>
    </div>
    </c:if>
    <div class="acoes-popup">
        <button type="button" class="botao" onclick="this.closest('dialog').close()">Cancelar</button>
        <button type="submit" class="botao primario" ${empty product and empty stores ? 'disabled' : ''}>Salvar</button>
    </div>
</form>
