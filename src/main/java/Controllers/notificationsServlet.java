package Controllers;

import DAO.*;
import Types.Challenge;
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


@WebServlet(name = "notifications", value = "/notifications")
public class notificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if (myUser == null) return;
        QuizzesDAO quizzesDAO = (QuizzesDAO) httpServletRequest.getServletContext().getAttribute("quizzesDB");
        UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
        ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");
        ArrayList<Challenge> challenges = challengesDAO.getChallenges(myUser.getId());
        for (int i = 0; i < challenges.size(); i++) {
            Challenge challenge = challenges.get(i);
            User challUserInfo = usersDAO.getUserById(challenge.getUserId());
            if(i != challenges.size() - 1) {
                out.println(challUserInfo.getId()+"|"+challUserInfo.getUsername()+"|"+challenge.getQuizId()+"|"+quizzesDAO.getQuizInfo(challenge.getQuizId()).getQuizName() + "/");
            } else out.println(challUserInfo.getId()+"|"+challUserInfo.getUsername()+"|"+challenge.getQuizId()+"|"+quizzesDAO.getQuizInfo(challenge.getQuizId()).getQuizName());
        }
        out.println('$');
        MessagesDAO messagesDAO = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
        HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
        if(!notSeen.isEmpty()) {
            int x = 0;
            for (int id : notSeen.keySet()) {
                User chatUser = usersDAO.getUserById(id);
                if (x != notSeen.keySet().size()-1) {
                    out.println(chatUser.getId() + "|" + chatUser.getUsername() + "/");
                } else out.println(chatUser.getId() + "|" + chatUser.getUsername());
                x++;
            }
        }
        out.println('$');
        FriendsDAO friendsDAO = (FriendsDAO) httpServletRequest.getServletContext().getAttribute("friendsDB");
        ArrayList<Integer> requests = friendsDAO.getFriendsRequests(myUser.getId());
        for (int i = 0; i < requests.size(); i++) {
            User reqUserInfo = usersDAO.getUserById(requests.get(i));
            if(i != requests.size()-1) {
                out.println(reqUserInfo.getId() + "|" + reqUserInfo.getUsername() + "/");
            } else out.println(reqUserInfo.getId() + "|" + reqUserInfo.getUsername());
        }
    }
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if(httpServletRequest.getParameter("notification").equals("challenge")) {
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
        if(httpServletRequest.getParameter("notification").equals("request")) {
            int user1 = Integer.parseInt(httpServletRequest.getParameter("user1"));
            int user2 = Integer.parseInt(httpServletRequest.getParameter("user2"));
            String action = httpServletRequest.getParameter("action");
            FriendsDAO fr = (FriendsDAO) httpServletRequest.getServletContext().getAttribute("friendsDB");
            if (action.equals("sendRequest")) {
                fr.sendFriendRequest(user1, user2);
            }
            if (action.equals("acceptRequest")) {
                fr.acceptRequest(user1, user2);
            }
            if (action.equals("cancelRequest")) {
                fr.rejectRequest(user2, user1);
            }
            if (action.equals("rejectRequest")) {
                fr.rejectRequest(user1, user2);
            }
            if (action.equals("unfriend")) {
                fr.deleteFriend(user1, user2);
            }
        }
    }
}
