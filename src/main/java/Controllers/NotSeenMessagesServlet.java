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

@WebServlet(name = "notSeen", value = "/notSeen")
public class NotSeenMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.getWriter().println("login");
            return;
        }
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        String chatWith = httpServletRequest.getParameter("chatWith");
        if (httpServletRequest.getParameter("action").equals("currentChat")) {
            if (chatWith.equals("")) return;
            for (int us : notSeen.keySet()) {
                if (us == Integer.parseInt(chatWith)) {
                    messagesDAO.setMessagesSeen(myUser.getId(), us);
                    ArrayList<String> messages = notSeen.get(us);
                    for (String msg : messages) {
                        out.println("<div class='row justify-content-start' style='margin-left: 1px;'>" +
                                "                        <div class='messageBox' style='background-color: #3e4042;'>" +
                                "                            <p class='messageParagraph'>" + msg +
                                "                            </p>" +
                                "                        </div>" +
                                "                    </div>");
                    }
                }
            }
        }
        if (httpServletRequest.getParameter("action").equals("notCurrentChat")) {
            for (int us : notSeen.keySet()) {
                if (chatWith.equals("") ||
                        us != Integer.parseInt(chatWith)) {
                    ArrayList<String> messages = notSeen.get(us);
                    out.println(us + "$" + messages.size() + "$" + ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB")).getUserById(us).getUsername() + "/");
                }
            }
        }
    }
}
