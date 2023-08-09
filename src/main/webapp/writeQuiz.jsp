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


    const timeout = setTimeout(function () {
        $('#finishQuiz').submit()
    }, <% if(session.getAttribute("endTime") != null) out.println(((Long)session.getAttribute("endTime")) - new Date().getTime());%>);


</script>
<div class="container">
    <div class="row mt-3">
        <div class="col-3 uk-box-shadow-large uk-padding-small" style="height: 100%">
            <h1 style="margin: 5px"><%=currQuiz.getQuizName()%>
            </h1>

            <div class="row justify-content-left" style="margin-left: 5px; margin-right: 2px; margin-top: 5px">
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
            <form method="post" action="/writeQuiz" id="finishQuiz" style="float: right;">
                <input type="hidden" name="action" value="finish">
                <input type="hidden" name="quizId" value="<%=request.getParameter("quizId")%>">
                <input type="submit" class="btn btn-success mt-3" value="Finish">
            </form>
        </div>
        <div class="col-1"></div>
        <div class="col-8 uk-box-shadow-large uk-padding-small">
            <div class="row">
                <div class="col">
                    <h4>#<%=questionInd + 1%> Question</h4>
                </div>
                <%
                    if (session.getAttribute("endTime") != null) {
                %>
                <div class="col-auto">
                    <div class="uk-grid-small uk-child-width-auto" uk-grid id="countdown"
                         uk-countdown="date: <%=new Date((Long) session.getAttribute("endTime") + 1000).toGMTString()%>">
                        <div class="mt-1">
                            <div class="uk-countdown-number uk-countdown-hours" style="font-size: medium"></div>
                        </div>
                        <div class="uk-countdown-separator" style="font-size: small">:</div>
                        <div class="mt-1">
                            <div class="uk-countdown-number uk-countdown-minutes" style="font-size: medium"></div>
                        </div>
                        <div class="uk-countdown-separator" style="font-size: small">:</div>
                        <div class="mt-1">
                            <div class="uk-countdown-number uk-countdown-seconds" style="font-size: medium"></div>
                        </div>
                    </div>
                </div>
                <%
                } else {
                %>
                <script>
                    clearTimeout(timeout);
                </script>
                <%
                    }
                %>
            </div>

            <form action="/writeQuiz" method="post" id="currQuestionForm">
                <input type="hidden" name="quizId" value="<%=currQuiz.getQuizId()%>">
                <input type="hidden" name="questionInd" value="<%=questionInd%>">
                <input type="hidden" name="nextQuestionInd" value="<%=questionInd+1%>" id="nextQuestionInd">
                <input type="hidden" name="action" value="questionAnswer">
                <%
                    out.print(currQuestion.getWriteQuizHTML(answers, questionInd));
                %>
                <input type="submit" class="btn btn-success mt-3" value="Next Question">
            </form>
        </div>
    </div>
</div>
</body>
</html>
