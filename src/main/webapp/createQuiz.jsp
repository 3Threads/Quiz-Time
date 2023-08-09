<%@ page import="Controllers.CreateQuizServlet" %>
<%@ page import="Types.*" %>
<%@ page import="java.sql.Time" %>
<%@ page import="java.util.*" %>
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

    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css"
          rel="stylesheet">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js">
    </script>

    <!-- Include Moment.js CDN -->
    <script type="text/javascript" src=
            "https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment.min.js">
    </script>

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<script type="text/javascript">
    function beforeSubmit() {
        if ($('#questionType').val() === "multipleChoices" || $('#questionType').val() === "multipleChoicesWithMultipleAnswers") {
            if ($('#questionType').val() === "multipleChoicesWithMultipleAnswers") {
                if (document.querySelector('input[name=answers]:checked') == null) {
                    $('#errors').html(errorMessageConstructor("You must choose at least 1 correct answer"));
                    return false;
                }
            }
            $("input[name=answers]").each(function (i, o) {
                if ($(this).is(":checked")) {
                    $('#indexes').append("<input type='hidden' name='choosedIndex' value='" + i + "' id='indexOfChecked'>");
                }
            });
        }
        $('#titleLabel').val($('#titleField').val());
        $('#descriptionLabel').val($('#descriptionField').val());
        $('#hourLabel').val($('#Hour').val());
        $('#minuteLabel').val($('#Minute').val());
        $('#secondLabel').val($('#Second').val());
        if ($('#timeFormatCheckBox').is(':checked')) {
            $('#timeFormatCheckLabel').val("on");
        } else $('#timeFormatCheckLabel').val("off");
        let selectedCategories = '';
        $("input[name=category]").each(function () {
            if ($(this).is(":checked")) {
                selectedCategories += $(this).val();
                selectedCategories += ',';
            }
        });
        $('#categoriesLabel').val(selectedCategories);
    }

    function errorMessageConstructor(message) {
        return '<br><div class="alert alert-danger alert-dismissible d-flex align-items-center fade show"> <i class="bi-exclamation-octagon-fill"></i> <strong class="mx-2">Error!</strong> ' + message + ' <button type="button" class="btn-close" data-bs-dismiss="alert"></button> </div>';
    }

    function addNewQuestion() {
        const questionType = $('#newQuestionType').val();
        let childToAppend = "<input type='hidden' name='action' value='addQuestion'>";
        childToAppend += "<input type='hidden' name='title' value='' id='titleLabel'>";
        childToAppend += "<input type='hidden' name='description' value='' id='descriptionLabel'>";
        childToAppend += "<input type='hidden' name='hour' value='' id='hourLabel'>";
        childToAppend += "<input type='hidden' name='minute'  value='' id='minuteLabel'>";
        childToAppend += "<input type='hidden' name='second'  value='' id='secondLabel'>";
        childToAppend += "<input type='hidden' name='timeFormatChecker'  value='' id='timeFormatCheckLabel'>";
        childToAppend += "<input type='hidden' name='categories'  value='' id='categoriesLabel'>";
        switch (questionType) {
            case "textResponse":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='textResponse'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText' required>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "fillInTheBlank":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='fillInTheBlank'>" +
                    "<div class='uk-margin row' style='margin-top: 0!important;'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1' required> " +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 4px; padding-right: 4px'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2' required>" +
                    "    </div> </div> <div id='answerFields'> " +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "multipleChoices":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multipleChoices' id='questionType'>" +
                    "<div id='indexes'>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText' required>" +
                    "</div>" +
                    "<div id='answerRadios'>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'><input class='uk-radio' type='radio' name='answers' checked>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-radio' type='radio' name='answers'>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerRadio()' value='Add new answer'>";
                break;

            case "multipleChoicesWithMultipleAnswers":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multipleChoicesWithMultipleAnswers' id='questionType'>" +
                    "<div id='indexes'>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText' required>" +
                    "</div>" +
                    "<div id='answerCheckboxes'>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-checkbox' type='checkbox' name='answers' checked>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
                    "        </div>" +
                    "    </div>" +
                    "    <div class='row  d-flex align-items-center uk-margin'>" +
                    "        <div class='col-auto'>" +
                    "            <input class='uk-checkbox' type='checkbox' name='answers'>" +
                    "        </div>" +
                    "        <div class='col'>" +
                    "            <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerCheckbox()' value='Add new answer'>";
                break;

            case "pictureResponse":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='pictureResponse'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input' name='questionText' required>" +
                    "</div>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Image URL' aria-label='Input' pattern='https?://.+' title='Include http://' name='questionImage' required>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerField()' value='Add new answer'> ";
                break;

            case "multiAnswers":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='multiAnswers'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText' required>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "    <div class='uk-margin'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answer' required>" +
                    "    </div>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerField()' value='Add new answer'>";
                break;

            case "matching":
                childToAppend +=
                    "<input type='hidden' name='questionType' value='matching'>" +
                    "<div class='uk-margin' style='margin-top: 0!important;'>" +
                    "    <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question' aria-label='Input'  name='questionText' required>" +
                    "</div>" +
                    "<div class='row uk-margin'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1' required>" +
                    "    </div>" +
                    "    <div class='col-2'>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2' required>" +
                    "    </div>" +
                    "</div>" +
                    "<div class='uk-margin row' style='margin-top: 0!important;'>" +
                    "    <div class='col' style='padding-right: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1' required>" +
                    "    </div>" +
                    "    <div class='col-2'>" +
                    "    </div>" +
                    "    <div class='col' style='padding-left: 0'>" +
                    "        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2' required>" +
                    "    </div>" +
                    "</div>" +
                    "<div id='answerFields'>" +
                    "</div>" +
                    "<input type='button' class='btn btn-outline-success' onclick='addAnswerMatching()' value='Add new answer'>";
                break;

            default:
                break;
        }
        childToAppend += "<div id='errors'></div>";
        childToAppend += "<button type='submit' class='btn btn-outline-success mt-3'>Create question</button>";

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
            "            <input type='button' class='btn btn-outline-danger' value='Delete' onclick='removeAnswer(this)'>" +
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
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-outline-danger' value='Delete' onclick='removeAnswer(this)'>" +
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
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer' aria-label='Input' name='answerText' required>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-outline-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    function addAnswerMatching() {
        $('#answerFields').append("" +
            "<div class='uk-margin row' style='margin-top: 0!important;'>" +
            "   <div class='col' style='padding-right: 0'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 1' aria-label='Input' name='questionText1' required>" +
            "   </div>" +
            "   <div class='col-2'>" +
            "   </div>" +
            "   <div class='col' style='padding-left: 0'>" +
            "       <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Question part 2' aria-label='Input' name='questionText2' required>" +
            "   </div>" +
            "   <div class='col-auto'>" +
            "       <div>" +
            "           <input type='button' class='btn btn-outline-danger' value='Delete' onclick='removeAnswer(this)'>" +
            "       </div>" +
            "   </div>" +
            "</div>");
    }

    function editOrDelete(action, index) {
        let url = "/createQuiz?action=" + action + "&index=" + index;
        if ($('#titleField').val() !== '') {
            console.log($('#titleField').val());
            url += "&title=";
            url += $('#titleField').val();
        }
        if ($('#descriptionField').val() !== '') {
            console.log($('#descriptionField').val());
            url += "&description=";
            url += $('#descriptionField').val();
        }
        if ($('#Hour').val() !== '') {
            url += "&hour=";
            url += $('#Hour').val();
        }
        if ($('#Minute').val() !== '') {
            url += "&minute=";
            url += $('#Minute').val();
        }
        if ($('#Second').val() !== '') {
            url += "&second=";
            url += $('#Second').val();
        }
        url += "&timeFormatChecker=";
        if ($('#timeFormatCheckBox').is(':checked')) {
            url += "on";
        } else url += "off";
        url += "&categories=";
        $("input[name=category]").each(function () {
            if ($(this).is(":checked")) {
                url += $(this).val();
                url += ',';
            }
        });
        $(location).attr('href', url);
    }

    function toggleTimeFormat() {
        $('#timeFormat').toggle();
        const checkbox = $("#timeFormatCheckBox");
        if (checkbox.is(':checked')) {
            $('#Hour').prop('required', true);
            $('#Minute').prop('required', true);
            $('#Second').prop('required', true);
        } else {
            $('#Hour').prop('required', false);
            $('#Minute').prop('required', false);
            $('#Second').prop('required', false);
        }
    }

    $(document).ready(
        function () {
            if (<%=session.getAttribute("timeFormatChecker") != null
                && session.getAttribute("timeFormatChecker").equals("off")%>) {
                $('#timeFormat').toggle();
                $('#Hour').prop('required', false);
                $('#Minute').prop('required', false);
                $('#Second').prop('required', false);
                $('#timeFormatCheckBox').prop('checked', false);
            } else $('#timeFormatCheckBox').prop('checked', true);
        }
    )
