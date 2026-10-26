package Controller;

import DAO.RegionTypeDAO;
import Model.RegionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

//encaminha pra pagina de regioes
@WebServlet({"/regioes", "/regionTypes"})
public class RegionTypeServlet extends HttpServlet {

    private final RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    //lista as regioes na tela
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setAttribute("itens", regionTypeDAO.readRegionTypes());
            request.setAttribute("erro", request.getParameter("erro"));
            request.getRequestDispatcher("/WEB-INF/views/regioes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar tipos de regiao", e);
        }
    }

    //verifica a acao enviada pelo formulario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("acao");
        if (action == null) action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acao invalida");
            return;
        }

        try {
            switch (action) {
                case "salvar":
                    saveRegionType(request, response);
                    break;
                case "register":
                    createRegionType(request, response);
                    break;
                case "edit":
                    updateRegionType(request, response);
                    break;
                case "remover":
                case "delete":
                    deleteRegionType(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acao invalida");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID invalido");
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                response.sendRedirect(request.getContextPath() + "/regioes?erro=duplicado");
            } else if ("23503".equals(e.getSQLState())) {
                response.sendRedirect(request.getContextPath() + "/regioes?erro=vinculo");
            } else {
                throw new ServletException("Erro ao salvar tipo de regiao", e);
            }
        }
    }

    //id vazio cria uma regiao, id preenchido atualiza
    private void saveRegionType(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            createRegionType(request, response);
        } else {
            updateRegionType(request, response);
        }
    }

    //cria uma regiao com o nome enviado pelo formulario
    private void createRegionType(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = getName(request);

        if (!validName(name)) {
            response.sendRedirect(request.getContextPath() + "/regioes?erro=vazio");
            return;
        }

        RegionType regionType = new RegionType();
        regionType.setName(name.trim());
        regionTypeDAO.createRegionType(regionType);
        response.sendRedirect(request.getContextPath() + "/regioes");
    }

    //atualiza o nome da regiao
    private void updateRegionType(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(getId(request));
        String name = getName(request);

        if (!validName(name)) {
            response.sendRedirect(request.getContextPath() + "/regioes?erro=vazio");
            return;
        }

        RegionType regionType = new RegionType();
        regionType.setId(id);
        regionType.setName(name.trim());

        if (regionTypeDAO.updateRegionType(regionType) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Regiao nao encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/regioes");
    }

    //remove a regiao pelo id
    private void deleteRegionType(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(getId(request));

        if (regionTypeDAO.deleteRegionType(id) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Regiao nao encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/regioes");
    }

    //aceita o nome do campo do jsp novo e do formulario antigo
    private String getName(HttpServletRequest request) {
        String name = request.getParameter("nome");
        if (name == null) name = request.getParameter("regionTypeName");
        return name;
    }

    //pega o id enviado pelo formulario
    private String getId(HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id == null) id = request.getParameter("regionTypeId");
        return id;
    }

    //confere se o nome foi preenchido e cabe no campo
    private boolean validName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 100;
    }

    //confere o id antes de consultar o db
    private int parseId(String value) {
        int id = Integer.parseInt(value);
        if (id <= 0) throw new NumberFormatException("ID invalido");
        return id;
    }
}
