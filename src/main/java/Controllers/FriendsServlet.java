package Controllers;

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
        int user1 = Integer.parseInt(httpServletRequest.getParameter("user1"));
        int user2 = Integer.parseInt(httpServletRequest.getParameter("user2"));
        String action = httpServletRequest.getParameter("action");
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
            String from = httpServletRequest.getParameter("from");
            if (from.equals("homepage")) {
                httpServletResponse.sendRedirect("/homePage");
                return;
            }
        }
        if (action.equals("unfriend")) {
            fr.deleteFriend(user1, user2);
        }
        httpServletResponse.sendRedirect("/profile?user=" + user2);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        int user1 = Integer.parseInt(httpServletRequest.getParameter("user1"));
        int user2 = Integer.parseInt(httpServletRequest.getParameter("user2"));
        String action = httpServletRequest.getParameter("action");
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
    }
}
