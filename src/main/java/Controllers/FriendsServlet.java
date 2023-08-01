package Controllers;

import BusinessLogic.SessionRemove;
import DAO.FriendsDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "friends", value = "/friends")
public class FriendsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        int user1;
        int user2;
        try {
            user1 = Integer.parseInt(httpServletRequest.getParameter("user1"));
            user2 = Integer.parseInt(httpServletRequest.getParameter("user2"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        String action = httpServletRequest.getParameter("action");
        if (action == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        FriendsDAO fr = (FriendsDAO) httpServletRequest.getServletContext().getAttribute("friendsDB");
        if (action.equals("sendRequest")) {
            fr.sendFriendRequest(user1, user2);
        }
        if (action.equals("acceptRequest")) {
            fr.acceptRequest(user1, user2);
        }
        if (action.equals("cancelRequest")) {
            fr.rejectRequest(user2, user1);
        }
        if (action.equals("rejectRequest")) {
            fr.rejectRequest(user1, user2);
        }
        if (action.equals("unfriend")) {
            fr.deleteFriend(user1, user2);
        }
        httpServletResponse.sendRedirect("/profile?user=" + user2);
    }
}
