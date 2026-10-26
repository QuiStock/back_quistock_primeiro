package Controller;

import DAO.RegionalManagerDAO;
import DAO.RegionTypeDAO;
import Model.RegionalManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

//encaminha pra pagina de gerentes
@WebServlet({"/gerentes", "/regionalManagers"})
public class RegionalManagerServlet extends HttpServlet {

    private final RegionalManagerDAO dao = new RegionalManagerDAO();
    private final RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    //lista os gerentes na tela nova
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String erro = request.getParameter("erro");

        try {
            request.setAttribute("itens", dao.select());
            request.setAttribute("regioes", regionTypeDAO.readRegionTypes());
            request.setAttribute("erro", erro);
            request.getRequestDispatcher("/WEB-INF/views/gerentes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar gerentes", e);
        }
    }

    //verifica a acao enviada pelo formulario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("acao");
        if (action == null) action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            return;
        }

        try {
            switch (action) {
                case "salvar":
                    saveManager(request, response);
                    break;
                case "add":
                    addManager(request, response);
                    break;
                case "edit":
                    updateManager(request, response);
                    break;
                case "remover":
                case "delete":
                    deleteManager(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                response.sendRedirect(request.getContextPath() + "/gerentes?erro=duplicado");
            } else if ("23503".equals(e.getSQLState())) {
                response.sendRedirect(request.getContextPath() + "/gerentes?erro=regiao");
            } else {
                throw new ServletException("Erro ao salvar gerente", e);
            }
        }
    }

    //id vazio cria um gerente, id preenchido atualiza
    private void saveManager(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            addManager(request, response);
        } else {
            updateManager(request, response);
        }
    }

    //cria o gerente com os dados enviados pelo formulario
    private void addManager(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("name");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String regionTypeId = request.getParameter("regionTypeId");
        if (regionTypeId == null) regionTypeId = request.getParameter("region-type");

        String error = validateFields(name, email);
        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=" + error);
            return;
        }

        if (password == null || password.trim().isEmpty() || regionTypeId == null || regionTypeId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "O cadastro de gerente precisa de senha e região");
            return;
        }

        if (dao.searchByEmail(email.trim()) != null) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=duplicado");
            return;
        }

        RegionalManager manager = new RegionalManager(
                email.trim(), name.trim(), password, parseId(regionTypeId));

        if (regionTypeDAO.foundRegionType(manager.getRegionTypeId()) == null) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=regiao");
            return;
        }

        dao.insert(manager);
        response.sendRedirect(request.getContextPath() + "/gerentes");
    }

    //altera nome, email e regiao sem apagar senha
    private void updateManager(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(request.getParameter("id"));
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("name");

        String email = request.getParameter("email");
        int regionTypeId = parseId(request.getParameter("regionTypeId"));

        String error = validateFields(name, email);
        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=" + error);
            return;
        }

        RegionalManager manager = dao.searchById(id);
        if (manager == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Gerente não encontrado");
            return;
        }

        RegionalManager other = dao.searchByEmail(email.trim());
        if (other != null && other.getId() != id) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=duplicado");
            return;
        }

        manager.setName(name.trim());
        manager.setEmail(email.trim());
        manager.setRegionTypeId(regionTypeId);

        if (regionTypeDAO.foundRegionType(regionTypeId) == null) {
            response.sendRedirect(request.getContextPath() + "/gerentes?erro=regiao");
            return;
        }

        if (dao.update(manager) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Gerente não encontrado");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/gerentes");
    }

    //remove o gerente pelo id
    private void deleteManager(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(request.getParameter("id"));

        if (dao.delete(id) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Gerente não encontrado");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/gerentes");
    }

    //confere o id antes de consultar o db
    private int parseId(String value) {
        int id = Integer.parseInt(value);
        if (id <= 0) throw new NumberFormatException("ID inválido");
        return id;
    }

    //retorna o erro que o jsp vai mostrar
    private String validateFields(String name, String email) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return "vazio";
        }
        if (name.trim().length() > 100 || email.trim().length() > 150) {
            return "tamanho";
        }
        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return "email";
        }
        return null;
    }
}