</script>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <div class="row mt-3">
        <div class="col-6">
            <form name="quiz-form" action="/createQuiz" method="post">
                <input type="hidden" value="createQuiz" name="action">
                <input class="uk-margin form-control bg-dark whitePlaceholder text-light" type="text"
                       placeholder="Title"
                       aria-label="Title"
                       name="title"
                       id="titleField"
                       pattern="[^$\|\/]+"
                       title="Your quiz name must not exist '$','|' or '/'"
                       value="<%if(session.getAttribute("title")!=null) out.print(session.getAttribute("title"));%>"
                       style="width: 50%;" required>
                <%
                    if (request.getAttribute("QuizTitleExist") != null) {
                %>
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi-exclamation-octagon-fill"></i>
                    <strong>Error!</strong> Quiz with same title is already exist.
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                %>
                <textarea class="uk-margin form-control bg-dark whitePlaceholder text-light"
                          placeholder="Description"
                          aria-label="Description"
                          name="description"
                          id="descriptionField"
                          style="height: 150px" required><%
                    if (session.getAttribute("description") != null) out.print(session.getAttribute("description"));
                %></textarea>
                <div class="mb-2">
                    <button class="btn btn-dark" type="button">Select categories</button>
                    <div uk-dropdown="mode: click; pos: bottom-center; animation: slide-top; animate-out: true"
                         class="bg-dark text-light" style="max-height: 100px; overflow: auto; border: white 1px solid">
                        <%
                            ArrayList<String> selected = null;
                            if (session.getAttribute("categories") != null)
                                selected = (ArrayList<String>) session.getAttribute("categories");
                            Set<String> categories = new HashSet<String>(Arrays.asList("Sports", "Entertainment", "Geography", "Science", "History", "Art", "Literature", "Music", "Technology", "Animals", "Economy", "Politics"));
                        %>
                        <ul class="uk-nav uk-dropdown-nav">
                            <%
                                for (String category : categories) {
                            %>
                            <li>
                                <label>
                                    <input class='uk-checkbox' type='checkbox' name='category' value="<%=category%>"
                                        <%if (selected != null && selected.contains(category)) {%>
                                           checked <% }%>> <%=category%>
                                </label>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                    </div>
                </div>
                <label><input id="timeFormatCheckBox" class="uk-checkbox" type="checkbox" name="timeFormatCheck"
                              onchange="toggleTimeFormat()"> Set timer</label>
                <%
                    Time time = null;
                    if (session.getAttribute("timeLimit") != null) time = (Time) session.getAttribute("timeLimit");
                %>
                <div class="row mt-2" id="timeFormat">
                    <div class="col-4">
                        <input class='form-control bg-dark whitePlaceholder text-light' type='number'
                               placeholder='Hour' id="Hour" aria-label='Input' name='hour'
                               value="<%if(time != null) out.print(time.getHours());%>" required min="0" max="3">
                    </div>
                    <div class="col-4">
                        <input class='form-control bg-dark whitePlaceholder text-light' type='number'
                               placeholder='Minute' id="Minute" aria-label='Input' name='minute'
                               value="<%if(time != null) out.print(time.getMinutes());%>" required min="0" max="59">
                    </div>
                    <div class="col-4">
                        <input class='form-control bg-dark whitePlaceholder text-light' type='number'
                               placeholder='Second' id="Second" aria-label='Input' name='second'
                               value="<%if(time != null) out.print(time.getSeconds());%>" required min="0" max="59">
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col">
                        <select aria-label="Custom controls" id="newQuestionType"
                                class="form-select form-select-md bg-dark text-light">
                            <option value="textResponse">Question Response</option>
                            <option value="fillInTheBlank">Fill in the blank</option>
                            <option value="pictureResponse">Picture Response</option>
                            <option value="multipleChoices">Multiple Choice</option>
                            <option value="multipleChoicesWithMultipleAnswers">Multiple choice with multiple answers
                            </option>
                            <option value="multiAnswers">Multi Answers</option>
                            <option value="matching">Matching</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <input type="button" class="btn btn-outline-success" id="addNewQuestionBtn"
                               onclick="addNewQuestion()"
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
                                    if (currQuestion.getType().equals(QuestionTypes.fillInTheBlank))
                                        out.print(currQuestion.getQuestionText() + " _____ " + ((QuestionFillInTheBlank) currQuestion).getQuestionText2());
                                    else {
                                        out.print(currQuestion.getQuestionText());
                                    }
                                %>
                            </div>
                            <div class="col-auto">
                                <input onclick="editOrDelete('edit',<%=i%>)" type="button"
                                       class="btn btn-outline-primary" value="Edit">
                                <input onclick="editOrDelete('delete',<%=i%>)" type="button"
                                       class="btn btn-outline-danger" value="Delete">
                            </div>
                        </div>
                    </li>
                    <%
                        }
                    %>
                </ul>
                <%
                    if (request.getSession().getAttribute("questions") != null) {
                        if (((ArrayList<Question>) request.getSession().getAttribute("questions")).size() != 0) {
                %>
                <button type="submit" class="btn btn-outline-success">Create Quiz</button>
                <%
                        }
                    }
                %>
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
                        <input type='hidden' name='hour' value='' id='hourLabel'>
                        <input type='hidden' name='minute' value='' id='minuteLabel'>
                        <input type='hidden' name='second' value='' id='secondLabel'>
                        <input type='hidden' name='timeFormatChecker' value='' id='timeFormatCheckLabel'>
                        <input type='hidden' name='categories' value='' id='categoriesLabel'>
                            <%
                                if(request.getParameter("type").equals("textResponse")) {
                            %>
                        <input type='hidden' name='questionType' value='textResponse'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>" required>
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
                                        <input type='button' class='btn btn-outline-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerField()'
                               value='Add new answer'>
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
                                    value="<%=request.getParameter("questionText1")%>" required></div>
                            <div class='col' style='padding-left: 4px; padding-right: 4px'><input
                                    class='form-control bg-dark whitePlaceholder text-light' type='text'
                                    placeholder='Answer' aria-label='Input' name='answer'
                                    value="<%=answers[0]%>" required></div>
                            <div class='col' style='padding-left: 0'><input
                                    class='form-control bg-dark whitePlaceholder text-light' type='text'
                                    placeholder='Question part 2' aria-label='Input' name='questionText2'
                                    value="<%=request.getParameter("questionText2")%>" required></div>
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
                                        <input type='button' class='btn btn-outline-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerField()'
                               value='Add new answer'>
                            <%
                            }
                            if(request.getParameter("type").equals("multipleChoices")) {
                        %>
                        <input type='hidden' name='questionType' value='multipleChoices' id='questionType'>
                        <div id='indexes'></div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>" required>
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
                                           value="<%=correctAnswers[i]%>" required>
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
                                           value="<%=incorrectAnswer%>" required>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerRadio()'
                               value='Add new answer'>
                            <%
                            } if(request.getParameter("type").equals("multipleChoicesWithMultipleAnswers")) {
                        %>
                        <input type='hidden' name='questionType' value='multipleChoicesWithMultipleAnswers'
                               id='questionType'>
                        <div id='indexes'></div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>" required>
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
                                           value="<%=correctAnswers[i]%>" required>
                                </div>
                            </div>
                            <%
                                }
                                if (incorrectAnswers != null) {
                                    for (int i = 0; i < incorrectAnswers.length; i++) {

                            %>
                            <div class='row  d-flex align-items-center uk-margin'>
                                <div class='col-auto'>
                                    <input class='uk-checkbox' type='checkbox' name='answers'>
                                </div>
                                <div class='col'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Answer' aria-label='Input' name='answerText'
                                           value="<%=incorrectAnswers[i]%>" required>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerCheckbox()'
                               value='Add new answer'>
                            <%
                            }  if(request.getParameter("type").equals("pictureResponse")) {
                            %>
                        <input type='hidden' name='questionType' value='pictureResponse'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>" required>
                        </div>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Image URL' aria-label='Input' name='questionImage'
                                   pattern="https?://.+" title="Include http://"
                                   value="<%=request.getParameter("imageUrl")%>" required>
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
                                        <input type='button' class='btn btn-outline-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerField()'
                               value='Add new answer'>
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
                                   value="<%=request.getParameter("questionText")%>" required>
                        </div>
                        <div class='row uk-margin'>
                            <div class='col' style='padding-right: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 1' aria-label='Input' name='questionText1'
                                       value="<%=keys[0]%>" required>
                            </div>
                            <div class='col-2'></div>
                            <div class='col' style='padding-left: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 2' aria-label='Input' name='questionText2'
                                       value="<%=values[0]%>" required>
                            </div>
                        </div>
                        <div class='uk-margin row' style='margin-top: 0!important;'>
                            <div class='col' style='padding-right: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 1' aria-label='Input' name='questionText1'
                                       value="<%=keys[1]%>" required>
                            </div>
                            <div class='col-2'>
                            </div>
                            <div class='col' style='padding-left: 0'>
                                <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                       placeholder='Question part 2' aria-label='Input' name='questionText2'
                                       value="<%=values[1]%>" required>
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
                                           value="<%=keys[i]%>" required>
                                </div>
                                <div class='col-2'>
                                </div>
                                <div class='col' style='padding-left: 0'>
                                    <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                           placeholder='Question part 2' aria-label='Input' name='questionText2'
                                           value="<%=values[i]%>" required>
                                </div>
                                <div class='col-auto'>
                                    <input type='button' class='btn btn-outline-danger' value='Delete'
                                           onclick='removeAnswer(this)'>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <input type='button' class='btn btn-outline-success' onclick='addAnswerMatching()'
                               value='Add new answer'>
                            <%
                                } if(request.getParameter("type").equals("multiAnswers")) {
                            %>
                        <input type='hidden' name='questionType' value='multiAnswers'>
                        <div class='uk-margin' style='margin-top: 0!important;'>
                            <input class='form-control bg-dark whitePlaceholder text-light' type='text'
                                   placeholder='Question' aria-label='Input' name='questionText'
                                   value="<%=request.getParameter("questionText")%>" required>
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
                                        <input type='button' class='btn btn-outline-danger' value='Delete'
                                               onclick='removeAnswer(this)'>
                                    </div>
                                </div>
                            </div>
                                <%
                                    }
                                }
                            }%>
                            <div id="errors"></div>
                            <button type='submit' class='btn btn-outline-primary mt-3'>Edit question</button>
                                <%
                        }
                    %>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
