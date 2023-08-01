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
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        int profileId;
        try {
            profileId = Integer.parseInt(httpServletRequest.getParameter("user"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        User pageUser = ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB")).getUserById(profileId);
        if (pageUser == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        httpServletRequest.getRequestDispatcher("profile.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        String action = httpServletRequest.getParameter("action");
        int userId;
        try {
            userId = Integer.parseInt(httpServletRequest.getParameter("userId"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (action != null) {
            UsersDAO usersDAO = ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB"));
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            int myUserId = myUser.getId();
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
                    usersDAO.deleteAdminToUser(userId);
                }
                if (action.equals("addToAdmin")) {
                    usersDAO.getAdminToUser(userId);
                }
            }
        }
        httpServletResponse.sendRedirect("/profile?user=" + userId);
    }
}
