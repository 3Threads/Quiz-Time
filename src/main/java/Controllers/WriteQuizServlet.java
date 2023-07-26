package Controllers;

import DAO.QuestionsDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "writeQuiz", value = "/writeQuiz")
public class WriteQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        if (httpServletRequest.getParameter("quizId") == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("questionInd") == null || questionIndIsOutOfBound(Integer.parseInt(httpServletRequest.getParameter("questionInd")), httpServletRequest)) {
            httpServletResponse.sendRedirect("/writeQuiz?quizId="+httpServletRequest.getParameter("quizId")+"&questionInd=0");
            return;
        }

        httpServletRequest.getRequestDispatcher("writeQuiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    private boolean questionIndIsOutOfBound(int questionInd, HttpServletRequest httpServletRequest) {
        QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
        return !(questionsDAO.getQuestionsIdByQuizId(Integer.parseInt(httpServletRequest.getParameter("quizId"))).size() > questionInd && questionInd >= 0);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}
