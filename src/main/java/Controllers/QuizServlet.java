package Controllers;

import BusinessLogic.RankingSystem;
import BusinessLogic.SessionRemove;
import DAO.ChallengesDAO;
import DAO.QuizzesDAO;
import DAO.RatingsDAO;
import DAO.UsersDAO;
import Types.Quiz;
import Types.Rating;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "quiz", value = "/quiz")
public class QuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        int quizId;
        try {
            quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        Quiz currQuiz = ((QuizzesDAO) getServletContext().getAttribute("quizzesDB")).getQuizInfo(quizId);
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("delete")) {
            if (myUser.isAdmin() || myUser.getId() == currQuiz.getCreatorID()) {
                ((QuizzesDAO) getServletContext().getAttribute("quizzesDB")).deleteQuiz(quizId);
                httpServletResponse.sendRedirect("/homePage");
                return;
            }
        }

        if (currQuiz == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("sendChallenge")) {
            int friendId;
            try {
                friendId = Integer.parseInt(httpServletRequest.getParameter("friendId"));
            } catch (NumberFormatException e) {
                httpServletResponse.sendRedirect("/homePage");
                return;
            }
            ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");
            challengesDAO.sendChallenge(myUser.getId(), friendId, quizId);
            httpServletResponse.sendRedirect("/quiz?quizId=" + quizId);
            return;
        }
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("changeList")) {
            RatingsDAO ratingsDAO = (RatingsDAO) httpServletRequest.getServletContext().getAttribute("ratingsDB");
            ArrayList<Rating> rates = ratingsDAO.getQuizRatings(Integer.parseInt(httpServletRequest.getParameter("quizId")), httpServletRequest.getParameter("order"));
            UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");

            StringBuilder str = new StringBuilder();
            if (ratingsDAO.haveAlreadyRated(myUser.getId(), currQuiz.getQuizId())) {
                for (int i = 0; i < rates.size(); i++) {
                    if (rates.get(i).getUserId() == myUser.getId()) {
                        Rating rr = rates.get(i);
                        rates.remove(i);
                        rates.add(0, rr);
                    }
                }
            }
            for (Rating rate : rates) {
                str.append(rateConstructor(myUser, usersDAO.getUserById(rate.getUserId()), rate));
            }
            httpServletResponse.getWriter().println(str);
            return;
        }
        httpServletRequest.getRequestDispatcher("quiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    private String rateConstructor(User myUser, User user, Rating rate) {
        StringBuilder str = new StringBuilder();
        str.append("<div id='").append(user.getId()).append("'>\n");

        str.append("<div style=\"display: inline-block;\"> <a href=\"/profile?user=").append(user.getId()).append("\">\n<div  class=\"rank-").append(RankingSystem.countRank(user.getScore())).append("\" style=\"font-size:17px; display: inline-block;\">\n");
        str.append("@").append(user.getUsername()).append(" </div>\n </a>");
        str.append("<div style=\"font-size:17px; display: inline-block; \">\n");
        str.append("<span class=\"fa fa-star checked\"></span>\n".repeat(Math.max(0, rate.getRating())));
        str.append("<span class=\"fa fa-star\"></span>\n".repeat(Math.max(0, 5 - rate.getRating())));
        str.append("</div>\n");
        str.append("<div style=\"font-size:15px; display: inline-block; color: #aaa\">| ").append(rate.getRatingsDate()).append("</div>\n </div>\n");

        if (myUser.isAdmin() || user.getId() == myUser.getId()) {
            str.append("<a class=\"btn-danger\" style=\"float:right; color: #aaa\" onclick=\"deleteComment(").append(user.getId()).append(")\"> Delete comment</a>\n");
        }
        str.append("<div style=\"margin-bottom: 25px\">").append(rate.getComment()).append("</div>\n </div>\n");
        return str.toString();
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.getWriter().println("login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("addRate")) {
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
            int userId = Integer.parseInt(httpServletRequest.getParameter("userId"));
            int rate = Integer.parseInt(httpServletRequest.getParameter("rate"));
            String comment = httpServletRequest.getParameter("comment");
            RatingsDAO ratingsDAO = (RatingsDAO) httpServletRequest.getServletContext().getAttribute("ratingsDB");
            ratingsDAO.addRatingToQuiz(userId, quizId, rate, comment);
        }
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("deleteRate")) {
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
            int userId = Integer.parseInt(httpServletRequest.getParameter("userId"));
            RatingsDAO ratingsDAO = (RatingsDAO) httpServletRequest.getServletContext().getAttribute("ratingsDB");
            ratingsDAO.deleteRating(userId, quizId);
        }
    }

}
