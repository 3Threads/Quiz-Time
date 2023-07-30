package Controllers;

import BusinessLogic.SessionRemove;
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
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "writeQuiz", value = "/writeQuiz")
public class WriteQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        httpServletRequest.getSession().removeAttribute("title");
        httpServletRequest.getSession().removeAttribute("description");
        httpServletRequest.getSession().removeAttribute("questions");

        int quizId;
        try {
            quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }

        int questionInd;
        try {
            questionInd = Integer.parseInt(httpServletRequest.getParameter("questionInd"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=0");
            return;
        }

        QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
        if (questionsDAO.getQuestionsIdByQuizId(quizId).size() == 0) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }

        if (questionIndIsOutOfBound(quizId, questionInd, httpServletRequest)) {
            httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=0");
            return;
        }
        if (httpServletRequest.getSession().getAttribute("userAnswers") == null) {
            int size = ((QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB")).getQuestions(quizId).size();
            ArrayList<String>[] answers = new ArrayList[size];
            httpServletRequest.getSession().setAttribute("userAnswers", answers);
        }
        if (httpServletRequest.getSession().getAttribute("startTime") == null) {
            httpServletRequest.getSession().setAttribute("startTime", System.currentTimeMillis());
            httpServletRequest.getSession().setAttribute("endTime", System.currentTimeMillis() + 10000); //aq aris shesacvleli time
        }
        httpServletRequest.getRequestDispatcher("writeQuiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    private boolean questionIndIsOutOfBound(int quizId, int questionInd, HttpServletRequest httpServletRequest) {
        QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
        return !(questionsDAO.getQuestionsIdByQuizId(quizId).size() > questionInd && questionInd >= 0);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        int quizId;
        try {
            quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
        if (httpServletRequest.getParameter("action").equals("questionAnswer")) {
            String[] answers = httpServletRequest.getParameterValues("answer");
            int questionInd;
            try {
                questionInd = Integer.parseInt(httpServletRequest.getParameter("questionInd"));
            } catch (NumberFormatException e) {
                httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=0");
                return;
            }

            int nextQuestionInd;
            try {
                nextQuestionInd = Integer.parseInt(httpServletRequest.getParameter("nextQuestionInd"));
            } catch (NumberFormatException e) {
                httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=0");
                return;
            }
            if (answers != null && httpServletRequest.getSession().getAttribute("userAnswers") != null) {
                ((ArrayList<String>[]) httpServletRequest.getSession().getAttribute("userAnswers"))[questionInd] = new ArrayList<>(List.of(answers));
            }
            httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=" + nextQuestionInd);
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
            httpServletRequest.getSession().removeAttribute("endTime");
            ResultsDAO resultsDAO = (ResultsDAO) httpServletRequest.getServletContext().getAttribute("resultsDB");
            int userId = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getId();

            resultsDAO.addResult(userId, quizId, score, time);
            httpServletResponse.sendRedirect("/quiz?quizId=" + quizId + "&score=" + score + "&time=" + time);
        }
    }
}
