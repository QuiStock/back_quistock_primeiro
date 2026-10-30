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

@WebServlet({"/regionTypes", "/regioes"})
public class RegionTypeServlet extends HttpServlet{

    private RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    // Handles do, Get
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        // Applying the method logic, using the provided values
        String action = request.getParameter("action");
        if (action == null) action = "regionTypesList";

        switch (action){
            default:
                readRegionTypes(request, response);
        }
    }

    // Handles do, Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Read submitted names as UTF-8.
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("acao");
        if (action == null) action = request.getParameter("action");
        if (action == null) action = "";

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

    // Handles read, Region, Types
    private void readRegionTypes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        // Applying the method logic, using the provided values
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

    // Handles create, Region, Type
    private void createRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        // Applying the method logic, using the provided values
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("regionTypeName");

        if(!nomeValidado(name)){
            request.setAttribute("erro", "Digite um nome válido para o tipo de região.");
            readRegionTypes(request, response);
            return;
        }

        RegionType regionType = new RegionType();
        regionType.setName(name.trim());

        try {
            regionTypeDAO.createRegionType(regionType);
            response.sendRedirect(request.getContextPath() + "/regioes");

        }catch (SQLException e){
            throw new ServletException("Erro ao criar tipo de regiao", e);
        }
    }

    // Handles update, Region, Type
    private void updateRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        // Applying the method logic, using the provided values
        RegionType regionType = new RegionType();

        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("regionTypeName");

        int id = Integer.parseInt(request.getParameter("id"));
        regionType.setId(id);

        if(!nomeValidado(name)){
            request.setAttribute("erro", "Digite um nome válido para o tipo de região.");
            readRegionTypes(request, response);
            return;
        }


        regionType.setName(name.trim());


        try {
            regionTypeDAO.updateRegionType(regionType);
            response.sendRedirect(request.getContextPath() + "/regioes");

        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar tipo de regiao", e);
        }
    }


    // Handles delete, Region, Type
    private void deleteRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        // Applying the method logic, using the provided values
        String idValue = request.getParameter("id");
        if (idValue == null) idValue = request.getParameter("regionTypeId");
        int id = Integer.parseInt(idValue);

        try {
            regionTypeDAO.deleteRegionType(id);
            response.sendRedirect(request.getContextPath() + "/regioes");

        } catch (SQLException e) {
            response.sendError(500, e.getMessage());
        }
    }

    // Handles nome, Validado
    private boolean nomeValidado(String name){

        if (name == null){
            return false;
        }

        name = name.trim();

        // Allow letters with internal spaces, apostrophes, or hyphens.
        String regex = "^[\\p{L}]+(?:[ '\\u2019-][\\p{L}]+)*$";

        return name.trim().length() <=100 && name.matches(regex);
    }

}
