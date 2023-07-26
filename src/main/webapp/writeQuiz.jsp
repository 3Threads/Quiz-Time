<%@ page import="Types.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.7.0.js"
            integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM=" crossorigin="anonymous"></script>

    <!-- UIkit CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/css/uikit.min.css"/>

    <!-- UIkit JS -->
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit-icons.min.js"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"/>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<body>
<%@include file="header.jsp" %>
<%
    Quiz currQuiz = quizzesDAO.getQuizInfo(Integer.parseInt(request.getParameter("quizId")));
    ArrayList<Question> questions = questionsDAO.getQuestions(currQuiz.getQuizId());
//    if (session.getAttribute("writingQuestions") == null) {
//        questions = questionsDAO.getQuestions(currQuiz.getQuizId());
//        session.setAttribute("writingQuestions", questions);
//    } else {
//        questions = (ArrayList<Question>) session.getAttribute("writingQuestions");
//    }
    int questionInd = Integer.parseInt(request.getParameter("questionInd"));
    Question currQuestion = questions.get(questionInd);
%>
<div class="container">
    <div class="row mt-3">
        <div class="col-4 uk-box-shadow-large uk-padding-small">
            <h1 style="margin: 0"><%=currQuiz.getQuizName()%>
            </h1>

        </div>
        <div class="col-1"></div>
        <div class="col-7 uk-box-shadow-large uk-padding-small">
            <h4>#<%=questionInd + 1%> Question</h4>
            <form action="/writeQuiz" method="post">
                <input type="hidden" name="quizId" value="<%=currQuiz.getQuizId()%>">
                <input type="hidden" name="questionInd" value="<%=questionInd%>">

                <% if (currQuestion.getType().equals("fillInTheBlank")) {
                %>
                <div class="uk-form-controls uk-form-controls-text row">
                    <div class="col-auto d-flex align-Items-center"><%=currQuestion.getQuestionText()%>
                    </div>
                    <div class="col-2">
                        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                               aria-label='Input' name='answer' style="width: 100px">
                    </div>
                    <div class="col-auto d-flex align-Items-center">
                        <%=((FillInTheBlank) currQuestion).getQuestionText2()%>
                    </div>
                </div>

                <%} else {%>
                <div class="mb-3"><%=currQuestion.getQuestionText()%>
                </div>


                <% if (currQuestion.getType().equals("questionResponse")) {
                %>
                <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'>
                <%
                    }


                    if (currQuestion.getType().equals("pictureResponse")) {
                %>
                <img src="<%=((PictureResponse)currQuestion).getPictureUrl()%>" alt="image" class="mb-3">
                <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'>
                <%
                    }

                    if (currQuestion.getType().equals("multiAnswer")) {
                        for (int i = 0; i < currQuestion.getAnswers().size(); i++) {
                %>
                <input class='form-control bg-dark whitePlaceholder text-light mb-3' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'>
                <%
                        }
                    }


                    if (currQuestion.getType().equals("multipleChoice")) {
                        ArrayList<String> allAnswers = ((MultipleChoice) currQuestion).getAllAnswers();
                        for (int i = 0; i < allAnswers.size(); i++) {
                %>
                <div class="uk-form-controls uk-form-controls-text">
                    <label class="uk-margin-small">
                        <input class='uk-radio' type='radio' name='answers'
                               value="<%=allAnswers.get(i)%>">
                        <%=allAnswers.get(i)%>
                    </label>
                </div>
                <%
                        }
                    }


                    if (currQuestion.getType().equals("multipleChoiceWithMultipleAnswers")) {
                        ArrayList<String> allAnswers = ((MultipleChoice) currQuestion).getAllAnswers();
                        for (int i = 0; i < allAnswers.size(); i++) {

                %>

                <div class="uk-form-controls uk-form-controls-text">
                    <label class="uk-margin-small">
                        <input class='uk-checkbox' type='checkbox' name='answers'
                               value="<%=allAnswers.get(i)%>">
                        <%=allAnswers.get(i)%>
                    </label>
                </div>
                <%
                        }
                    }


                    if (currQuestion.getType().equals("matching")) {
                        ArrayList<String> firstPart = ((Matching) currQuestion).getFirstPart();
                        ArrayList<String> secondPart = ((Matching) currQuestion).getSecondPart();
                %>
                <div class="row">
                    <div class="col-6">
                        <% int i = 0;
                            for (String key: firstPart) {
                                i++;
                        %>
                        <div class="mt-1">
                            <%=i + "). " + key%>
                        </div>
                        <%}%>
                    </div>
                    <div class="col-6">
                        <%
                            for (String val: secondPart) {
                        %>
                        <div class="row mt-1">
                            <input class='form-control bg-dark whitePlaceholder text-light mb-1 mt-1 col-2 '
                                   type='text'
                                   placeholder='#'
                                   aria-label='Input' name='answer'
                                   style="width: 40px; height: 40px">
                            <div class="col-10 d-flex align-items-center"><%=val%>
                            </div>
                        </div>
                        <%}%>
                    </div>
                </div>
                <%
                        }

                    }
                %>
                <input type="submit" class="btn btn-success mt-3" value="Approve Answer And Next Question">
            </form>
        </div>
    </div>

</div>
</body>
</html>
