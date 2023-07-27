package Controllers;

import DAO.QuestionsDAO;
import DAO.ResultsDAO;
import Types.Question;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "writeQuiz", value = "/writeQuiz")
public class WriteQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getSession().removeAttribute("title");
        httpServletRequest.getSession().removeAttribute("description");
        httpServletRequest.getSession().removeAttribute("questions");
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        if (httpServletRequest.getParameter("quizId") == null) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("questionInd") == null || questionIndIsOutOfBound(Integer.parseInt(httpServletRequest.getParameter("questionInd")), httpServletRequest)) {
            httpServletResponse.sendRedirect("/writeQuiz?quizId=" + httpServletRequest.getParameter("quizId") + "&questionInd=0");
            return;
        }
        if (httpServletRequest.getSession().getAttribute("userAnswers") == null) {
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
            int size = ((QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB")).getQuestions(quizId).size();
            ArrayList<String>[] answers = new ArrayList[size];
            httpServletRequest.getSession().setAttribute("userAnswers", answers);
        }
        if (httpServletRequest.getParameter("startTime") == null)
            httpServletRequest.getSession().setAttribute("startTime", System.currentTimeMillis());
        httpServletRequest.getRequestDispatcher("writeQuiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    private boolean questionIndIsOutOfBound(int questionInd, HttpServletRequest httpServletRequest) {
        QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
        return !(questionsDAO.getQuestionsIdByQuizId(Integer.parseInt(httpServletRequest.getParameter("quizId"))).size() > questionInd && questionInd >= 0);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (httpServletRequest.getParameter("action").equals("questionAnswer")) {
            String[] answers = httpServletRequest.getParameterValues("answer");
            int questionInd = Integer.parseInt(httpServletRequest.getParameter("questionInd"));
            if (answers != null && httpServletRequest.getSession().getAttribute("userAnswers") != null) {
                ((ArrayList<String>[]) httpServletRequest.getSession().getAttribute("userAnswers"))[questionInd] = new ArrayList<>(List.of(answers));
            }
            httpServletResponse.sendRedirect("/writeQuiz?quizId=" + httpServletRequest.getParameter("quizId") + "&questionInd=" + (httpServletRequest.getParameter("nextQuestionInd")));
            return;
        }
        if (httpServletRequest.getParameter("action").equals("finish")) {
            long time = System.currentTimeMillis() - (long) httpServletRequest.getSession().getAttribute("startTime");
            int score = 0;
            ArrayList<String>[] userAnswers = (ArrayList<String>[]) httpServletRequest.getSession().getAttribute("userAnswers");
            ArrayList<Question> questions = (ArrayList<Question>) httpServletRequest.getSession().getAttribute("writingQuestions");
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                if (question.checkAnswer(userAnswers[i])) score++;
            }
            httpServletRequest.getSession().removeAttribute("writingQuestions");
            httpServletRequest.getSession().removeAttribute("userAnswers");
            httpServletRequest.getSession().removeAttribute("startTime");
            ResultsDAO resultsDAO = (ResultsDAO) httpServletRequest.getServletContext().getAttribute("resultsDB");
            int userId = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getId();
            int quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));

            resultsDAO.addResult(userId, quizId, score, new Time(time));
            httpServletResponse.sendRedirect("/quiz?quizId=" + quizId + "&score=" + score + "&time=" + time);
        }
    }
}