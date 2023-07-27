<%@ page import="Types.Quiz" %>
<%@ page import="Types.Result" %>
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
<body>
<%@include file="header.jsp" %>
<%
    Quiz currQuiz = quizzesDAO.getQuizInfo(Integer.parseInt(request.getParameter("quizId")));

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
                <div>Time: <%=request.getParameter("time")%>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row mt-3 uk-box-shadow-large uk-padding-small">
        <div class="col"><h1 style="margin: 0"><%=currQuiz.getQuizName()%>
        </h1></div>
        <div class="col-auto d-flex align-items-center">
            <a href="/writeQuiz?quizId=<%=currQuiz.getQuizId()%>">
                <input type="button" class="btn btn-success" value="Start Quiz" style="margin-right: 6px">
            </a>
            <a href="#modalSendChallenges" uk-toggle>
                <input type="button" class="btn btn-primary" value="Send Challenge">
            </a>
            <%if (myUser.isAdmin() || currQuiz.getCreatorID() == myUser.getId()) {%>
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
                                            <a href=<%="/profile?user=" + reqUserInfo.getId()%>><%=reqUserInfo.getUsername()%>
                                            </a>
                                        </div>
                                        <div class="col-auto">
                                            <a href=<%="/quiz?quizId=" + currQuiz.getQuizId() + "&action=sendChallenge&friendId=" + reqUserInfo.getId()%>>
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
        <div class="col-4 uk-box-shadow-large" style="max-height: 537px; overflow: auto; margin-bottom: 20px">
            <h3>Description:</h3>
            <div class="mt-2"><%=currQuiz.getQuizDescription()%>
            </div>
        </div>
        <div class="col-8">
            <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                <li><a style="color: white" href="">Global</a></li>
                <li><a style="color: white" href="">Friends</a></li>
                <li><a style="color: white" href="">Personal</a></li>
            </ul>
            <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                style="max-height: 485px; overflow: auto;">
                <li>
                    <table class="uk-table uk-table-divider" style="max-height: 600px; overflow: auto;">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Username</th>
                            <th>Score</th>
                            <th>Time</th>
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
                            <td><%=i + 1%>
                            </td>
                            <td>
                                <a href="/profile?user=<%=res.getUserId()%>"><%= usersDAO.getUserById(res.getUserId()).getUsername()%>
                                </a>
                            </td>
                            <td><%=res.getScore()%>
                            </td>
                            <td><%=res.getSpentTime()%>
                            </td>
                        </tr>
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
                            <th>#</th>
                            <th>Username</th>
                            <th>Score</th>
                            <th>Time</th>
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
                            <td><%=i + 1%>
                            </td>
                            <td>
                                <a href="/profile?user=<%=res.getUserId()%>"><%= usersDAO.getUserById(res.getUserId()).getUsername()%>
                                </a>
                            </td>
                            <td><%=res.getScore()%>
                            </td>
                            <td><%=res.getSpentTime()%>
                            </td>
                        </tr>
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
                            <th>#</th>
                            <th>Username</th>
                            <th>Score</th>
                            <th>Time</th>
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
                            <td><%=i + 1%>
                            </td>
                            <td>
                                <a href="/profile?user=<%=res.getUserId()%>"><%= myUser.getUsername()%>
                                </a>
                            </td>
                            <td><%=res.getScore()%>
                            </td>
                            <td><%=res.getSpentTime()%>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
