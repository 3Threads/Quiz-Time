package Controllers;

import BusinessLogic.SessionRemove;
import DAO.UsersDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "createAccount", value = "/createAccount")
public class CreateAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") != null) {
            httpServletResponse.sendRedirect("/homePage");
        } else {
            httpServletRequest.getRequestDispatcher("register.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        UsersDAO sql = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        try {
            if (sql.addUser(username, password)) {
                httpServletResponse.sendRedirect("/login");
            } else {
                httpServletResponse.sendRedirect("/createAccount?userAlreadyExists=true");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
