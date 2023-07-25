package Controllers;

import DAO.FriendsDAO;
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

@WebServlet(name = "getRequests", value = "/getRequests")
public class RequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if(myUser == null) return;
        FriendsDAO friendsDAO = (FriendsDAO) httpServletRequest.getServletContext().getAttribute("friendsDB");
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        ArrayList<Integer> requests = friendsDAO.getFriendsRequests(myUser.getId());
        int requestId = 1;
        for (Integer reqId : requests) {
            User reqUserInfo = usersDAO.getUserById(reqId);
            out.println("<li>\n" +
                    "<div class=\"row\" id=\"request\"+ " + requestId +">\n" +
                    "   <div class=\"col d-flex align-items-center\">\n" +
                    "   <a href=\"/profile?user="+reqUserInfo.getId()+ "\">" +reqUserInfo.getUsername() + "\n" +
                    "   </a>\n" +
                    "\n" +
                    "   </div>\n" +
                    "   <div class=\"col-auto\">\n" +
                    "     <button onclick=\"requestAction("+myUser.getId()+","+reqUserInfo.getId()+", 'acceptRequest', " + requestId+")\" class=\"btn btn-success\">accept</button>\n" +
                    "     <button onclick=\"requestAction("+myUser.getId()+","+reqUserInfo.getId()+", 'rejectRequest', " +  requestId + ")\" class=\"btn btn-danger\">Reject</button>\n" +
                    "     </div>\n" +
                    "     </div>\n" +
                    "     </li>");
            requestId++;
        }
    }
}
