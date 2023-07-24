package Controllers;

import DAO.MessagesDAO;
import DAO.UsersDAO;
import Types.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
@WebServlet(name = "getChats", value = "/getChats")
public class getChatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if (myUser == null) return;
        HashMap<Integer, ArrayList<String>> notSeenMessages = messagesDAO.getNotSeenMessage(myUser.getId());
        ArrayList<Integer> interactors = messagesDAO.getInteractorsList(myUser.getId());
        if (httpServletRequest.getParameter("chatWith") != "") {
            Integer chatId = Integer.parseInt(httpServletRequest.getParameter("chatWith"));
            User myFriend = usersDAO.getUserById(chatId);
            if (!interactors.contains(myFriend.getId())) {
                out.println("<div class=\"d-flex align-items-center\" style=\"background-color: #3e4042;\">\n" +
                        "                    <a class=\"fullWidthList\" href=\"/chat?chatWith=" + myFriend.getId() + "\">" + myFriend.getUsername() + "\n" +
                        "                    </a>\n" +
                        "                    <div id=\"friend" + myFriend.getId() + "\"></div>\n" +
                        "                </div>");
            }
        }
        for (Integer person : interactors) {
            User myFriend = usersDAO.getUserById(person);
            if (httpServletRequest.getParameter("chatWith") != ""
                    && myFriend.getId() == Integer.parseInt(httpServletRequest.getParameter("chatWith"))) {
                out.println("<li><div class=\"d-flex align-items-center\" style=\"background-color: #3e4042;\">\n" +
                        "                    <a class=\"fullWidthList\" href=\"/chat?chatWith=" + myFriend.getId() + "\">" + myFriend.getUsername() + "\n" +
                        "                   <div id=\"friend" + myFriend.getId() + "\"> </div>\n" +
                        "                    </a>\n" +
                        "                </div></li>");
            } else {
                out.println("<li><div class=\"d-flex align-items-center\">\n" +
                        "                    <a class=\"fullWidthList\" href=\"/chat?chatWith=" + myFriend.getId() + "\">" + myFriend.getUsername() + "\n" +
                        "                   <div id=\"friend" + myFriend.getId() + "\">");
                if (notSeenMessages.keySet().contains(myFriend.getId())) {
                    out.println(notSeenMessages.get(myFriend.getId()).size());
                }
                out.println("</div>\n" +
                        "                    </a>\n" +
                        "                </div><" +
                        "/li>");
            }
        }
    }
}
