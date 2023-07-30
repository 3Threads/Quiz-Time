package Controllers;

import BusinessLogic.SessionRemove;
import DAO.UsersDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "search", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        String searchUserName = httpServletRequest.getParameter("search");
        if(searchUserName==null || searchUserName.trim().equals("")){
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        UsersDAO usConnect = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        int searchUserId;
        searchUserId = usConnect.getUserId(searchUserName);
        if (searchUserId != -1) {
            httpServletResponse.sendRedirect("/profile?user=" + searchUserId);
        } else httpServletResponse.sendRedirect("/homePage");

    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}
