<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="Types.*" %>
<%@ page import="jdk.jfr.Category" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="header.jsp" %>
</head>
<body>
<%
    Quiz currQuiz = quizzesDAO.getQuizInfo(Integer.parseInt(request.getParameter("quizId")));
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
%>
<script>
    const btn = document.querySelector("button");
    const post = document.querySelector(".post1");
    const widget = document.querySelector(".star-widget");
    btn.onclick = () => {
        widget.style.display = "none";
        post.style.display = "block";
        return false;
    }

    function deleteScore() {
        location.href = "/quiz?quizId=<%=request.getParameter("quizId")%>"
    }

    $(document).ready(function () {
        <%
            if(request.getParameter("score")!=null){
        %>
        UIkit.modal('#resultsModal').show();
        <%
            }
        %>
    })

    function addRate() {
        let num = 0;
        if (document.getElementById('rate-5').checked) {
            num = 5;
        } else if (document.getElementById('rate-4').checked) {
            num = 4
        } else if (document.getElementById('rate-3').checked) {
            num = 3
        } else if (document.getElementById('rate-2').checked) {
            num = 2
        } else if (document.getElementById('rate-1').checked) {
            num = 1
        }
        $.post('quiz', {
            userId: <%=myUser.getId()%>,
            quizId: <%=currQuiz.getQuizId()%>,
            rate: num,
            comment: $('#comment').val(),
            action: "addRate"
        }, (text) => {
            if (text === 'login') {
                $(location).attr('href', "/login");
            } else $(location).attr('href', "/quiz?quizId=" +<%=currQuiz.getQuizId()%>);
        });
    }

    function deleteComment(userId) {
        $.post('quiz', {userId: userId, quizId: <%=currQuiz.getQuizId()%>, action: "deleteRate"}, (text) => {
            if (text === 'login') {
                $(location).attr('href', "/login");
            }
            $('#' + userId).remove();
            let curNum = parseInt($('#commentsNum').text());
            $('#commentsNum').text(curNum - 1);
        });
    }

    function changeList() {
        $.get('quiz', {
            order: $('#selectOrder').val(),
            quizId: <%=currQuiz.getQuizId()%>,
            action: "changeList"
        }, (text) => {
            if (text !== '') {
                $('#rates').html(text);
            }
        });
    }
</script>
<div id="resultsModal" uk-modal class="main">
    <div class="uk-modal-dialog bg-dark">
        <button class="uk-modal-close-default" type="button" uk-close onclick="deleteScore()"></button>
        <div class="uk-modal-header bg-dark">
            <h2 class="uk-modal-title">Results</h2>
        </div>
        <div class="uk-modal-body">
            <div class="uk-padding-small">
                <div>Score: <%=request.getParameter("score")%>
                </div>
                <%
                    if (request.getParameter("time") != null) {
                %>
                <div>Time: <%=formatter.format(new Date((Long.parseLong(request.getParameter("time")))))%>
                </div>
                <%
                    }
                %>
            </div>

        </div>
        <%if (!ratingsDAO.haveAlreadyRated(myUser.getId(), currQuiz.getQuizId())) {%>
        <div class="container1" style="margin-left: 100px;">
            <div class="star-widget">
                <input type="radio" name="rate" id="rate-5">
                <label for="rate-5" class="fas fa-star"></label>
                <input type="radio" name="rate" id="rate-4">
                <label for="rate-4" class="fas fa-star"></label>
                <input type="radio" name="rate" id="rate-3">
                <label for="rate-3" class="fas fa-star"></label>
                <input type="radio" name="rate" id="rate-2">
                <label for="rate-2" class="fas fa-star"></label>
                <input type="radio" name="rate" id="rate-1">
                <label for="rate-1" class="fas fa-star"></label>
                <form onsubmit="addRate(); return false;">
                    <header></header>
                    <div class="textarea">
                        <textarea id="comment" cols="30" placeholder="Describe your experience.."></textarea>
                    </div>
                    <div class="btn1">
                        <button type="submit">Post</button>
                    </div>
                </form>
            </div>
        </div>
        <%
            }
        %>
        <br>
    </div>
