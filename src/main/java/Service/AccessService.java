package Service;

import DAO.ProductDAO;
import Model.LoggedUser;
import Model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

//scope rules, admin can do evertything, manager can only see it from its region
public final class AccessService {

    private static final ProductDAO productDAO = new ProductDAO();

    private AccessService() {}

    //loggedUser from session, fails if user = null
    public static LoggedUser currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        LoggedUser user = session == null ? null : (LoggedUser) session.getAttribute("loggedUser");
        if (user == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return user;
    }

    public static boolean isAdmin(LoggedUser user) {
        return "ADMIN".equals(user.getType());
    }

    //null = no filter, manager withou region gets -1, doesn't match with anything
    public static Integer scope(LoggedUser user) {
        if (isAdmin(user)) return null;
        return user.getRegionTypeId() == null ? Integer.valueOf(-1) : user.getRegionTypeId();
    }

    public static boolean canAccessRegion(LoggedUser user, Integer regionTypeId) {
        if (isAdmin(user)) return true;
        return regionTypeId != null && regionTypeId.equals(user.getRegionTypeId());
    }

    // Produto existe E pertence à região do usuário (via loja).
    public static boolean canAccessProduct(LoggedUser user, int productId) throws SQLException {
        Product product = productDAO.searchById(productId);
        return product != null && canAccessRegion(user, product.getRegionTypeId());
    }
}
