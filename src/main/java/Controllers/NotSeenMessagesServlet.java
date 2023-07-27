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
public class NotSeenMessagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        }
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        if(httpServletRequest.getParameter("action").equals("currentChat")) {
            if (httpServletRequest.getParameter("chatWith") == "") return;
            for (int us : notSeen.keySet()) {
                if (us == Integer.parseInt(httpServletRequest.getParameter("chatWith"))) {
                    messagesDAO.setMessagesSeen(myUser.getId(), us);
                    ArrayList<String> messages = notSeen.get(us);
                    for (String msg : messages) {
                        out.println("\n<div class=\"uk-align-left messageBox\" style=\"background-color: #3e4042;\">\n" +
                                "                        <p class=\"messageParagraph\">" + msg + "</p> </div>");
                    }
                }
            }
        }
        if(httpServletRequest.getParameter("action").equals("notCurrentChat")) {
            for(int us : notSeen.keySet()) {
                if(httpServletRequest.getParameter("chatWith") == "" ||
                        us != Integer.parseInt(httpServletRequest.getParameter("chatWith"))) {
                    ArrayList<String> messages = notSeen.get(us);
                    out.println(us+"$"+messages.size()+"/");
                }
            }
        }
    }
}
