package Controllers;

import BusinessLogic.SessionRemove;
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
import java.util.Map;

@WebServlet(name = "createQuiz", value = "/createQuiz")
public class CreateQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if(!SessionRemove.checkUser(httpServletRequest,httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        httpServletRequest.getSession().removeAttribute("writingQuestions");
        httpServletRequest.getSession().removeAttribute("userAnswers");
        httpServletRequest.getSession().removeAttribute("startTime");
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("delete")) {
                ArrayList<Question> questions = getQuestionsFromSession(httpServletRequest);
                questions.remove(Integer.parseInt(httpServletRequest.getParameter("index")));
                httpServletRequest.getSession().setAttribute("questions", questions);
                httpServletResponse.sendRedirect("/createQuiz");
                return;
            }
            if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("edit")) {
                ArrayList<Question> questions = getQuestionsFromSession(httpServletRequest);
                int index = Integer.parseInt(httpServletRequest.getParameter("index"));
                Question q = questions.get(index);
                StringBuilder url = new StringBuilder("/createQuiz?index=" + index + "&editMode=true&type=" + q.getType());
                if (q.getType().equals(QuestionTypes.fillInTheBlank)) {
                    url.append("&questionText1=").append(q.getQuestionText()).append("&questionText2=").append(((QuestionFillInTheBlank) q).getQuestionText2());
                } else {
                    url.append("&questionText=").append(q.getQuestionText());
                }


                if (q.getType().equals(QuestionTypes.pictureResponse)) {
                    url.append("&imageUrl=").append(((QuestionPictureResponse) q).getPictureUrl());
                }
                if (q.getType().equals(QuestionTypes.multipleChoices)) {
                    for (int i = 0; i < q.getAnswers().size(); i++) {
                        String ans = q.getAnswers().get(i);
                        url.append("&correctAnswerText=").append(ans);
                    }
                    assert q instanceof QuestionMultipleChoices;
                    for (int i = 0; i < ((QuestionMultipleChoices) q).getIncorrectAnswers().size(); i++) {
                        String ans = ((QuestionMultipleChoices) q).getIncorrectAnswers().get(i);
                        url.append("&incorrectAnswerText=").append(ans);
                    }
                } else {
                    if (q.getType().equals(QuestionTypes.multipleChoicesWithMultipleAnswers)) {
                        for (int i = 0; i < q.getAnswers().size(); i++) {
                            String ans = q.getAnswers().get(i);
                            url.append("&correctAnswerText=").append(ans);
                        }
                        assert q instanceof QuestionMultipleChoicesWithMultipleAnswers;
                        for (int i = 0; i < ((QuestionMultipleChoicesWithMultipleAnswers) q).getIncorrectAnswers().size(); i++) {
                            String ans = ((QuestionMultipleChoicesWithMultipleAnswers) q).getIncorrectAnswers().get(i);
                            url.append("&incorrectAnswerText=").append(ans);
                        }
                    } else {
                        if (q.getType().equals(QuestionTypes.matching)) {
                            assert q instanceof QuestionMatching;
                            Map<String, String> pairs = ((QuestionMatching) q).getMatches();
                            for (String pr : pairs.keySet()) {
                                String ans = pairs.get(pr);
                                url.append("&key=").append(pr);
                                url.append("&value=").append(ans);
                            }

                        } else {
                            for (int i = 0; i < q.getAnswers().size(); i++) {
                                String ans = q.getAnswers().get(i);
                                url.append("&answerText=").append(ans);
                            }
                        }
                    }
                }
                httpServletResponse.sendRedirect(String.valueOf(url));
                return;
            }
            httpServletRequest.getRequestDispatcher("createQuiz.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws
            IOException {
        if(!SessionRemove.checkUser(httpServletRequest,httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        ArrayList<Question> questions = getQuestionsFromSession(httpServletRequest);
        Question question = null;
        if (httpServletRequest.getParameter("title") != null) {
            httpServletRequest.getSession().setAttribute("title", httpServletRequest.getParameter("title"));
        }

        if (httpServletRequest.getParameter("description") != null) {
            httpServletRequest.getSession().setAttribute("description", httpServletRequest.getParameter("description"));
        }

        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("addQuestion")) {
            if (httpServletRequest.getParameter("questionType").equals("textResponse")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("answer");
                question = new QuestionTextResponse(questionText, new ArrayList<>(List.of(answers)));
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("fillInTheBlank")) {
                String questionText = httpServletRequest.getParameter("questionText1");
                String questionText2 = httpServletRequest.getParameter("questionText2");
                String[] answers = httpServletRequest.getParameterValues("answer");
                question = new QuestionFillInTheBlank(questionText, questionText2, new ArrayList<>(List.of(answers)));
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("pictureResponse")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String url = httpServletRequest.getParameter("questionImage");
                String[] answers = httpServletRequest.getParameterValues("answer");
                question = new QuestionPictureResponse(questionText, url, new ArrayList<>(List.of(answers)));
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("multiAnswers")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("answer");
                question = new QuestionMultiAnswers(questionText, new ArrayList<>(List.of(answers)));
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
                question = new QuestionMatching(questionText, answers);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("multipleChoices")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("choosedIndex");
                String[] answerTexts = httpServletRequest.getParameterValues("answerText");

                ArrayList<String> allAnswers = new ArrayList<>(List.of(answerTexts));
                ArrayList<String> answersList = new ArrayList<>();
                for (String answer : answers) {
                    answersList.add(answerTexts[Integer.parseInt(answer)]);
                }
                question = new QuestionMultipleChoices(questionText, answersList, allAnswers);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("questionType").equals("multipleChoicesWithMultipleAnswers")) {
                String questionText = httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("choosedIndex");
                String[] answerTexts = httpServletRequest.getParameterValues("answerText");

                ArrayList<String> allAnswers = new ArrayList<>(List.of(answerTexts));
                ArrayList<String> answersList = new ArrayList<>();
                for (String answer : answers) {
                    answersList.add(answerTexts[Integer.parseInt(answer)]);
                }
                question = new QuestionMultipleChoicesWithMultipleAnswers(questionText, answersList, allAnswers);
                httpServletRequest.getSession().setAttribute("questions", questions);
            }

            if (httpServletRequest.getParameter("index") != null) {
                int ind = Integer.parseInt(httpServletRequest.getParameter("index"));
                questions.add(ind, question);
                questions.remove(ind + 1);
            } else questions.add(question);
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
            httpServletRequest.getSession().removeAttribute("title");
            httpServletRequest.getSession().removeAttribute("description");
            httpServletRequest.getSession().removeAttribute("questions");

            httpServletResponse.sendRedirect("/quiz?quizId=" + quizID);
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
