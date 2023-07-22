package Controllers;


import DAO.MessagesDAO;
import Types.User;
import com.mysql.cj.xdevapi.JsonArray;
import netscape.javascript.JSObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@WebServlet(name = "notSeen", value = "/notSeen")
public class notSeenMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if(myUser == null) return;
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        if(httpServletRequest.getParameter("chatWith") == "") return;
        for(int us : notSeen.keySet()) {
            if(us == Integer.parseInt(httpServletRequest.getParameter("chatWith"))) {
                messagesDAO.setMessagesSeen(myUser.getId(), us);
                ArrayList<String> messages = notSeen.get(us);
                for (String msg : messages) {
                    out.println("\n<div class=\"uk-align-left messageBox\" style=\"background-color: #3e4042;\">\n" +
                            "                        <p class=\"messageParagraph\">" + msg + "</p> </div>");
                }
            }
        }
    }
}
