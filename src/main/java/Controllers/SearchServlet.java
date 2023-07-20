package Controllers;

import FunctionalClasses.UserConnect;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "search", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            String searchUserName = httpServletRequest.getParameter("search");
            UserConnect usConnect = (UserConnect) httpServletRequest.getServletContext().getAttribute("usersDB");
            int searchUserId;
            searchUserId = usConnect.getUserId(searchUserName);
            if (searchUserId != 0) {
                httpServletResponse.sendRedirect("/profile?user=" + searchUserId);
            } else httpServletResponse.sendRedirect("/homePage");
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}
