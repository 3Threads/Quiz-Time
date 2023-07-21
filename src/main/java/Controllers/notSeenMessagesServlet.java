package Controllers;


import DAO.MessagesDAO;
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
public class notSeenMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        int id = Integer.parseInt(httpServletRequest.getParameter("chatWith"));
        if (notSeen.containsKey(id)) {
            messagesDAO.setMessagesSeen(myUser.getId(), id);
            ArrayList<String> messages = notSeen.get(id);
            StringBuilder allMessagesHTML = new StringBuilder();
            for (String msg : messages) {
                allMessagesHTML.append("\n<div class=\"uk-align-left messageBox\" style=\"background-color: #3e4042;\">\n<p class=\"messageParagraph\">").append(msg).append("</p></div>");
            }
            out.println(allMessagesHTML);
        }
    }
}
