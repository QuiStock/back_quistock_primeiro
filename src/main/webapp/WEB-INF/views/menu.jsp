<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<nav class="links">
    <a href="${ctx}/inicio" class="${param.ativo eq 'inicio' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/inicio.svg" alt="">Início</a>
    <a href="${ctx}/admins" class="${param.ativo eq 'admins' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/admin.svg" alt="">Admins</a>
    <a href="${ctx}/regionalManagers" class="${param.ativo eq 'gerentes' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/gerente.svg" alt="">Gerentes</a>
    <a href="${ctx}/regioes" class="${param.ativo eq 'regioes' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/regiao.svg" alt="">Regiões</a>
    <a href="${ctx}/stores" class="${param.ativo eq 'lojas' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/loja.svg" alt="">Lojas</a>
    <a href="${ctx}/orders" class="${param.ativo eq 'pedido' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/pedido.svg" alt="">Pedidos</a>
    <a href="${ctx}/sales" class="${param.ativo eq 'promocao' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/promocao.svg" alt="">Promoções</a>
    <a href="${ctx}/products" class="${param.ativo eq 'produtos' ? 'ativo' : ''}"><img src="${ctx}/assets/icons/produto.svg" alt="">Produtos</a>
</nav>
