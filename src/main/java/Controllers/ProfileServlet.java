package Controllers;

import BusinessLogic.SessionRemove;
import DAO.UsersDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet(name = "profile", value = "/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.getRequestDispatcher("profile.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if(((User) httpServletRequest.getSession().getAttribute("userInfo")).isAdmin()
                && httpServletRequest.getParameter("deleteUserId") != null) {
            UsersDAO usersDAO = ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB"));
            usersDAO.deleteUser(Integer.parseInt(httpServletRequest.getParameter("deleteUserId")));
            httpServletResponse.sendRedirect("/homePage");
        } else httpServletResponse.sendRedirect("/homePage");
    }
}
