package Controllers;

import DAO.QuestionsDAO;
import DAO.QuizzesDAO;
import Types.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "createQuiz", value = "/createQuiz")
public class CreateQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.getRequestDispatcher("createQuiz.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        ArrayList<Question> questions = getQuestionsFromSession(httpServletRequest);

        if (httpServletRequest.getParameter("title") != null) {
            httpServletRequest.getSession().setAttribute("title", httpServletRequest.getParameter("title"));
        }

        if (httpServletRequest.getParameter("description") != null) {
            httpServletRequest.getSession().setAttribute("description", httpServletRequest.getParameter("description"));
        }

        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("addQuestion")) {
            if (httpServletRequest.getParameter("questionType").equals("questionResponse")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("answer");
                Question question = new QuestionResponse(questionText, "questionResponse", new ArrayList<>(List.of(answers)));
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("fillInTheBlank")) {
                String questionText = httpServletRequest.getParameter("questionText1");
                String questionText2 = httpServletRequest.getParameter("questionText2");
                String[] answers = httpServletRequest.getParameterValues("answer");
                Question question = new FillInTheBlank(questionText, questionText2, "fillInTheBlank", new ArrayList<>(List.of(answers)));
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("pictureResponse")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String url = httpServletRequest.getParameter("questionImage");
                String[] answers = httpServletRequest.getParameterValues("answer");
                Question question = new PictureResponse(questionText, "pictureResponse", url, new ArrayList<>(List.of(answers)));
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("multiAnswer")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("answer");
                Question question = new MultiAnswer(questionText, "multiAnswer", new ArrayList<>(List.of(answers)));
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("matching")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] questionText1 = httpServletRequest.getParameterValues("questionText1");
                String[] questionText2 = httpServletRequest.getParameterValues("questionText2");
                HashMap<String, String> answers = new HashMap<>();
                for (int i = 0; i < questionText1.length; i++) {
                    answers.put(questionText1[i], questionText2[i]);
                }
                Question question = new Matching(questionText, "matching", answers);
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("multipleChoice") || httpServletRequest.getParameter("questionType").equals("multipleChoiceWithMultipleAnswers")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("choosedIndex");
                String[] answerTexts = httpServletRequest.getParameterValues("answerText");

                ArrayList<String> allAnswers = new ArrayList<>(List.of(answerTexts));
                ArrayList<String> answersList = new ArrayList<>();
                for (String answer : answers) {
                    answersList.add(answerTexts[Integer.parseInt(answer)]);
                }

                Question question = new MultipleChoice(questionText, httpServletRequest.getParameter("questionType"), answersList, allAnswers);
                questions.add(question);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            httpServletResponse.sendRedirect("/createQuiz");
        }

        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("createQuiz")) {
            String title = httpServletRequest.getParameter("title");
            String description = httpServletRequest.getParameter("description");
            User user = (User) httpServletRequest.getSession().getAttribute("userInfo");
            QuizzesDAO qzDAO = (QuizzesDAO) httpServletRequest.getServletContext().getAttribute("quizzesDB");
            qzDAO.addQuiz(title, description, user.getId());
            Quiz quiz = qzDAO.getQuizByName(title);
            int quizID = quiz.getQuizId();

            QuestionsDAO questionsDAO = (QuestionsDAO) httpServletRequest.getServletContext().getAttribute("questionsDB");
            for (Question q : questions) {
                questionsDAO.addQuestion(q, quizID);
            }
        }
    }

    public static ArrayList<Question> getQuestionsFromSession(HttpServletRequest httpServletRequest) {
        ArrayList<Question> questions = (ArrayList<Question>) httpServletRequest.getSession().getAttribute("questions");
        if (questions == null) {
            questions = new ArrayList<>();
        }
        return questions;
    }
}
