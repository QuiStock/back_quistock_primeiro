package Controller;

import DAO.RegiaoDAO;
import Model.Regiao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cadastrarRegiao")
public class CadastrarRegiaoServlet extends HttpServlet{

    private RegiaoDAO regiaoDAO = new RegiaoDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        //define as variáveis conforme o usuario colocou
        String nome = request.getParameter("nome");
        int gerente_regional_id = Integer.parseInt(request.getParameter("gerente_regional_id"));

        Regiao regiao = new Regiao();
        regiao.setNome(nome);
        regiao.setGerente_regional_id(gerente_regional_id);

        //salva a regiao no banco de dados
        try{
            regiaoDAO.create(regiao);
            response.sendRedirect("regioes");// manda devolta pra pagina das regioes

        } catch (SQLException e){
            throw new ServletException("Erro ao criar regiao", e);
        }

    }

}

