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
@WebServlet(name = "notSeenChat", value = "/notSeenChat")
public class notSeenMessagesNotChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if(myUser == null) return;
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        for(int us : notSeen.keySet()) {
            if(httpServletRequest.getParameter("chatWith") == "" ||
                        us != Integer.parseInt(httpServletRequest.getParameter("chatWith"))) {
                ArrayList<String> messages = notSeen.get(us);
                System.out.println(us+"$"+messages.size());
                out.println(us+"$"+messages.size()+"/");
            }
        }
    }
}
