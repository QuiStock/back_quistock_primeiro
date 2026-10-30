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

    // Public routes and routes reserved for admins.
    private static final Set<String> general = Set.of("/", "/index.jsp", "/login", "/logout");
    private static final Set<String> only_admin = Set.of(
            "/home-admin.jsp", "/admins", "/crud-admin.jsp",
            "/regionalManagers", "/regional-managers.jsp", "/crud-managers.jsp",
            "/regionTypes", "/regioes", "/stores");

    @Override
    // Handles do, Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path  = req.getServletPath();

        // Static assets must load before login too.
        if (path.startsWith("/css/") || path.startsWith("/assets/") || path.startsWith("/script/")) {
            filter.doFilter(request, response);
            return;
        }

        if (general.contains(path)) {
            filter.doFilter(request, response);
            return;
        }

        // Do not create a session just to check access.
        HttpSession session = req.getSession(false);
        LoggedUser user = session == null ? null : (LoggedUser) session.getAttribute("loggedUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Block admin routes for other users.
        if (only_admin.contains(path) && !"ADMIN".equals(user.getType())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Página não permitida");
            return;
        }

        filter.doFilter(request, response);
    }
}
