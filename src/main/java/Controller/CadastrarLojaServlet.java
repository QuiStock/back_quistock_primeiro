package Controller;

import DAO.LojaDAO;
import Model.Loja;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cadastrarLoja")
public class CadastrarLojaServlet extends HttpServlet{

    private LojaDAO lojaDAO = new LojaDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        //define as variáveis conforme o usuario colocou
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String endereco_id = request.getParameter("endereco_id");
        String regiao_id = request.getParameter("regiao_id");

        Loja loja = new Loja();
        loja.setEmail(email);
        loja.setSenha(senha);
        loja.setEndereco_id(endereco_id);
        loja.setRegiao_id(regiao_id);

        //salva a loja no banco de dados
        try{
            lojaDAO.create(loja);
            response.sendRedirect("lojas");// manda devolta pra pagina das lojas

        } catch (SQLException e){
            throw new ServletException("Erro ao criar loja", e);
        }

    }

}
