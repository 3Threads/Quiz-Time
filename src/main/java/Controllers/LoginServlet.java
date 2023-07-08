package Controllers;

import FunctionalClasses.User;
import FunctionalClasses.UserConnect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if ((boolean) httpServletRequest.getSession().getAttribute("isLoggedIn")) {
            httpServletResponse.sendRedirect("/homePage");
        } else {
            httpServletRequest.getRequestDispatcher("index.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        UserConnect sql = (UserConnect) httpServletRequest.getServletContext().getAttribute("usersDB");
        try {
            if (sql.checkUser(username, password)) {
                httpServletRequest.getSession().setAttribute("isLoggedIn", true);
                int userId = sql.getUserId(username);
                User user = new User(username, userId);
                httpServletRequest.getSession().setAttribute("userInfo", user);
                httpServletResponse.sendRedirect("/homePage");
            } else {
                httpServletResponse.sendRedirect("/login?loginFailed=true");
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
