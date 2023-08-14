<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Types.User" %>
<%@ page import="Types.FriendInfo" %>
<%@ page import="Types.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="BusinessLogic.RankingSystem" %>
<html>
<head>
    <%@include file="header.jsp" %>
</head>
<%
    int profileId = Integer.parseInt(request.getParameter("user"));
    User pageUser = usersDAO.getUserById(profileId);

    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
%>
<body class="bg-dark text-light">
<div class="container-fluid main">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls:uk-animation-fade delay: 500">
            <div style="font-size: 25px; padding-bottom: 10px; display: flex; align-items: center">
                <img class="rank-test" src="/images/rank<%=RankingSystem.countRank(pageUser.getScore())%>.png"
                     alt="rank-test">
                <div style="white-space: nowrap; display: inline-block; padding-left: 10px; font-size:25px">
                    <div class="rank-<%=RankingSystem.countRank(pageUser.getScore())%>"
                         style="display: inline-block;">
                        <%=pageUser.getUsername() %>
                    </div>
                    <%
                        if (pageUser.getId() != myUser.getId()) {
                    %>
                    <a href=<%="/chat?chatWith=" + pageUser.getId()%>>
                        <div style="display: inline-block;">
                            <i class="bi bi-envelope"></i>
                        </div>
                    </a>
                    <%
                        }
                    %>
                </div>
                <hr>
            </div>
            <div class="row d-flex align-items-center mb-2">
                <div class="col-auto">
                    <img style="display: inline-block; width: 30px; height: auto;"
                         src="/images/rank<%=RankingSystem.countRank(pageUser.getScore())%>.png"
                         alt="rank-test">
                </div>
                <%

                    int rank = RankingSystem.countRank(pageUser.getScore());
                    int score = pageUser.getScore();
                    int percent = (score - 200 * rank) / 2;
                    int nextRank = Math.min(5, RankingSystem.countRank(pageUser.getScore()) + 1);
                %>
                <div class="progress bg-dark col" role="progressbar" style="border: solid 0.5px white; padding: 0;">
                    <div class="progress-bar"
                         style="width: <% if(nextRank==5) out.print(100);else out.print(percent); %>%;">
                        <div class="progress-text">
                            <%
                                String scoreText = score + "/" + (rank + 1) * 200;
                                if (nextRank == 5) {
                                    scoreText = String.valueOf(score);
                                }
                            %>
                            <%= scoreText %>
                        </div>
                    </div>
                </div>
                <div class="col-auto">
                    <img style="display: inline-block; width: 30px; height: auto; "
                         src="/images/rank<%=nextRank%>.png"
                         alt="rank-test">
                </div>
            </div>
            <% if (profileId != myUser.getId()) {
                FriendInfo info = friendsDAO.getBetweenUsersInfo(profileId, myUser.getId());
                if (info.getAccepted() == -1) {
            %>
            <a title="Add friend"
               href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=sendRequest"%>>
                <button type="button" class="btn btn-outline-success profile-button">
                    <i class="bi bi-person-plus-fill"></i> Add friend
                </button>
            </a>

            <%
                }
                if (info.getAccepted() == 0) {
                    if (info.getUser1Id() == profileId) {
            %>

            <div>
                <a title="Accept friend request"
                   href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=acceptRequest"%>>
                    <button style="display: inline-block; width: 48%" type="button"
                            class="btn btn-outline-success profile-button">
                        <i class="bi bi-person-plus-fill"></i> Accept
                    </button>
                </a>

                <a title="Reject friend request"
                   href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=rejectRequest&from=profile"%>>
                    <button style="display: inline-block; width: 48%; float: right" type="button"
                            class="btn btn-outline-danger profile-button">
                        <i class="bi bi-person-x-fill"></i> Reject
                    </button>
                </a>
            </div>

            <%
            } else {
            %>

            <a title="Delete friend request"
               href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=cancelRequest"%>>
                <button type="button" class="btn btn-outline-danger profile-button">
                    <i class="bi bi-x-octagon-fill"></i> Delete request
                </button>
            </a>

            <%
                    }
                }
                if (info.getAccepted() == 1) {
            %>

            <a title="Remove friend"
               href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=unfriend"%>>
                <button type="button" class="btn btn-outline-danger profile-button">
                    <i class="bi bi-person-dash-fill"></i> Remove
                </button>
            </a>

            <%
                    }
                }
            %>

            <%
                if (myUser.isAdmin()) {
                    if (pageUser.isAdmin()) {
            %>

            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="deleteAdmin">

                <a title="Remove the user from the admin role">
                    <button type="submit" class="btn btn-outline-danger profile-button">
                        <i class="bi bi-chevron-double-down"></i> Remove admin
                    </button>
                </a>
            </form>

            <%
            } else {
            %>
            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="addToAdmin">
                <a title="Grant admin privileges to the user">
                    <button type="submit" class="btn btn-outline-primary profile-button">
                        <i class="bi bi-chevron-double-up"></i> Make admin
                    </button>
                </a>
            </form>
            <%
                    }
                }
            %>
            <%
                if (pageUser.getId() == myUser.getId() || myUser.isAdmin()) {
            %>
            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="deleteProfile">
                <a title="Delete user's account">
                    <button type="submit" class="btn btn-outline-danger profile-button">
                        <i class="bi bi-exclamation-octagon-fill"></i>
                        Delete
                    </button>
                </a>
            </form>
            <%
                }
            %>
            <br>
            <div class="uk-padding-small " uk-scrollspy="cls: uk-animation-slide-left; repeat: true"
                 style="border: solid 1px gray; border-radius: 6px; max-height: 300px; overflow: auto">
                <h4>Friends:</h4>
                <hr style="margin-bottom:4px">
                <% ArrayList<Integer> friends = friendsDAO.getFriendsList(profileId);
                    for (Integer friend : friends) {
                        User myFriend = usersDAO.getUserById(friend);
                %>
                <div style="font-size: 20px; padding-left: 8px; padding-top:8px">
                    <a class="rank-<%=RankingSystem.countRank(myFriend.getScore())%>"
                       href=<%="/profile?user=" + myFriend.getId()%>>
                        <%=myFriend.getUsername()%>
                    </a>
                </div>
                <%
                    }
                %>
            </div>
        </div>
        <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">
            <br>
            <div>
                <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                    <li><a style="color: white" href="">
                        <div class="table_name">Created quizzes</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">Completed quizzes</div>
                    </a></li>
                </ul>
                <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                    style="height: 475px; overflow: auto;">

                    <%--My created quizzes--%>
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
                                </th>
                                <th>
                                    <div class="table_name">Users</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> myCreated = quizzesDAO.getMyCreatedQuizzes(myUser.getId());
                                int toFollow = Math.min(myCreated.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Quiz quiz = myCreated.get(i);

                            %>
                            <tr>
                                <td>
                                    <div class="table_name">
                                        <%=i + 1%>
                                    </div>
                                </td>
                                <td>
                                    <a href="/quiz?quizId=<%=quiz.getQuizId()%>">
                                        <div class="table_name">
                                            <%=quiz.getQuizName()%>
                                        </div>
                                    </a>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=quiz.getCompleted()%>
                                    </div>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=quiz.getCreationTime()%>
                                    </div>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>

                    <%--My completed quizzes--%>
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
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
                                ArrayList<Result> myWritten = resultsDAO.getUserResults(myUser.getId());
                                toFollow = Math.min(myWritten.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Result result = myWritten.get(i);
                                    int quizId = result.getQuizId();
                                    Quiz quiz = quizzesDAO.getQuizInfo(quizId);

                            %>
                            <tr>
                                <td>
                                    <div class="table_name">
                                        <%=i + 1%>
                                    </div>
                                </td>
                                <td>
                                    <a href="/quiz?quizId=<%=quiz.getQuizId()%>">
                                        <div class="table_name">
                                            <%=quiz.getQuizName()%>
                                        </div>
                                    </a>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=result.getScore()%>
                                    </div>
                                </td>
                                <td>
                                    <div class="table_name">
                                        <%=formatter.format(new Date(result.getSpentTime()))%>
                                    </div>
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
</div>
</body>
</html>
