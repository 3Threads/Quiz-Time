<%@ page import="Types.Question" %>
<%@ page import="Types.FillInTheBlank" %>
<%@ page import="Controllers.CreateQuizServlet" %>
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
<script type="text/javascript">
    function beforeSubmit() {
        if ($('#questionType').val() === "multipleChoice" || $('#questionType').val() === "multipleChoiceWithMultipleAnswers") {
            $("input[name=answers]").each(function (i, o) {
                if ($(this).is(":checked")) {
                    $('#indexes').append("<input type='hidden' name='choosedIndex' value='" + i + "' id='indexOfChecked'>");
                }
            });
        }
    }

    function addNewQuestion() {
        const questionType = $('#newQuestionType').val();
        let childToAppend = "<input type='hidden' name='action' value='addQuestion'>";
        if (questionType === "questionResponse") {
            childToAppend += "<input type='hidden' name='questionType' value='questionResponse'><div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'></div><div id='answerFields'><div class='uk-margin'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required></div></div><input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>";
        }

        if (questionType === "fillInTheBlank") {
            childToAppend += "<input type='hidden' name='questionType' value='fillInTheBlank'><div class='uk-margin row' style='margin-top: 0!important;'> <div class='col' style='padding-right: 0'> <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'> </div> <div class='col' style='padding-left: 4px; padding-right: 4px'> <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required> </div> <div class='col' style='padding-left: 0'> <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'> </div> </div> <div id='answerFields'> </div> <input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>"
        }

        if (questionType === "multipleChoice") {
            childToAppend += "<input type='hidden' name='questionType' value='multipleChoice' id='questionType'><div id='indexes'></div><div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'></div><div id='answerRadios'><div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-radio' type='radio' name='answers' checked></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div></div><div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-radio' type='radio' name='answers'></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div></div></div><input type='button' class='btn btn-success' onclick='addAnswerRadio()' value='Add new answer'>";
        }

        if (questionType === "multipleChoiceWithMultipleAnswers") {
            childToAppend += "<input type='hidden' name='questionType' value='multipleChoiceWithMultipleAnswers' id='questionType'><div id='indexes'></div><div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'></div><div id='answerCheckboxes'><div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-checkbox' type='checkbox' name='answers' checked></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div></div><div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-checkbox' type='checkbox' name='answers'></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div></div></div><input type='button' class='btn btn-success' onclick='addAnswerCheckbox()' value='Add new answer'>";
        }

        if (questionType === "pictureResponse") {
            childToAppend += "<input type='hidden' name='questionType' value='pictureResponse'> <div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'> </div> <div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Image URL' aria-label='Input' name='questionImage'> </div> <div id='answerFields'> <div class='uk-margin'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required></div> </div> <input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'> ";
        }

        if (questionType === "multiAnswer") {
            childToAppend += "<input type='hidden' name='questionType' value='multiAnswer'><div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'></div><div id='answerFields'><div class='uk-margin'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required></div></div><input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>";
        }

        if (questionType === "matching") {
            childToAppend += "<input type='hidden' name='questionType' value='matching'><div class='uk-margin' style='margin-top: 0!important;'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'></div><div class='row uk-margin'><div class='col' style='padding-right: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'></div><div class='col-2'></div><div class='col' style='padding-left: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'></div></div><div class='uk-margin row' style='margin-top: 0!important;'><div class='col' style='padding-right: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'></div><div class='col-2'></div><div class='col' style='padding-left: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'></div></div><div id='answerFields'></div><input type='button' class='btn btn-success' onclick='addAnswerMatching()' value='Add new answer'>";
        }


        childToAppend += "<br><button type='submit' class='btn btn-success mt-3'>Create question</button>";

        $('#formForThisType').html(childToAppend);
    }

    function removeAnswer(e) {
        $(e).parent().parent().parent().remove();
    }

    function addAnswerField() {
        $('#answerFields')
            .append("<div class='uk-margin'><div class='row'><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required></div><div class='col-auto'><input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'></div></div></div>");
    }

    function addAnswerRadio() {
        $('#answerRadios')
            .append("<div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-radio' type='radio' name='answers'></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div><div class='col-auto'><div><input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'></div></div></div>");
    }

    function addAnswerCheckbox() {
        $('#answerCheckboxes')
            .append("<div class='row  d-flex align-items-center uk-margin'><div class='col-auto'><input class='uk-checkbox' type='checkbox' name='answers'></div><div class='col'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'></div><div class='col-auto'><div><input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'></div></div></div>");
    }

    function addAnswerMatching() {
        $('#answerFields')
            .append("<div class='uk-margin row' style='margin-top: 0!important;'><div class='col' style='padding-right: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'></div><div class='col-2'></div><div class='col' style='padding-left: 0'><input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'></div><div class='col-auto'><div><input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'></div></div></div>");
    }

    $(document).ready(() => {

    })
</script>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="row mt-3">
        <div class="col-6">
            <form action="/createQuiz" method="post">
                <input type="hidden" value="createQuiz" name="action">
                <input class="uk-margin form-control bg-dark whitePlaceholder text-light" type="text"
                       placeholder="Title"
                       aria-label="Title"
                       name="title"
                       style="width: 50%;">
                <textarea class="uk-margin form-control bg-dark whitePlaceholder text-light"
                          placeholder="Description"
                          aria-label="Description"
                          name="description"
                          style="height: 150px"></textarea>
                <div class="row">
                    <div class="col">
                        <select aria-label="Custom controls" id="newQuestionType"
                                class="form-select form-select-md bg-dark text-light">
                            <option value="questionResponse">Question Response</option>
                            <option value="fillInTheBlank">Fill in the blank</option>
                            <option value="pictureResponse">Picture Response</option>
                            <option value="multipleChoice">Multiple Choice</option>
                            <option value="multipleChoiceWithMultipleAnswers">Multiple choice with multiple answers
                            </option>
                            <option value="multiAnswer">Multi Answers</option>
                            <option value="matching">Matching</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <input class="btn btn-success" id="addNewQuestionBtn" onclick="addNewQuestion()"
                               value="Add new question">
                    </div>
                </div>
                <ul class="uk-list mt-3">
                    <%
                        ArrayList<Question> questions = CreateQuizServlet.getQuestionsFromSession(request);
                        for (int i = 0; i < questions.size(); i++) {
                            Question currQuestion = questions.get(i);
                    %>
                    <li>
                        <div class="row">
                            <div class="col d-flex align-items-center">
                                <%=i + 1%>) <%
                                if (currQuestion.getType().equals("fillInTheBlank"))
                                    out.print(currQuestion.getQuestionText() + " _____ " + ((FillInTheBlank) currQuestion).getQuestionText2());
                                else {
                                    out.print(currQuestion.getQuestionText());
                                }%>
                            </div>
                            <div class="col-auto">
                                <button class="btn btn-primary">Edit</button>
                                <button class="btn btn-danger">Delete</button>
                            </div>
                        </div>
                    </li>
                    <%
                        }
                    %>
                </ul>
                <button class="btn btn-success">Create Quiz</button>
            </form>

        </div>
        <div class="col-6">
            <div id="currQuestionBox">
                <form action="/createQuiz" method="post" onsubmit="return beforeSubmit()">
                    <div id="formForThisType">

                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
