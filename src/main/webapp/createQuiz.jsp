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
            crossorigin="anonymous">
    </script>

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
        $('#titleLabel').val($('#titleField').val());
        $('#descriptionLabel').val($('#descriptionField').val());
    }

    function addNewQuestion() {
        const questionType = $('#newQuestionType').val();
        let childToAppend = "<input type='hidden' name='action' value='addQuestion'>";
        childToAppend += "<input type='hidden' name='title' value='' id='titleLabel'>";
        childToAppend += "<input type='hidden' name='description' value='' id='descriptionLabel'>";

        switch (questionType) {
            case "questionResponse":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='questionResponse'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "fillInTheBlank":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='fillInTheBlank'>" +
                    "<div class='uk-margin row' style='margin-top: 0!important;'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'> " +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 4px; padding-right: 4px'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'>" +
                    "    </div> </div> <div id='answerFields'> " +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "multipleChoice":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multipleChoice' id='questionType'>" +
                    "<div id='indexes'>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'>" +
                    "</div>" +
                    "<div id='answerRadios'>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'><input class='uk-radio' type='radio' name='answers' checked>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-radio' type='radio' name='answers'>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerRadio()' value='Add new answer'>";
                break;

            case "multipleChoiceWithMultipleAnswers":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multipleChoiceWithMultipleAnswers' id='questionType'>" +
                    "<div id='indexes'>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'>" +
                    "</div>" +
                    "<div id='answerCheckboxes'>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-checkbox' type='checkbox' name='answers' checked>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-checkbox' type='checkbox' name='answers'>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerCheckbox()' value='Add new answer'>";
                break;

            case "pictureResponse":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='pictureResponse'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText'>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Image URL' aria-label='Input' name='questionImage'>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'> ";
                break;

            case "multiAnswer":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multiAnswer'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "matching":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='matching'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText'>" +
                    "</div>" +
                    "<div class='row uk-margin'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'>" +
                    "    </div>" +
                    "    <div class='col-2'>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'>" +
                    "    </div>" +
                    "</div>" +
                    "<div class='uk-margin row' style='margin-top: 0!important;'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'>" +
                    "    </div>" +
                    "    <div class='col-2'>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'>" +
                    "    </div>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "</div>" +
                    "<input type='button' class='btn btn-success' onclick='addAnswerMatching()' value='Add new answer'>";
                break;

            default:
                break;
        }

        childToAppend += "<br><button type='submit' class='btn btn-success mt-3'>Create question</button>";

        $('#formForThisType').html(childToAppend);
    }

    function removeAnswer(e) {
        $(e).parent().parent().parent().remove();
    }

    function addAnswerField() {
        $('#answerFields').append("" +
            "<div class='uk-margin'>" +
            "    <div class='row'>" +
            "        <div class='col'>" +
            "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
            "       </div>" +
            "        <div class='col-auto'>" +
            "            <input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    function addAnswerRadio() {
        $('#answerRadios').append("" +
            "<div class='row  d-flex align-items-center uk-margin'>" +
            "   <div class='col-auto'>" +
            "       <input class='uk-radio' type='radio' name='answers'>" +
            "   </div>" +
            "   <div class='col'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    function addAnswerCheckbox() {
        $('#answerCheckboxes').append("" +
            "<div class='row  d-flex align-items-center uk-margin'>" +
            "   <div class='col-auto'>" +
            "       <input class='uk-checkbox' type='checkbox' name='answers'>" +
            "   </div>" +
            "   <div class='col'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText'>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    function addAnswerMatching() {
        $('#answerFields').append("" +
            "<div class='uk-margin row' style='margin-top: 0!important;'>" +
            "   <div class='col' style='padding-right: 0'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1'>" +
            "   </div>" +
            "   <div class='col-2'>" +
            "   </div>" +
            "   <div class='col' style='padding-left: 0'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2'>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    $(document).ready(() => {})
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
                       id="titleField"
                       value="<%if(session.getAttribute("title")!=null) out.print(session.getAttribute("title"));%>"
                       style="width: 50%;">
                <textarea class="uk-margin form-control bg-dark whitePlaceholder text-light"
                          placeholder="Description"
                          aria-label="Description"
                          name="description"
                          id="descriptionField"
                          style="height: 150px"><%if (session.getAttribute("description") != null) out.print(session.getAttribute("description")); %></textarea>
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
                        <input type="button" class="btn btn-success" id="addNewQuestionBtn" onclick="addNewQuestion()"
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
                            <div class="col d-flex align-items-center"><%=i + 1%>)
                                <%
                                    if (currQuestion.getType().equals("fillInTheBlank"))
                                        out.print(currQuestion.getQuestionText() + " _____ " + ((FillInTheBlank) currQuestion).getQuestionText2());
                                    else {
                                        out.print(currQuestion.getQuestionText());
                                    }
                                %>
                            </div>
                            <div class="col-auto">
                                <a href="/createQuiz?action=edit&index=<%=i%>">
                                    <input type="button" class="btn btn-primary" value="Edit">
                                </a>
                                <a href="/createQuiz?action=delete&index=<%=i%>">
                                    <input type="button" class="btn btn-danger" value="Delete">
                                </a>
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
                            <%
                                if(request.getParameter("editMode") != null && request.getParameter("editMode").equals("true")) {
                            %>
                        <input type='hidden' name='index' value=<%=request.getParameter("index")%>>
                        <input type='hidden' name='action' value='addQuestion'>
                        <input type='hidden' name='title' value='' id='titleLabel'>
                        <input type='hidden' name='description' value='' id='descriptionLabel'>
                            <%
                                if(request.getParameter("type").equals("questionResponse")) {
                            %>
                        <input type='hidden' name='questionType' value='questionResponse'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div id='answerFields'>
                            <%
                                String[] answers = request.getParameterValues("answerText");
                                for (int i = 0; i < answers.length; i++) {
                                    if (i == 0) {
                            %>
                            <div class='uk-margin'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Answer' aria-label='Input' name='answer'
                                       value="<%=answers[i]%>" required>
                            </div>
                            <%
                            } else {
                            %>
                            <div class='uk-margin'>
                                <div class='row'>
                                    <div class='col'>
                                        <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                               placeholder='Answer' aria-label='Input' name='answer'
                                               value="<%=answers[i]%>" required>
                                    </div>
                                    <div class='col-auto'>
                                        <input type='button' class='btn btn-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                                }
                                if(request.getParameter("type").equals("fillInTheBlank")) {
                                    String[] answers = request.getParameterValues("answerText");
                            %>
                        <input type='hidden' name='questionType' value='fillInTheBlank'>
                        <div class='uk-margin row' style='margin-top: 0!important;'>
                            <div class='col' style='padding-right: 0'><input
                                    class='form-control bg-dark whitePlaceholder text-light' type='text'
                                    placeholder='Question part 1' aria-label='Input' name='questionText1'
                                    value="<%=request.getParameter("questionText1")%>"></div>
                            <div class='col' style='padding-left: 4px; padding-right: 4px'><input
                                    class='form-control bg-dark whitePlaceholder text-light' type='text'
                                    placeholder='Answer' aria-label='Input' name='answer' required
                                    value="<%=answers[0]%>"></div>
                            <div class='col' style='padding-left: 0'><input
                                    class='form-control bg-dark whitePlaceholder text-light' type='text'
                                    placeholder='Question part 2' aria-label='Input' name='questionText2'
                                    value="<%=request.getParameter("questionText2")%>"></div>
                        </div>
                        <div id='answerFields'>
                            <%
                                for (int i = 1; i < answers.length; i++) {
                            %>
                            <div class='uk-margin'>
                                <div class='row'>
                                    <div class='col'>
                                        <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                               placeholder='Answer' aria-label='Input' name='answer'
                                               value="<%=answers[i]%>" required>
                                    </div>
                                    <div class='col-auto'>
                                        <input type='button' class='btn btn-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                            }
                            if(request.getParameter("type").equals("multipleChoice")) {
                        %>
                        <input type='hidden' name='questionType' value='multipleChoice' id='questionType'>
                        <div id='indexes'></div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div id='answerRadios'>
                            <%
                                String[] correctAnswers = request.getParameterValues("correctAnswerText");
                                String[] incorrectAnswers = request.getParameterValues("incorrectAnswerText");
                                for (int i = 0; i < correctAnswers.length; i++) {

                            %>
                            <div class='row  d-flex align-items-center uk-margin'>
                                <div class='col-auto'>
                                    <input class='uk-radio' type='radio' name='answers' checked>
                                </div>
                                <div class='col'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Answer' aria-label='Input' name='answerText'
                                           value="<%=correctAnswers[i]%>">
                                </div>
                            </div>
                            <%
                                }
                                for (String incorrectAnswer : incorrectAnswers) {

                            %>
                            <div class='row  d-flex align-items-center uk-margin'>
                                <div class='col-auto'>
                                    <input class='uk-radio' type='radio' name='answers'>
                                </div>
                                <div class='col'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Answer' aria-label='Input' name='answerText'
                                           value="<%=incorrectAnswer%>">
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerRadio()' value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                            } if(request.getParameter("type").equals("multipleChoiceWithMultipleAnswers")) {
                        %>
                        <input type='hidden' name='questionType' value='multipleChoiceWithMultipleAnswers'
                               id='questionType'>
                        <div id='indexes'></div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div id='answerCheckboxes'>
                            <%
                                String[] correctAnswers = request.getParameterValues("correctAnswerText");
                                String[] incorrectAnswers = request.getParameterValues("incorrectAnswerText");
                                for (int i = 0; i < correctAnswers.length; i++) {

                            %>
                            <div class='row  d-flex align-items-center uk-margin'>
                                <div class='col-auto'>
                                    <input class='uk-checkbox' type='checkbox' name='answers' checked>
                                </div>
                                <div class='col'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Answer' aria-label='Input' name='answerText'
                                           value="<%=correctAnswers[i]%>">
                                </div>
                            </div>
                            <%
                                }
                                for (int i = 0; i < incorrectAnswers.length; i++) {

                            %>
                            <div class='row  d-flex align-items-center uk-margin'>
                                <div class='col-auto'>
                                    <input class='uk-checkbox' type='checkbox' name='answers'>
                                </div>
                                <div class='col'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Answer' aria-label='Input' name='answerText'
                                           value="<%=incorrectAnswers[i]%>">
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerCheckbox()'
                               value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                            }  if(request.getParameter("type").equals("pictureResponse")) {
                            %>
                        <input type='hidden' name='questionType' value='pictureResponse'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Image URL' aria-label='Input' name='questionImage'
                                   value="<%=request.getParameter("imageUrl")%>">
                        </div>
                        <div id='answerFields'>
                            <%
                                String[] answers = request.getParameterValues("answerText");
                                for (int i = 0; i < answers.length; i++) {
                                    if (i == 0) {
                            %>
                            <div class='uk-margin'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Answer' aria-label='Input' name='answer'
                                       value="<%=answers[i]%>" required>
                            </div>
                            <%
                            } else { %>
                            <div class='uk-margin'>
                                <div class='row'>
                                    <div class='col'>
                                        <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                               placeholder='Answer' aria-label='Input' name='answer'
                                               value="<%=answers[i]%>" required>
                                    </div>
                                    <div class='col-auto'>
                                        <input type='button' class='btn btn-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerField()' value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                                }
                            if(request.getParameter("type").equals("matching")) {
                                String[] keys = request.getParameterValues("key");
                                String[] values = request.getParameterValues("value");
                           %>
                        <input type='hidden' name='questionType' value='matching'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div class='row uk-margin'>
                            <div class='col' style='padding-right: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 1' aria-label='Input' name='questionText1'
                                       value="<%=keys[0]%>">
                            </div>
                            <div class='col-2'></div>
                            <div class='col' style='padding-left: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 2' aria-label='Input' name='questionText2'
                                       value="<%=values[0]%>">
                            </div>
                        </div>
                        <div class='uk-margin row' style='margin-top: 0!important;'>
                            <div class='col' style='padding-right: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 1' aria-label='Input' name='questionText1'
                                       value="<%=keys[1]%>">
                            </div>
                            <div class='col-2'>
                            </div>
                            <div class='col' style='padding-left: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 2' aria-label='Input' name='questionText2'
                                       value="<%=values[1]%>">
                            </div>
                        </div>
                        <div id='answerFields'>
                            <%
                                for (int i = 2; i < keys.length; i++) {
                            %>
                            <div class='uk-margin row' style='margin-top: 0!important;'>
                                <div class='col' style='padding-right: 0'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Question part 1' aria-label='Input' name='questionText1'
                                           value="<%=keys[i]%>">
                                </div>
                                <div class='col-2'>
                                </div>
                                <div class='col' style='padding-left: 0'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Question part 2' aria-label='Input' name='questionText2'
                                           value="<%=values[i]%>">
                                </div>
                                <div class='col-auto'>
                                    <input type='button' class='btn btn-danger' value='Delete'
                                           onclick='removeAnswer(this)'>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-success' onclick='addAnswerMatching()'
                               value='Add new answer'>
                        <br>
                        <button type='submit' class='btn btn-primary mt-3'>Edit question</button>
                            <%
                                } if(request.getParameter("type").equals("multiAnswer")) {
                            %>
                        <input type='hidden' name='questionType' value='multiAnswer'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>">
                        </div>
                        <div id='answerFields'>
                                <%
                                        String[] answers = request.getParameterValues("answerText");
                                        for(int i = 0; i < answers.length; i++) {
                                            if(i == 0) {
                                %>
                            <div class='uk-margin'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Answer' aria-label='Input' name='answer'
                                       value="<%=answers[i]%>" required>
                            </div>
                                <%
                                            } else {
                                %>
                            <div class='uk-margin'>
                                <div class='row'>
                                    <div class='col'>
                                        <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                               placeholder='Answer' aria-label='Input' name='answer'
                                               value="<%=answers[i]%>" required>
                                    </div>
                                    <div class='col-auto'>
                                        <input type='button' class='btn btn-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                                <%
                                    }
                                }
                            }
                        }
                    %>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
