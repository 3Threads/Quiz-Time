package Controllers;

import BusinessLogic.SessionRemove;
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
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") != null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        httpServletRequest.getRequestDispatcher("index.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        UsersDAO sql = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");

        if (sql.checkUser(username, password)) {
            int userId = sql.getUserId(username);
            User user = sql.getUserById(userId);
            httpServletRequest.getSession().setAttribute("userInfo", user);
            httpServletRequest.getSession().removeAttribute("questions");
            httpServletRequest.getSession().removeAttribute("title");
            httpServletRequest.getSession().removeAttribute("description");
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        httpServletResponse.sendRedirect("/login?loginFailed=true");
    }
}
