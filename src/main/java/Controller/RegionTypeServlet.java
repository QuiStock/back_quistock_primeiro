package Controller;

import DAO.RegionTypeDAO;
import Model.RegionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina regionTypes
@WebServlet({"/regionTypes", "/regioes"})
public class RegionTypeServlet extends HttpServlet{

    private RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "regionTypesList";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action){
            default:
                readRegionTypes(request, response); //retorna a tela da lista de tipos de regiao
        }
    }

    //doPost pra alterar tabela, seja criando, editando ou deletando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //traz pro teclado brasileiro
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("acao");
        if (action == null) action = request.getParameter("action");
        if (action == null) action = "";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action) {
            case "salvar":
                if (request.getParameter("id") == null || request.getParameter("id").isBlank()) {
                    createRegionType(request, response);
                } else {
                    updateRegionType(request, response);
                }
                break;
            case "remover":
                deleteRegionType(request, response);
                break;
            case "register":
                createRegionType(request, response);
                break;
            case "edit":
                updateRegionType(request, response);
                break;
            case "delete":
                deleteRegionType(request, response);
                break;
        }
    }

    private void readRegionTypes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<RegionType> regionTypesList = regionTypeDAO.select();

            request.setAttribute("regionTypesList", regionTypesList);
            request.setAttribute("itens", regionTypesList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/regioes.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipos de regiao", e);
        }
    }

    private void createRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("regionTypeName");

        //verifica se atende aos requisitos
        if(!nomeValidado(name)){
            request.setAttribute("erro", "Digite um nome válido para o tipo de região.");
            readRegionTypes(request, response);
            return;
        }

        //define os atributos
        RegionType regionType = new RegionType();
        regionType.setName(name.trim());

        try {
            regionTypeDAO.createRegionType(regionType);
            response.sendRedirect(request.getContextPath() + "/regioes");

        //retorna erro caso nao consiga adicionar o tipo de regiao
        }catch (SQLException e){
            throw new ServletException("Erro ao criar tipo de regiao", e);
        }
    }

    //metodo pra atualizar/editar um tipo de regiao
    private void updateRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        RegionType regionType = new RegionType();

        //define os atributos
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("regionTypeName");

        int id = Integer.parseInt(request.getParameter("id"));
        regionType.setId(id);

        //verifica se atende aos requisitos
        if(!nomeValidado(name)){
            request.setAttribute("erro", "Digite um nome válido para o tipo de região.");
            readRegionTypes(request, response);
            return;
        }


        regionType.setName(name.trim());


        try {
            regionTypeDAO.updateRegionType(regionType);
            response.sendRedirect(request.getContextPath() + "/regioes");

        //retorna erro caso nao consiga editar o tipo de regiao
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar tipo de regiao", e);
        }
    }


    //metodo pra excluir tipo de regiao
    private void deleteRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String idValue = request.getParameter("id");
        if (idValue == null) idValue = request.getParameter("regionTypeId");
        int id = Integer.parseInt(idValue);

        try {
            regionTypeDAO.deleteRegionType(id);
            response.sendRedirect(request.getContextPath() + "/regioes");

            //retorna erro caso nao consiga editar o tipo de regiao
        } catch (SQLException e) {
            response.sendError(500, e.getMessage());
        }
    }

    //metodo regex pra validar o nome da região
    private boolean nomeValidado(String name){

        //se o nome for vazio retorna false
        if (name == null){
            return false;
        }

        //remove os espaços em branco nas pontas do nome
        name = name.trim();

        //define a limitação pro nome, sendo letras com acento no começo, ífen ou espaço no meio e letras no final no final
        String regex = "^[\\p{L}]+(?:[ '\\u2019-][\\p{L}]+)*$";

        //retorna se o nome atende aos requisitos do regex
        return name.trim().length() <=100 && name.matches(regex);
    }

}
