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
        if(!SessionRemove.checkUser(httpServletRequest,httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            int profileId = Integer.parseInt(httpServletRequest.getParameter("user"));
            User pageUser = ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB")).getUserById(profileId);
            if (pageUser == null) {
                httpServletResponse.sendRedirect("/homePage");
                return;
            }
            httpServletRequest.getRequestDispatcher("profile.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if(!SessionRemove.checkUser(httpServletRequest,httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        if (httpServletRequest.getParameter("action") != null) {
            String action = httpServletRequest.getParameter("action");
            UsersDAO usersDAO = ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB"));
            int userId = Integer.parseInt(httpServletRequest.getParameter("userId"));
            int myUserId = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getId();
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            if ((myUser.isAdmin() || myUserId == userId) && action.equals("deleteProfile")) {
                usersDAO.deleteUser(userId);
                if (myUserId == userId) {
                    httpServletRequest.getSession().removeAttribute("userInfo");
                    httpServletResponse.sendRedirect("/login");
                } else {
                    httpServletResponse.sendRedirect("/homePage");
                }
                return;
            }
            if (myUser.isAdmin()) {
                if (action.equals("deleteAdmin")) {
                    usersDAO.deleteAdminToUser(Integer.parseInt(httpServletRequest.getParameter("userId")));
                }
                if (action.equals("addToAdmin")) {
                    usersDAO.getAdminToUser(Integer.parseInt(httpServletRequest.getParameter("userId")));
                }
            }
        }
        httpServletResponse.sendRedirect("/profile?user=" + Integer.parseInt(httpServletRequest.getParameter("userId")));
    }
}
