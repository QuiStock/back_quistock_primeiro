package Controller;

import DAO.RegionTypeDAO;
import DAO.RegionalManagerDAO;
import Model.RegionalManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

//encaminha pra pagina gerentes
@WebServlet("/regionalManagers")
public class RegionalManagerServlet extends HttpServlet{

    private final RegionalManagerDAO dao = new RegionalManagerDAO();
    private final RegionTypeDAO regionDAO = new RegionTypeDAO();

    //doGet to determine the path, based on action
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                //empty insert forms
                req.setAttribute("regionTypes", regionDAO.select());
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            else if ("edit".equals(action)) {
                //filled forms with previous data
                int id = Integer.parseInt(req.getParameter("id"));
                RegionalManager manager = dao.searchById(id);

                if (manager == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Manager not found");
                    return;
                }

                //filling forms
                req.setAttribute("regionTypes", regionDAO.select());
                req.setAttribute("manager", manager);
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            else {
                req.setAttribute("managers", dao.select());
                req.getRequestDispatcher("/regional-managers.jsp").forward(req, resp);
            }

        } catch (SQLException ex) {

            resp.sendError(500, ex.getMessage());

        }

    }


    //doPost pra alterar tabela, seja criando, editando ou deletando
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        //traz pro teclado brasileiro
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        //averigua qual action que é e redireciona para o servlet correto
        try {
            switch (action) {
                case "add": add(req, resp); break;
                case "edit": update(req, resp); break;
                case "delete": delete(req, resp); break;
                default: doGet(req, resp); break;
            }
        } catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Inválido");
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void add (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException{
        RegionalManager manager = new RegionalManager(
                req.getParameter("email"),
                req.getParameter("name"),
                req.getParameter("password"),
                Integer.parseInt(req.getParameter("region-type"))
        );

        dao.insert(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException{

        int id = Integer.parseInt(req.getParameter("id"));
        RegionalManager manager = dao.searchById(id);

        if (manager == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin não encontrado");
            return;
        }

        manager.setName(req.getParameter("name"));
        manager.setEmail(req.getParameter("email"));
        manager.setPassword(req.getParameter("password"));
        manager.setRegionTypeId(Integer.parseInt(req.getParameter("region-type")));

        dao.update(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    private void delete (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException,  IOException{

        RegionalManager manager = new RegionalManager();
        manager.setId(Integer.parseInt(req.getParameter("id")));

        dao.delete(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }
}