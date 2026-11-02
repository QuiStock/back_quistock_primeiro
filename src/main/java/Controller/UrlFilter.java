package Controller;

import Model.LoggedUser;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.MappingMatch;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebFilter("/*")
public class UrlFilter implements Filter {

    // Public routes (no login needed).
    private static final Set<String> general = Set.of("/", "/index.html", "/index.jsp", "/login.jsp", "/login", "/logout");

    //who can read (GET) and who can write (POST)
    private record Rule(Set<String> read, Set<String> write) {}

    //private routes, need login, each with its own rules
    private static final Set<String> ADMIN = Set.of("ADMIN");
    private static final Set<String> ALL   = Set.of("ADMIN", "MANAGER");

    //setting the access rules for each page
    //any other path will be blocked
    private static final Map<String, Rule> RULES = Map.ofEntries(
            Map.entry("/inicio",                new Rule(ALL,   ALL)),
            Map.entry("/admins",                new Rule(ADMIN, ADMIN)),
            Map.entry("/regionalManagers",      new Rule(ADMIN, ADMIN)),
            Map.entry("/regioes",               new Rule(ALL,   ADMIN)),
            Map.entry("/regionTypes",           new Rule(ALL,   ADMIN)),
            Map.entry("/stores",                new Rule(ALL,   ADMIN)),
            Map.entry("/products",              new Rule(ALL,   ALL)),
            Map.entry("/orders",                new Rule(ALL,   ALL)),
            Map.entry("/sales",                 new Rule(ALL,   ALL)),
            Map.entry("/home-admin.jsp",        new Rule(ADMIN, ADMIN)),
            Map.entry("/admins.jsp",            new Rule(ADMIN, ADMIN)),
            Map.entry("/crud-admin.jsp",        new Rule(ADMIN, ADMIN)),
            Map.entry("/regional-managers.jsp", new Rule(ADMIN, ADMIN)),
            Map.entry("/crud-managers.jsp",     new Rule(ADMIN, ADMIN)),
            Map.entry("/crud-product.jsp",      new Rule(ALL,   ALL)),
            Map.entry("/pedido.jsp",            new Rule(ALL,   ALL)),
            Map.entry("/promocao.jsp",          new Rule(ALL,   ALL))
    );

    //tokens generated for each session
    private static final String CSRF_ATTR  = "csrfToken";
    private static final String CSRF_PARAM = "_csrf";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    //filtering method
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path  = req.getServletPath();

        //static assets must load before login
        if (path.startsWith("/css/") || path.startsWith("/assets/") || path.startsWith("/landing-assets/") || path.startsWith("/script/")) {
            filter.doFilter(request, response);
            return;
        }

        if (general.contains(path)) {
            filter.doFilter(request, response);
            return;
        }

        //let unknown URL reach the 404 page even without a logged-in user
        if (req.getHttpServletMapping().getMappingMatch() == MappingMatch.DEFAULT
                && req.getServletContext().getResource(path) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //requires existent session to check
        HttpSession session = req.getSession(false);
        LoggedUser user = session == null ? null : (LoggedUser) session.getAttribute("loggedUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        //role checking
        Rule rule = RULES.get(path);
        String method = req.getMethod();
        boolean leitura = "GET".equals(method) || "HEAD".equals(method);

        //checks session, blocks access if tries entering a not allowed page
        if (rule == null || !(leitura ? rule.read() : rule.write()).contains(user.getType())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Página não permitida");
            return;
        }

        //creates the session token on first use and exposes it to the jsp
        String token = (String) session.getAttribute(CSRF_ATTR);
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(CSRF_ATTR, token);
        }
        req.setAttribute(CSRF_ATTR, token);

        //every write request needs to carry a crsf token
        if (!leitura) {
            req.setCharacterEncoding("UTF-8"); //must run before the first getParameter
            String enviado = req.getParameter(CSRF_PARAM);
            boolean ok = enviado != null && MessageDigest.isEqual(
                    token.getBytes(StandardCharsets.UTF_8),
                    enviado.getBytes(StandardCharsets.UTF_8));
            if (!ok) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Requisição inválida");
                return;
            }
        }

        filter.doFilter(request, response);
    }
}