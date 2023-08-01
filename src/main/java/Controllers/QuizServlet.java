package Controllers;

import BusinessLogic.SessionRemove;
import DAO.ChallengesDAO;
import DAO.QuizzesDAO;
import Types.Quiz;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


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
        httpServletRequest.getRequestDispatcher("quiz.jsp").forward(httpServletRequest, httpServletResponse);
    }
}
