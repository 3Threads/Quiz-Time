package Controllers;

import DAO.ChallengesDAO;
import DAO.FriendsDAO;
import DAO.QuizzesDAO;
import DAO.UsersDAO;
import Types.Challenge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Types.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "challenges", value = "/challenges")

public class ChallengeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if (myUser == null) return;
        QuizzesDAO quizzesDAO = (QuizzesDAO) httpServletRequest.getServletContext().getAttribute("quizzesDB");
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");
        ArrayList<Challenge> challenges = challengesDAO.getChallenges(myUser.getId());
        int challId = 1;
        for (Challenge challenge : challenges) {
            User challUserInfo = usersDAO.getUserById(challenge.getUserId());
            out.println("<li>\n" +
                    "\n" +
                    " <div class=\"row\" id=\"challenge\"" + challId+"\">\n" +
                    "   <div class=\"col d-flex align-items-center\">\n" +
                    "   <a href=\"/profile?user=" + challUserInfo.getId()+"\">"+ challUserInfo.getUsername()   +"\n" +
                    "    </a>\n" +
                    "     <div style=\"margin-left: 3px\"> challenged you:</div>\n" +
                    "     <a style=\"margin-left: 3px\"\n" +
                    "     href=\"/quiz?quizId=" + challenge.getQuizId()+ "\">" + quizzesDAO.getQuizInfo(challenge.getQuizId()).getQuizName()+ "\n" +
                    "            </a>\n" +
                    "\n" +
                    "       </div>\n" +
                    "       <div class=\"col-auto\">\n" +
                    "          <button onclick=\"challengeAction("+challUserInfo.getId()+", 'acceptChallenge',"+ challId+ "," + challenge.getQuizId()+")\"\n" +
                    "               class=\"btn btn-success\">Accept\n" +
                    "          </button>\n" +
                    "            <button onclick=\"challengeAction("+challUserInfo.getId()+", 'rejectChallenge', "+ challId+ ", " + challenge.getQuizId()+")\"\n" +
                    "               class=\"btn btn-danger\">Reject\n" +
                    "               </button>\n" +
                    "         </div>\n" +
                    "       </div>\n" +
                    "\n" +
                    " </li>");
            challId++;
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        int userID = Integer.parseInt(httpServletRequest.getParameter("userID"));

        int quizID = Integer.parseInt(httpServletRequest.getParameter("quizID"));
        String action = httpServletRequest.getParameter("action");
        ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");

        if (action.equals("sendChallenge")) {
            challengesDAO.sendChallenge(myUser.getId(), userID, quizID);
        }
        if (action.equals("acceptChallenge")) {
            challengesDAO.removeChallenge(userID, myUser.getId(), quizID);
        }
        if (action.equals("rejectChallenge")) {
            challengesDAO.removeChallenge(userID, myUser.getId(), quizID);
        }
    }
}
