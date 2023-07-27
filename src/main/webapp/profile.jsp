<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Types.User" %>
<%@ page import="Types.FriendInfo" %>
<%@ page import="Types.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <!-- UIkit CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/css/uikit.min.css"/>

    <!-- UIkit JS -->
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit-icons.min.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous">
    </script>

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<%@include file="header.jsp" %>
<%
    int profileId = Integer.parseInt(request.getParameter("user"));
    User pageUser = usersDAO.getUserById(profileId);

    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
%>
<body class="bg-dark text-light">
<br><br>
<div class="container">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls:uk-animation-fade delay: 500">
            <h3>
                Username: <%=pageUser.getUsername() %>
            </h3>
            <br>
            <% if (profileId != myUser.getId()) {
                FriendInfo info = friendsDAO.getBetweenUsersInfo(profileId, myUser.getId());
                if (info.getAccepted() == -1) {
            %>
            <a href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=sendRequest"%>>
                <button class="btn btn-success">Add Friend</button>
            </a>
            <%
                }
                if (info.getAccepted() == 0) {
                    if (info.getUser1Id() == profileId) {
            %>
            <a href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=acceptRequest"%>>
                <button class="btn btn-success">Accept Request</button>
            </a><br><br>
            <a href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=rejectRequest&from=profile"%>>
                <button class="btn btn-danger">Reject Request</button>
            </a>
            <%
            } else {
            %>
            <a href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=cancelRequest"%>>
                <button class="btn btn-danger">Delete Request</button>
            </a>
            <% }
            }
                if (info.getAccepted() == 1) {%>
            <a href=<%="/friends?user1=" + myUser.getId() + "&user2=" + profileId + "&action=unfriend"%>>
                <button class="btn btn-danger">Unfriend</button>
            </a>

            <%
                }
            %>
            <a href=<%="/chat?chatWith=" + pageUser.getId()%>>
                <button class="btn btn-primary">Chat</button>
            </a>
            <br><br><br>
            <%
                }
            %>

            <% if (pageUser.getId() == myUser.getId() || myUser.isAdmin()) {
            %>
            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="deleteProfile">
                <button type="submit" class="btn btn-danger">Delete User</button>
            </form>
            <%
                }
                if (myUser.isAdmin()) {
                    if (pageUser.isAdmin()) {
            %>
            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="deleteAdmin">
                <button type="submit" class="btn btn-danger">Delete Admin</button>
            </form>
            <%
            } else {
            %>
            <form method="post" action="/profile">
                <input type="hidden" name="userId" value=<%=pageUser.getId()%>>
                <input type="hidden" name="action" value="addToAdmin">
                <button type="submit" class="btn btn-primary">Add Admin</button>
            </form>
            <%
                    }
                }
            %>
            <br>
            <div class="uk-padding-small " uk-scrollspy="cls: uk-animation-slide-left; repeat: true"
                 style="border: solid 1px gray; border-radius: 10px;">
                <h4>Friends:</h4>
                <ul class="uk-list uk-list-divider" style="max-height: 200px; overflow: auto">
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
                    <li><a style="color: white" href="">Created quizzes</a></li>
                    <li><a style="color: white" href="">Completed quizzes</a></li>
                </ul>
                <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                    style="max-height: 485px; overflow: auto;">

                    <li>
                        <table name="My created" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Completed</th>
                                <th>Creation</th>
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
                                <td><%=i + 1%>
                                </td>
                                <td><a href="/quiz?quizId=<%=quiz.getQuizId()%>"><%=quiz.getQuizName()%>
                                </a></td>
                                <td><%=quiz.getCompleted()%>
                                </td>
                                <td><%=quiz.getCreationTime()%>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>

                    <li>
                        <table name="My Completed" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Score</th>
                                <th>Time</th>
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
                                <td><%=i + 1%>
                                </td>
                                <td><a href="/quiz?quizId=<%=quizId%>"><%=quiz.getQuizName()%>
                                </a></td>
                                <td><%=result.getScore()%>
                                </td>
                                <td><%=formatter.format(new Date(result.getSpentTime()))%>
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