</div>
<div class="container-fluid main">
    <div>
        <div class="row  uk-box-shadow-large uk-padding-small" uk-scrollspy="cls: uk-animation-slide-top; repeat: true">
            <div style="display: inline-block" class="col">
                <h1 style="margin: 0; display: inline-block;">
                    <%=currQuiz.getQuizName()%>
                </h1>
                <% int avgRating = ratingsDAO.getAvgRatingOfQuiz(currQuiz.getQuizId()); %>
                <h1 id="avgRating" style="display: inline-block; white-space: nowrap">
                    <% for (int i = 0; i < avgRating; i++) {%>
                    <span class="fa fa-star checked"></span>
                    <%
                        }
                        for (int i = 0; i < 5 - avgRating; i++) {
                    %>
                    <span class="fa fa-star"></span>
                    <%
                        }
                    %>
                </h1>
            </div>
            <div class="col-auto d-flex align-items-center">
                <a href="/writeQuiz?quizId=<%= currQuiz.getQuizId() %>" class="quiz-button">
                    <button type="button" class="btn btn-success" style="margin-right: 6px">
                        <i class="bi bi-play-circle-fill"></i>
                        Start Quiz
                    </button>
                </a>

                <a href="#modalSendChallenges" class="quiz-button" uk-toggle>
                    <button type="button" class="btn btn-primary" style="margin-right: 6px">
                        <i class="bi bi-share"></i>
                        Send Challenge
                    </button>
                </a>

                <%
                    if (myUser.isAdmin() || currQuiz.getCreatorID() == myUser.getId()) {
                %>

                <a href="/quiz?quizId=<%=currQuiz.getQuizId()%>&action=delete" class="quiz-button">
                    <button type="button" class="btn btn-danger" style="margin-right: 6px">
                        <i class="bi bi-exclamation-octagon-fill"></i>
                        Delete quiz
                    </button>
                </a>

                <%
                    }
                %>
                <div id="modalSendChallenges" uk-modal class="main">
                    <div class="uk-modal-dialog bg-dark">
                        <button class="uk-modal-close-default" type="button" uk-close></button>
                        <div class="uk-modal-header bg-dark">
                            <h2 class="uk-modal-title">Send Challenge</h2>
                        </div>
                        <div class="uk-modal-body">
                            <div class="uk-padding-small">
                                <ul id="requestsList" class="uk-list container-fluid"
                                    style="max-height: 200px; overflow: auto">
                                    <%
                                        ArrayList<Integer> friends = friendsDAO.getFriendsList(myUser.getId());
                                        for (Integer reqId : friends) {
                                            User reqUserInfo = usersDAO.getUserById(reqId);
                                            if (!challengesDAO.alreadyChallenged(myUser.getId(), reqUserInfo.getId(), currQuiz.getQuizId())) {

                                    %>
                                    <li>
                                        <div class="row">
                                            <div class="col d-flex align-items-center">
                                                <a href=<%="/profile?user=" + reqUserInfo.getId()%>>
                                                    <%=reqUserInfo.getUsername()%>
                                                </a>
                                            </div>
                                            <div class="col-auto">
                                                <a href=<%="/quiz?quizId="
                                                        + currQuiz.getQuizId() + "&action=sendChallenge&friendId=" + reqUserInfo.getId()%>>
                                                    <input class="btn btn-success" type="button" value="Send">
                                                </a>
                                            </div>
                                        </div>
                                    </li>
                                    <%
                                                requestId++;
                                            }
                                        }
                                    %>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row uk-margin-small mt-5">
            <div class="col-4 uk-box-shadow-large" style="overflow: auto; margin-bottom: 20px; max-height: 530px" uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
                <h3>Description:</h3>
                <hr>
                <div class="mt-2" style="word-wrap: break-word">
                    <div>
                        Categories:
                        <ul>
                            <%

                                ArrayList<String> list = currQuiz.getCategories();
                                for (String s : list) {
                            %>


                            <li><%=s%>
                            </li>
                            <% } %>
                        </ul>
                    </div>
                    <%=currQuiz.getQuizDescription()%>
                </div>
            </div>
            <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">

                <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                    <li><a style="color: white" href="">
                        <div class="table_name">Global</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">Friends</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">Personal</div>
                    </a></li>
                </ul>
                <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                    style="height: 475px; overflow: auto;">
                    <li>
                        <table class="uk-table uk-table-divider" style="overflow: auto;">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">UserName</div>
                                </th>
                                <th>
                                    <div class="table_name">Score</div>
                                </th>
                                <th>
                                    <div class="table_name">Time</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Result> results = resultsDAO.getQuizResults(currQuiz.getQuizId());
                                int toFollow = Math.min(results.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Result res = results.get(i);

                            %>
                            <tr>
                                <td>
                                    <div class="table_name">
                                        <%=i + 1%>
                                    </div>
                                </td>
                                <td>
                                    <a href="/profile?user=<%=res.getUserId()%>">
                                        <div class="table_name">
                                            <%= usersDAO.getUserById(res.getUserId()).getUsername()%>
                                        </div>
                                    </a>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=res.getScore()%>
                                    </div>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=formatter.format(new Date(res.getSpentTime()))%>
                                    </div>
                                </td>
                            </tr>
                            <tr></tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Username</div>
                                </th>
                                <th>
                                    <div class="table_name">Score</div>
                                </th>
                                <th>
                                    <div class="table_name">Time</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Result> friendsResults = resultsDAO.getUserFriendsResultOnQuiz(myUser.getId(), currQuiz.getQuizId());
                                toFollow = Math.min(friendsResults.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Result res = friendsResults.get(i);

                            %>
                            <tr>
                                <td>
                                    <div class="table_name">
                                        <%=i + 1%>
                                    </div>
                                </td>
                                <td>
                                    <a href="/profile?user=<%=res.getUserId()%>">
                                        <div class="table_name">
                                            <%=usersDAO.getUserById(res.getUserId()).getUsername()%>
                                        </div>
                                    </a>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=res.getScore()%>
                                    </div>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=formatter.format(new Date(res.getSpentTime()))%>
                                    </div>
                                </td>
                            </tr>
                            <tr></tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Username</div>
                                </th>
                                <th>
                                    <div class="table_name">Score</div>
                                </th>
                                <th>
                                    <div class="table_name">Time</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Result> myResults = resultsDAO.getUserResultsOnQuiz(myUser.getId(), currQuiz.getQuizId());
                                toFollow = Math.min(myResults.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Result res = myResults.get(i);

                            %>
                            <tr>
                                <td>
                                    <div class="table_name">
                                        <%=i + 1%>
                                    </div>
                                </td>
                                <td>
                                    <a href="/profile?user=<%=res.getUserId()%>">
                                        <div class="table_name">
                                            <%=myUser.getUsername()%>
                                        </div>
                                    </a>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=res.getScore()%>
                                    </div>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=formatter.format(new Date(res.getSpentTime()))%>
                                    </div>
                                </td>
                            </tr>
                            <tr></tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>
                </ul>
            </div>
        </div>
        <%
            ArrayList<Types.Rating> rates = ratingsDAO.getQuizRatings(currQuiz.getQuizId(), "newest");
            if (ratingsDAO.haveAlreadyRated(myUser.getId(), currQuiz.getQuizId())) {
                for (int i = 0; i < rates.size(); i++) {
                    if (rates.get(i).getUserId() == myUser.getId()) {
                        Rating rr = rates.get(i);
                        rates.remove(i);
                        rates.add(0, rr);
                    }
                }
            }
        %>
        <div class="row mt-3 uk-box-shadow-large uk-padding-small" uk-scrollspy="cls: uk-animation-fade; repeat: true">
            <div style="align-content: center; display: inline-block;">

                <div class="col-auto "
                     style="overflow: auto; margin-bottom: 10px; text-align: left; font-size: 20px; display: inline-block;">
                    <div style="display: inline-block" id="commentsNum"><%=rates.size()%>
                    </div>
                    <div style="display: inline-block">Comments</div>
                </div>
                <div class="col-auto "
                     style="overflow: auto; margin-bottom: 10px; font-size: 220px; display: inline-block; float: right">
                    <select id="selectOrder" onchange="changeList()" aria-label="Custom controls" id="newQuestionType"
                            class="form-select form-select-md bg-dark text-light">
                        <option value="newest">Newest first</option>
                        <option value="oldest">Oldest first</option>
                    </select>
                </div>
            </div>
            <hr>
            <div id="rates">
                <%
                    for (Rating rate : rates) {
                        User user = usersDAO.getUserById(rate.getUserId());
                %>
                <div id="<%=user.getId()%>">

                    <div style="display: inline-block;">
                        <a href="/profile?user=<%=user.getId()%>">
                            <div style="font-size:17px; display: inline-block;">
                                @<%=user.getUsername()%>
                            </div>
                        </a>
                        <% int rating = rate.getRating(); %>
                        <div style="font-size:17px; display: inline-block; ">
                            <% for (int i = 0; i < rating; i++) {%>
                            <span class="fa fa-star checked"></span>
                            <%
                                }
                                for (int i = 0; i < 5 - rating; i++) {
                            %>
                            <span class="fa fa-star"></span>
                            <%
                                }
                            %>
                        </div>
                        <div style="font-size:15px; display: inline-block; color: #aaa">
                            | <%=rate.getRatingsDate()%>
                        </div>
                    </div>

                    <%
                        if (myUser.isAdmin() || user.getId() == myUser.getId()) {
                    %>
                    <a class="btn-danger" style="float:right; color: #aaa" onclick="deleteComment(<%=user.getId()%>)">Delete
                        comment</a>
                    <%
                        }
                    %>

                    <div style=" margin-bottom: 25px">
                        <%=rate.getComment()%>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>
</body>
</html>
