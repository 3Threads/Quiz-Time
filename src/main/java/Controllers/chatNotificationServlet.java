package Controllers;

import DAO.MessagesDAO;
import DAO.UsersDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "chatNotification", value = "/chatNotification")
public class chatNotificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if(myUser == null) return;
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        for(int id : notSeen.keySet()) {
            User chatUser = usersDAO.getUserById(id);
            out.println("<li>\n" +
                    "   <div class=\"row\">\n" +
                    "     <div class=\"col d-flex align-items-center\">\n" +
                    "        <div>New message from</div>\n" +
                    "           <a style=\"margin-left: 3px\" href=\"/profile?user=" + chatUser.getId() + "\">" +
                                      chatUser.getUsername() + "</a>\n" +
                    "          </div>\n" +
                    "            <div class=\"col-auto\">\n" +
                    "               <a href=\"/chat?chatWith="+chatUser.getId()+"\">" +
                    "                   <button class=\"btn btn-primary\">Open chat</button> </a>\n" +
                    "          </div>\n" +
                    "     </div>\n" +
                    "</li>");
        }
    }
}
