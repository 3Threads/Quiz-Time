<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
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

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous">
    </script>

    <link rel="stylesheet" type="text/css" href="style.css">

    <meta charset="utf-8">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>

    <script>
        const btn = document.querySelector("button");
        const post = document.querySelector(".post1");
        const widget = document.querySelector(".star-widget");
        btn.onclick = () => {
            widget.style.display = "none";
            post.style.display = "block";
            return false;
        }
    </script>

    <title>Quiz Time</title>
</head>
<body>
<%@include file="header.jsp" %>
<%
    Quiz currQuiz = quizzesDAO.getQuizInfo(Integer.parseInt(request.getParameter("quizId")));
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

%>
<script>
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
        $.get('quiz', {order: $('#selectOrder').val(), quizId: <%=currQuiz.getQuizId()%>, action: "changeList"}, (text) => {
            if(text !== '') {
                $('#rates').html(text);
            }
        });
    }
</script>
<div id="resultsModal" uk-modal>
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
<div class="container">
    <div class="row">
        <div class="row mt-3 uk-box-shadow-large uk-padding-small">
            <div style="display: inline-block" class="col">
                <h1 style="margin: 0; display: inline-block;">
                    <%=currQuiz.getQuizName()%>
                </h1>
                <% int avgRating = ratingsDAO.getAvgRatingOfQuiz(currQuiz.getQuizId()); %>
                <h1 id="avgRating"style="display: inline-block; white-space: nowrap">
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
                <a href="/writeQuiz?quizId=<%=currQuiz.getQuizId()%>">
                    <input type="button" class="btn btn-success" value="Start Quiz" style="margin-right: 6px">
                </a>
                <a href="#modalSendChallenges" uk-toggle>
                    <input type="button" class="btn btn-primary" value="Send Challenge">
                </a>
                <%
                    if (myUser.isAdmin() || currQuiz.getCreatorID() == myUser.getId()) {
                %>
                <a href="/quiz?quizId=<%=currQuiz.getQuizId()%>&action=delete">
                    <input type="button" class="btn btn-danger" value="Delete Quiz" style="margin-left: 6px">
                </a>
                <%
                    }
                %>
                <div id="modalSendChallenges" uk-modal>
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
            <div class="col-4 uk-box-shadow-large" style="overflow: auto; margin-bottom: 20px; max-height: 530px">
                <h3>Description:</h3>
                <hr>
                <div class="mt-2">
                    <%=currQuiz.getQuizDescription()%>
                </div>
            </div>
            <div class="col-8">
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
        <div class="row mt-3 uk-box-shadow-large uk-padding-small" style="margin-bottom: 50px;">
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
