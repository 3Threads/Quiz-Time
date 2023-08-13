package Controllers;

import BusinessLogic.RankingSystem;
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

@WebServlet(name = "getChats", value = "/getChats")
public class DynamicChatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.getWriter().println("login");
            return;
        }
        PrintWriter out = httpServletResponse.getWriter();
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        ArrayList<Integer> interactors = messagesDAO.getInteractorsList(myUser.getId());
        String chatWith = httpServletRequest.getParameter("chatWith");
        if (!chatWith.equals("")) {
            int chatId = Integer.parseInt(chatWith);
            User myFriend = usersDAO.getUserById(chatId);
            if (!interactors.contains(myFriend.getId())) {
                out.println(myFriend.getId() + "/" + RankingSystem.countRank(myFriend.getRank()) + "/" + myFriend.getUsername());
                if (interactors.size() != 1) out.println("$");
            }
        }
        for (int i = 0; i < interactors.size(); i++) {
            User myFriend = usersDAO.getUserById(interactors.get(i));
            if (i != interactors.size() - 1) {
                out.println(myFriend.getId() + "/" + RankingSystem.countRank(myFriend.getRank()) + "/" + myFriend.getUsername() + "$");
            } else out.println(myFriend.getId() + "/" + RankingSystem.countRank(myFriend.getRank()) + "/" + myFriend.getUsername());
        }
    }
}
