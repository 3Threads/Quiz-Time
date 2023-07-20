package Controllers;

import DAO.UsersDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") != null) {
            httpServletResponse.sendRedirect("/homePage");
        } else {
            httpServletRequest.getRequestDispatcher("index.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        UsersDAO sql = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        try {
            if (sql.checkUser(username, password)) {
                int userId = sql.getUserId(username);
                User user = new User(userId, username);
                httpServletRequest.getSession().setAttribute("userInfo", user);
                httpServletResponse.sendRedirect("/homePage");
            } else {
                httpServletResponse.sendRedirect("/login?loginFailed=true");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
