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
@WebServlet("/regionTypes")
public class RegionTypeServlet extends HttpServlet{

    private RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "regionTypesList";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action){
            case "edit":
                showUpdateForm(request, response); //aparecer a interface pra editar
                break;
            default:
                readRegionTypes(request, response);
        }
    }

    //doPost pra alterar tabela, seja criando, editando ou deletando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //traz pro teclado brasileiro
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action) {
            case "register":
                createRegionType(request, response);
                break;
            case "edit":
                updateRegionType(request, response); //editar de fato
                break;
            case "delete":
                deleteRegionType(request, response);
                break;
        }
    }

    private void readRegionTypes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<RegionType> regionTypesList = regionTypeDAO.readRegionTypes();

            request.setAttribute("regionTypesList", regionTypesList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("regionTypes.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipos de regiao", e);
        }
    }

    private void createRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String name = request.getParameter("regionTypeName");
        int storeId = Integer.parseInt(request.getParameter("regionTypeStoreId"));

        RegionType regionType = new RegionType();
        regionType.setName(name);
        regionType.setStoreId(storeId);

        try {
            regionTypeDAO.createRegionType(regionType);
            response.sendRedirect("regionTypes");
        }catch (SQLException e){
            throw new ServletException("Erro ao criar tipo de regiao", e);
        }
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int id = Integer.parseInt(request.getParameter("regionTypeId"));

        try {
            RegionType regionType = regionTypeDAO.foundRegionType(id);
            request.setAttribute("regionType", regionType);

            RequestDispatcher dispatcher = request.getRequestDispatcher("editRegionType.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipo de regiao", e);
        }
    }

    private void updateRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        RegionType regionType = new RegionType();

        int id = Integer.parseInt(request.getParameter("regionTypeId"));
        String name = request.getParameter("regionTypeName");
        int storeId = Integer.parseInt(request.getParameter("regionTypeStoreId"));

        regionType.setId(id);
        regionType.setName(name);
        regionType.setStoreId(storeId);

        try {
            regionTypeDAO.updateRegionType(regionType);
            response.sendRedirect("regionTypes");
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar tipo de regiao", e);
        }
    }

    private void deleteRegionType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int id = Integer.parseInt(request.getParameter("regionTypeId"));

        try {
            regionTypeDAO.deleteRegionType(id);
            response.sendRedirect("regionTypes");
        } catch (SQLException e) {
            throw new ServletException("Erro ao deletar tipo de regiao", e);
        }
    }

}