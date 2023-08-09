<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Types.User" %>
<%@ page import="Types.FriendInfo" %>
<%@ page import="Types.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
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
<br><br>
<div class="container-fluid main">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls:uk-animation-fade delay: 500">
            <div style="font-size: 25px">
                Username:
                <div style="white-space: nowrap; display: inline-block">
                    <%=pageUser.getUsername() %>
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
                 style="border: solid 1px gray; border-radius: 10px;">
                <h4>Friends:</h4>
                <ul class="uk-list uk-list-divider" style="max-height: 275px; overflow: auto">
                    <% ArrayList<Integer> friends = friendsDAO.getFriendsList(profileId);
                        for (Integer friend : friends) {
                            User myFriend = usersDAO.getUserById(friend); %>
                    <li><a href=<%="/profile?user=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                    </a></li>
                    <%
                        }
                    %>
                </ul>
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
                            <tr></tr>
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
    </div>
</div>
</body>
</html>
