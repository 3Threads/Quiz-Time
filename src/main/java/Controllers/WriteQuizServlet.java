package Controllers;

import BusinessLogic.RankingSystem;
import BusinessLogic.SessionRemove;
import DAO.QuestionsDAO;
import DAO.QuizzesDAO;
import DAO.ResultsDAO;
import DAO.UsersDAO;
import Types.Question;
import Types.Quiz;
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
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletRequest.getSession().removeAttribute("title");
        httpServletRequest.getSession().removeAttribute("description");
        httpServletRequest.getSession().removeAttribute("questions");
        httpServletRequest.getSession().removeAttribute("timeLimit");

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
        QuizzesDAO quizzesDAO = (QuizzesDAO) httpServletRequest.getServletContext().getAttribute("quizzesDB");
        Quiz quiz = quizzesDAO.getQuizInfo(quizId);
        if (questionsDAO.getQuestions(quizId).size() == 0) {
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
            long time = quiz.getTimeLimit().getHours() * 3600000 + quiz.getTimeLimit().getMinutes() * 60000 + quiz.getTimeLimit().getSeconds() * 1000;
            if (time != 0) {
                httpServletRequest.getSession().setAttribute("endTime", System.currentTimeMillis() + time);
            }
        }
        httpServletRequest.getRequestDispatcher("writeQuiz.jsp").forward(httpServletRequest, httpServletResponse);
    }

    private boolean questionIndIsOutOfBound(int quizId, int questionInd, HttpServletRequest httpServletRequest) {
        QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
        return !(questionsDAO.getQuestions(quizId).size() > questionInd && questionInd >= 0);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
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
            if(httpServletRequest.getParameter("finished") != null
                    && httpServletRequest.getParameter("finished").equals("false")) {
                httpServletResponse.sendRedirect("/writeQuiz?quizId=" + quizId + "&questionInd=" + nextQuestionInd);
            } else finish(httpServletRequest, httpServletResponse);
        }
    }
    private void finish(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        int quizId;
        try {
            quizId = Integer.parseInt(httpServletRequest.getParameter("quizId"));
        } catch (NumberFormatException e) {
            httpServletResponse.sendRedirect("/homePage");
            return;
        }
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
        QuizzesDAO quizzesDAO = (QuizzesDAO) httpServletRequest.getServletContext().getAttribute("quizzesDB");
        int userId = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getId();
        int oldScore = ((User) httpServletRequest.getSession().getAttribute("userInfo")).getScore();
        int newScore = RankingSystem.countNewScore(oldScore, 100 * score / questions.size());
        if (resultsDAO.getUserResultsOnQuiz(userId, quizId).size() != 0) {
            newScore = oldScore;
        } else quizzesDAO.completeQuiz(quizId);

        resultsDAO.addResult(userId, quizId, score, time);
        ((UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB")).updateScore(userId, newScore);
        httpServletResponse.sendRedirect("/quiz?quizId=" + quizId + "&score=" + score + "&time=" + time + "&plusScore=" + (newScore - oldScore) + "&oldScore=" + oldScore);
    }
}
