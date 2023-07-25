package Controllers;

import DAO.ChallengesDAO;
import DAO.FriendsDAO;
import DAO.QuizzesDAO;
import Types.Challenge;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Types.User;

import java.io.IOException;

@WebServlet(name = "challenges", value = "/challenges")

public class ChallengeServlet extends HttpServlet {

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
