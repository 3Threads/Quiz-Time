package Controllers;

import FunctionalClasses.FriendsConnect;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "friends", value = "/friends")
public class FriendsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        int user1 = Integer.parseInt(httpServletRequest.getParameter("user1"));
        int user2 = Integer.parseInt(httpServletRequest.getParameter("user2"));
        String action = httpServletRequest.getParameter("action");
        FriendsConnect fr = new FriendsConnect(false);
        if (action.equals("sendRequest")) {
            try {
                fr.sendFriendRequest(user1, user2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (action.equals("acceptRequest")) {
            try {
                fr.acceptRequest(user1, user2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (action.equals("cancelRequest")) {
            try {
                fr.rejectRequest(user2, user1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (action.equals("rejectRequest")) {
            try {
                fr.rejectRequest(user1, user2);
                String from = httpServletRequest.getParameter("from");
                if (from.equals("homepage")) {
                    httpServletResponse.sendRedirect("/homePage");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (action.equals("unfriend")) {
            try {
                fr.deleteFriend(user1, user2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        httpServletResponse.sendRedirect("/profile?user=" + user2);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    }
}
