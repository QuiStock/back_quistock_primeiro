package Controller;

import Model.LoggedUser;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class UrlFilter implements Filter {

    //Allowed pages for each login
    private static final Set<String> general = Set.of("/", "/index.jsp", "/login", "/logout");
    private static final Set<String> only_admin = Set.of(
            "/home-admin.jsp", "/admins", "/crud-admin.jsp",
            "/regionalManagers", "/regional-managers.jsp", "/crud-managers.jsp",
            "/regionTypes", "/stores");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path  = req.getServletPath();

        //verify paths from general profiles
        if (general.contains(path)) {
            filter.doFilter(request, response);
            return;
        }

        //checks existent session, keeps null if null, otherwise, receives logged user
        HttpSession session = req.getSession(false);
        LoggedUser user = session == null ? null : (LoggedUser) session.getAttribute("user");

        //if user is null, sends back to login page
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        //if a non admin user tries to change url to an admin only page, it sends a forbidden error
        if (only_admin.contains(path) && !"ADMIN".equals(user.getType())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Página não permitida");
            return;
        }

        filter.doFilter(request, response);
    }
}
