<%@ page import="Types.*" %>
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
    ArrayList<Question> questions = (ArrayList<Question>) session.getAttribute("writingQuestions");
    if (questions == null) {
        questions = questionsDAO.getQuestions(currQuiz.getQuizId());
        session.setAttribute("writingQuestions", questions);
    }
    int questionInd = Integer.parseInt(request.getParameter("questionInd"));
    Question currQuestion = questions.get(questionInd);
    ArrayList<String>[] answers = (ArrayList<String>[]) session.getAttribute("userAnswers");

%>
<script>
    function goToQuestion(ind) {
        $('#nextQuestionInd').val(ind);
        $('#currQuestionForm').submit();
    }
</script>
<div class="container">
    <div class="row mt-3">
        <div class="col-3 uk-box-shadow-large uk-padding-small">
            <h1 style="margin: 0"><%=currQuiz.getQuizName()%>
            </h1>

            <div class="row justify-content-center">
                <%
                    for (int i = 0; i < questions.size(); i++) {
                %>
                <a class="col-auto text-center"
                   href="#"
                   onclick="goToQuestion(<%=i%>)"
                   style="width: 50px;
                       <%
                        if(questionInd==i) out.print("background-color: #3e4042;");
                        else if(answers[i]!=null && !answers[i].isEmpty() && !answers[i].get(0).trim().equals("")) out.print("background-color: #363636;");
                   %> border: darkgrey 1px solid; margin:2px;">
                    <%=i + 1%>
                </a>
                <%
                    }
                %>
            </div>
            <form method="post" action="/writeQuiz">
                <input type="hidden" name="action" value="finish">
                <input type="submit" class="btn btn-success mt-3" value="Finish">
            </form>
        </div>
        <div class="col-1"></div>
        <div class="col-8 uk-box-shadow-large uk-padding-small">
            <h4>#<%=questionInd + 1%> Question</h4>
            <form action="/writeQuiz" method="post" id="currQuestionForm">
                <input type="hidden" name="quizId" value="<%=currQuiz.getQuizId()%>">
                <input type="hidden" name="questionInd" value="<%=questionInd%>">
                <input type="hidden" name="nextQuestionInd" value="<%=questionInd+1%>" id="nextQuestionInd">
                <input type="hidden" name="action" value="questionAnswer">
                <% if (currQuestion.getType().equals(QuestionTypes.fillInTheBlank)) {
                %>
                <div class="uk-form-controls uk-form-controls-text row">
                    <div class="col-auto d-flex align-Items-center"><%=currQuestion.getQuestionText()%>
                    </div>
                    <div class="col-2">
                        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                               aria-label='Input' name='answer' style="width: 100px"
                            <%
                           if(answers[questionInd]!=null && !answers[questionInd].isEmpty()) out.print("value='"+answers[questionInd].get(0)+"'");
                       %>>
                    </div>
                    <div class="col-auto d-flex align-Items-center">
                        <%=((QuestionFillInTheBlank) currQuestion).getQuestionText2()%>
                    </div>
                </div>

                <%} else {%>
                <div class="mb-3"><%=currQuestion.getQuestionText()%>
                </div>


                <% if (currQuestion.getType().equals(QuestionTypes.textResponse)) {
                %>
                <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'
                    <%
                           if(answers[questionInd]!=null && !answers[questionInd].isEmpty()) out.print("value='"+answers[questionInd].get(0)+"'");
                       %>>
                <%
                    }


                    if (currQuestion.getType().equals(QuestionTypes.pictureResponse)) {
                %>
                <img src="<%=((QuestionPictureResponse)currQuestion).getPictureUrl()%>" alt="image" class="mb-3">
                <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'
                    <%
                           if(answers[questionInd]!=null && !answers[questionInd].isEmpty()) out.print("value='"+answers[questionInd].get(0)+"'");
                       %>>
                <%
                    }

                    if (currQuestion.getType().equals(QuestionTypes.multiAnswers)) {
                        for (int i = 0; i < currQuestion.getAnswers().size(); i++) {
                %>
                <input class='form-control bg-dark whitePlaceholder text-light mb-3' type='text' placeholder='Answer'
                       aria-label='Input' name='answer'
                    <%
                           if(answers[questionInd]!=null && !answers[questionInd].isEmpty() && i<answers[questionInd].size()) out.print("value='"+answers[questionInd].get(i)+"'");
                       %>>
                <%
                        }
                    }


                    if (currQuestion.getType().equals(QuestionTypes.multipleChoices)) {
                        assert currQuestion instanceof QuestionMultipleChoices;
                        ArrayList<String> allAnswers = ((QuestionMultipleChoices) currQuestion).getAllAnswers();
                        for (String allAnswer : allAnswers) {
                %>
                <div class="uk-form-controls uk-form-controls-text">
                    <label class="uk-margin-small">
                        <input class='uk-radio' type='radio' name='answer'
                               value="<%=allAnswer%>"
                            <%
                            if(answers[questionInd]!=null && !answers[questionInd].isEmpty() && answers[questionInd].contains(allAnswer))
                                out.print("checked");
                            %>
                        >
                        <%=allAnswer%>
                    </label>
                </div>
                <%
                        }
                    }


                    if (currQuestion.getType().equals(QuestionTypes.multipleChoicesWithMultipleAnswers)) {
                        assert currQuestion instanceof QuestionMultipleChoicesWithMultipleAnswers;
                        ArrayList<String> allAnswers = ((QuestionMultipleChoicesWithMultipleAnswers) currQuestion).getAllAnswers();
                        for (String allAnswer : allAnswers) {

                %>

                <div class="uk-form-controls uk-form-controls-text">
                    <label class="uk-margin-small">
                        <input class='uk-checkbox' type='checkbox' name='answer'
                               value="<%=allAnswer%>"
                            <%
                            if(answers[questionInd]!=null && !answers[questionInd].isEmpty() && answers[questionInd].contains(allAnswer))
                                out.print("checked");
                            %>
                        >
                        <%=allAnswer%>
                    </label>
                </div>
                <%
                        }
                    }


                    if (currQuestion.getType().equals(QuestionTypes.matching)) {
                        assert currQuestion instanceof QuestionMatching;
                        ArrayList<String> firstPart = ((QuestionMatching) currQuestion).getFirstPart();
                        ArrayList<String> secondPart = ((QuestionMatching) currQuestion).getSecondPart();
                %>
                <div class="row">
                    <div class="col-6">
                        <% int i = 0;
                            for (String key : firstPart) {
                                i++;
                        %>
                        <div class="mt-1">
                            <%=i + "). " + key%>
                        </div>
                        <%}%>
                    </div>
                    <div class="col-6">
                        <%
                            i = 0;
                            for (String val : secondPart) {
                        %>
                        <div class="row mt-1">
                            <input class='form-control bg-dark whitePlaceholder text-light mb-1 mt-1 col-2 '
                                   type='text'
                                   placeholder='#'
                                   aria-label='Input' name='answer'
                                   style="width: 40px; height: 40px"
                                <%
                           if(answers[questionInd]!=null && !answers[questionInd].isEmpty() && i<answers[questionInd].size()) out.print("value='"+answers[questionInd].get(i)+"'");
                       %>>
                            <div class="col-10 d-flex align-items-center"><%=val%>
                            </div>
                        </div>
                        <%
                                i++;
                            }%>
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
