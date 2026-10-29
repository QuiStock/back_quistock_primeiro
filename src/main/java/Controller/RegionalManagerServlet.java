package Controller;

import DAO.RegionTypeDAO;
import DAO.RegionalManagerDAO;
import Model.RegionalManager;
import Service.PasswordService;
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

    //doGet pra determinar o caminho baseado na action
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            //averigua qual action veio e encaminha pra tela correta
            if ("new".equals(action)){
                //carrega as regioes no formulario
                req.setAttribute("regionTypes", regionDAO.select());
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            //else if pra editar o gerente
            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                RegionalManager manager = dao.searchById(id);

                //caso o gerente venha vazio, retorna tela de erro
                if (manager == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Manager not found");
                    return;
                }

                //manda o gerente e as regioes pro formulario
                req.setAttribute("manager", manager);
                req.setAttribute("regionTypes", regionDAO.select());
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            //caso nao seja nenhum dos actions acima, retorna lista de gerentes
            else {
                req.setAttribute("managers", dao.select());
                req.getRequestDispatcher("/regional-managers.jsp").forward(req, resp);
            }

        } catch (SQLException ex) {

            resp.sendError(500, ex.getMessage()); //retorna mensagem caso de erro 500

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
                case "add":
                    add(req, resp);
                    break;
                case "edit":
                    update(req, resp);
                    break;
                case "delete":
                    delete(req, resp);
                    break;
                default:
                    doGet(req, resp);
                    break;
            }

        //retorna os erros
        } catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Inválido");
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //metodo pra add gerente regional
    private void add (HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException{
        RegionalManager manager = new RegionalManager();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        if(!validEmail(email) || !validPassword(password)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email ou Senha inválidos");
            return;
        }
        if(!validName(name)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome inválido!");
            return;
        }

        manager.setEmail(email.trim());
        manager.setName(name.trim());
        manager.setPassword(PasswordService.genHash(password.trim()));

        int regionTypeId = Integer.parseInt(req.getParameter("region-type"));

        if (regionDAO.searchById(regionTypeId) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Região inválida");
            return;
        }

        manager.setRegionTypeId(regionTypeId);

        dao.insert(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    //metodo pra atualizar gerente regional
    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException{

        int regionTypeId = Integer.parseInt(req.getParameter("region-type"));

        if (regionDAO.searchById(regionTypeId) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Região inválida");
            return;
        }
        int id = Integer.parseInt(req.getParameter("id"));

        RegionalManager manager = dao.searchById(id);



        //caso o gerente tenha vazio, retorna msg de erro
        if (manager == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin não encontrado");
            return;
        }

        //define as variaveis e faz a validação
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        if (!validEmail(email)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email inválido");
            return;
        }

        if (!validName(name)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome inválido!");
            return;
        }

        manager.setName(name.trim());
        manager.setEmail(email.trim());
        manager.setRegionTypeId(regionTypeId);

        if (password != null && !password.trim().isEmpty()) {
            if (!validPassword(password)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Senha inválida");
                return;
            }

            manager.setPassword(PasswordService.genHash(password.trim()));
        }

        dao.update(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    //metodo pra excluir gerente regional
    private void delete (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException,  IOException{

        int id = Integer.parseInt(req.getParameter("id"));
        dao.delete(id);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    //metodo regex que confere formato e tamanho do email e os caracteres pra validar
    private boolean validEmail(String email) {
        if (email == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9À-ÿ._%+-]+@[A-Za-z0-9À-ÿ-]+\\.[A-Za-z0-9À-ÿ.-]+$";

        return email.trim().length() <= 320 && email.trim().matches(regex);
    }

    //metodo regex que confere os caracteres pra validar a senha
    private boolean validPassword(String pass){
        if (pass == null){
            return false;
        }

        String regex = "^[a-zA-Z0-9._]{8,50}$";

        return pass.trim().matches(regex);
    }

    //metodo regex que confere os caracteres pra validar o nome
    private boolean validName(String name){

        if(name == null){
            return false;
        }

        String regex = "^[a-zA-ZÀ-ÿ]+(\s+[a-zA-ZÀ-ÿ]+)+$"; // apenas letras maiusculas e minusculas com/sem acento e espaço.

        return name.trim().length() <= 60 && name.trim().matches(regex); //valida tambem a quantidade maxima de caracteres

    }
}
