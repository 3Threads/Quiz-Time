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
        if(!SessionRemove.checkUser(httpServletRequest,httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        Quiz currQuiz =((QuizzesDAO) getServletContext().getAttribute("quizzesDB")).getQuizInfo(Integer.parseInt(httpServletRequest.getParameter("quizId")));
        User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
        if(httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("delete")) {
            if(myUser.isAdmin() || myUser.getId() == currQuiz.getCreatorID()) {
                ((QuizzesDAO) getServletContext().getAttribute("quizzesDB")).deleteQuiz(Integer.parseInt(httpServletRequest.getParameter("quizId")));
                httpServletResponse.sendRedirect("/homePage");
                return;
            }
        }
        if (httpServletRequest.getParameter("quizId") == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if(currQuiz == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("sendChallenge")) {
            int friendId = Integer.parseInt(httpServletRequest.getParameter("friendId"));
            ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
            challengesDAO.sendChallenge(myUser.getId(), friendId, quizId);
            httpServletResponse.sendRedirect("/quiz?quizId=" + quizId);
            return;
        }
        httpServletRequest.getRequestDispatcher("quiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}
