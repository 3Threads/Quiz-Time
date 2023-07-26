package Controllers;

import BusinessLogic.SessionRemove;
import DAO.ChallengesDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet(name = "quiz", value = "/quiz")
public class QuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        if (httpServletRequest.getParameter("quizId") == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("sendChallenge")) {
            int friendId = Integer.parseInt(httpServletRequest.getParameter("friendId"));
            ChallengesDAO challengesDAO = (ChallengesDAO) httpServletRequest.getServletContext().getAttribute("challengesDB");
            int myId = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getId();
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
            challengesDAO.sendChallenge(myId, friendId, quizId);
            httpServletResponse.sendRedirect("/quiz?quizId=" + quizId);
            return;
        }
        httpServletRequest.getRequestDispatcher("quiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}
