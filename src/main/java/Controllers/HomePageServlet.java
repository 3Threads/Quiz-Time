package Controllers;

import BusinessLogic.SessionRemove;
import DAO.AnnouncementsDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "homePage", value = "/homePage")
public class HomePageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        httpServletRequest.getRequestDispatcher("homePage.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String title = httpServletRequest.getParameter("title");
        String description = httpServletRequest.getParameter("description");
        User user = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if (title == null || description == null || title.trim().equals("") || description.trim().equals("") || user == null || !user.isAdmin()) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        AnnouncementsDAO announcementsDAO = (AnnouncementsDAO) httpServletRequest.getServletContext().getAttribute("announcementsDB");
        announcementsDAO.addAnnouncement(title, description, user.getId());
        httpServletResponse.sendRedirect("/homePage");

    }
}
