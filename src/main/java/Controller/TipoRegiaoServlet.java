package Controller;

import DAO.TipoRegiaoDAO;
import Model.TipoRegiao;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina tiposRegiao
@WebServlet("/tiposRegiao")
public class TipoRegiaoServlet extends HttpServlet{

    private TipoRegiaoDAO tipoRegiaoDAO = new TipoRegiaoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "tiposRegiaoList";

        switch (action){
            case "editar":
                showUpdateForm(request, response);
                break;
            case "deletar":
                deleteTipoRegiao(request, response);
                break;
            default:
                readTiposRegiao(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action){
            case "cadastrar":
                createTipoRegiao(request, response);
                break;
            case "editar":
                updateTipoRegiao(request, response);
                break;
        }
    }

    private void readTiposRegiao(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<TipoRegiao> tiposRegiaoList = tipoRegiaoDAO.readTiposRegiao();

            request.setAttribute("tiposRegiaoList", tiposRegiaoList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("tiposRegiao.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipos de regiao", e);
        }
    }

    private void createTipoRegiao(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String nome = request.getParameter("tipoRegiaoName");
        int lojaCodigo = Integer.parseInt(request.getParameter("tipoRegiaoLojaCode"));

        TipoRegiao tipoRegiao = new TipoRegiao();
        tipoRegiao.setNome(nome);
        tipoRegiao.setLojaCodigo(lojaCodigo);

        try {
            tipoRegiaoDAO.createTipoRegiao(tipoRegiao);
            response.sendRedirect("tiposRegiao");
        }catch (SQLException e){
            throw new ServletException("Erro ao criar tipo de regiao", e);
        }
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("tipoRegiaoCode"));

        try {
            TipoRegiao tipoRegiao = tipoRegiaoDAO.foundTipoRegiao(codigo);
            request.setAttribute("tipoRegiao", tipoRegiao);

            RequestDispatcher dispatcher = request.getRequestDispatcher("editarTipoRegiao.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipo de regiao", e);
        }
    }

    private void updateTipoRegiao(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        TipoRegiao tipoRegiao = new TipoRegiao();

        int codigo = Integer.parseInt(request.getParameter("tipoRegiaoCode"));
        String nome = request.getParameter("tipoRegiaoName");
        int lojaCodigo = Integer.parseInt(request.getParameter("tipoRegiaoLojaCode"));

        tipoRegiao.setCodigo(codigo);
        tipoRegiao.setNome(nome);
        tipoRegiao.setLojaCodigo(lojaCodigo);

        try {
            tipoRegiaoDAO.updateTipoRegiao(tipoRegiao);
            response.sendRedirect("tiposRegiao");
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar tipo de regiao", e);
        }
    }

    private void deleteTipoRegiao(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("tipoRegiaoCode"));

        try {
            tipoRegiaoDAO.deleteTipoRegiao(codigo);
            response.sendRedirect("tiposRegiao");
        } catch (SQLException e) {
            throw new ServletException("Erro ao deletar tipo de regiao", e);
        }
    }

}