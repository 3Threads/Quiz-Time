package log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("index.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        UserConnect sql = (UserConnect) httpServletRequest.getServletContext().getAttribute("usersDB");
        try {
            if (sql.checkUser(email, password)) {
                httpServletResponse.sendRedirect("/homePage");
            } else {
                httpServletResponse.sendRedirect("/login");
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
