<%--
  Created by IntelliJ IDEA.
  User: agurg
  Date: 7/5/2023
  Time: 8:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Types.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="DAO.*" %>
<%@ page import="java.util.HashMap" %>
<html>
<head>
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<%
    User myUser = (User) session.getAttribute("userInfo");
    UsersDAO usersDAO = (UsersDAO) application.getAttribute("usersDB");
    FriendsDAO friendsDAO = (FriendsDAO) application.getAttribute("friendsDB");
    ChallengesDao challengesDAO = (ChallengesDao) application.getAttribute("challengesDB");
    MessagesDAO messagesDAO = (MessagesDAO) application.getAttribute("messagesDB");
    QuizzesDAO quizzesDAO = (QuizzesDAO) application.getAttribute("quizzesDB");
    ResultsDAO resultsDAO = (ResultsDAO) application.getAttribute("resultsDB");
%>
<script>
    function requestAction(user1, user2, action, requestId) {
        const http = new XMLHttpRequest();
        const url = "friends?user1=" + user1 + "&user2=" + user2 + "&action=" + action;
        http.onreadystatechange = function () {
            if(http.readyState == 4) {
                const element = document.getElementById("request"+requestId);
                element.remove();
            }
        }
        http.open("POST",url);
        http.send(null);
    }
    function getRequests() {
        $.get('getRequests', (responseText) => {
            $('#requestsList').html(responseText);
        });
    }
    function getChatNotifications() {
        $.get('chatNotification', (responseText) => {
            console.log(responseText);
            $('#chatNotifications').html(responseText);
        });
    }
    $(document).ready(function () {
        setInterval(getRequests,2000);
        setInterval(getChatNotifications,2000);
    });
</script>
<body class="bg-dark text-light">
<div class="container">
    <div class="row">
        <div class="col">
            <a href="/homePage"><img src="images/logo.png" class="img-fluid" alt="..."></a>
        </div>
        <div class="col-2">
        </div>
        <div class="col d-flex align-items-end">
            <div class="container">
                <div class="d-flex flex-row-reverse mb-2">
                    <a style="margin-left: 5px" href="/logout">
                        Log Out
                    </a>
                    <a>|</a>
                    <a href="#modal-notifications" class="mt-1" uk-toggle><i class="bi bi-bell-fill mt-1"
                                                                             style="margin-right: 5px"></i>
                    </a>
                    <div id="modal-notifications" uk-modal>
                        <div class="uk-modal-dialog bg-dark">
                            <button class="uk-modal-close-default" type="button" uk-close></button>
                            <div class="uk-modal-header bg-dark">
                                <h2 class="uk-modal-title">Modal Title</h2>
                            </div>
                            <div class="uk-modal-body">
                                <ul class="uk-flex-left" data-uk-tab="{connect:'#notification tab'}">
                                    <li><a style="color: white" href="">Friend Requests</a></li>
                                    <li><a style="color: white" href="">Challenges</a></li>
                                    <li><a style="color: white" href="">Messages</a></li>
                                </ul>
                                <ul id="notification tab" class="uk-switcher uk-margin">
                                    <li>
                                        <div class="uk-padding-small">
                                            <ul id="requestsList" class="uk-list container-fluid"
                                                style="max-height: 200px; overflow: auto">
                                                <%
                                                    ArrayList<Integer> requests = friendsDAO.getFriendsRequests(myUser.getId());
                                                    int requestId = 1;
                                                    for (Integer reqId : requests) {
                                                        User reqUserInfo = usersDAO.getUserById(reqId);
                                                %>
                                                <li>
                                                    <div class="row" id=<%="request"+requestId%>>
                                                        <div class="col d-flex align-items-center">
                                                            <a href=<%="/profile?myUser=" + reqUserInfo.getId()%>><%=reqUserInfo.getUsername()%>
                                                            </a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'acceptRequest', <%=requestId%>)" class="btn btn-success">accept</button>
                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'rejectRequest', <%=requestId%>)" class="btn btn-danger">Reject</button>
                                                        </div>
                                                    </div>
                                                </li>
                                                <%
                                                        requestId++;
                                                    } %>
                                            </ul>

                                        </div>
                                    </li>
                                    <li>
                                        <div class="uk-padding-small">
                                            <ul class="uk-list container-fluid "
                                                style="max-height: 200px; overflow: auto">
                                                <li>
                                                    <div class="row">
                                                        <div class="col d-flex align-items-center">
                                                            <a href="/profile?user=<%= 2%>">akaki </a>
                                                            <div style="margin-left: 3px"> challenged you:</div>
                                                            <a style="margin-left: 3px"
                                                               href="/profile?user=<%= 2%>">Quizz's
                                                                name</a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <button class="btn btn-success">accept</button>
                                                            <button class="btn btn-danger">Reject</button>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="row">
                                                        <div class="col d-flex align-items-center">
                                                            <a href="/profile?user=<%= 2%>">akaki </a>
                                                            <div style="margin-left: 3px"> challenged you:</div>
                                                            <a style="margin-left: 3px"
                                                               href="/profile?user=<%= 2%>">Quizz's
                                                                name</a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <button class="btn btn-success">accept</button>
                                                            <button class="btn btn-danger">Reject</button>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="uk-padding-small">
                                            <ul id="chatNotifications"class="uk-list container-fluid"
                                                style="max-height: 200px; overflow: auto">
                                                <%
                                                    HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
                                                    for(int id : notSeen.keySet()) {
                                                        User chatUser = usersDAO.getUserById(id);
                                                %>
                                                <li>
                                                    <div class="row">
                                                        <div class="col d-flex align-items-center">
                                                            <div>New message from</div>
                                                            <a style="margin-left: 3px" href=<%="/profile?myUser=" + chatUser.getId()%>><%=chatUser.getUsername()%></a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <a href=<%="/chat?chatWith="+chatUser.getId()%>> <button class="btn btn-primary">Open chat</button> </a>
                                                        </div>
                                                    </div>
                                                </li>
                                                <%
                                                    }
                                                %>

                                            </ul>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <a style="margin-right: 3px" href="/profile?user=<%= myUser.getId()%>"><%=  myUser.getUsername()%>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class=" menuBar text-light"
         style="height: 50px; border-style: solid; border-width: 2px; border-color: white; border-radius: 10px;">
        <div class="container row" style="width: 100%">
            <div class="col-9 mb-1 mb-lg-0  mt-1">
                <a href="/createQuiz">
                    <button class="btn btn-dark text-light">Create Quiz</button>
                </a>
                <a href="/randomQuiz">
                    <button class="btn btn-dark text-light">Random Quiz</button>
                </a>
                <a href="/chat">
                    <button class="btn btn-dark text-light">Chats</button>
                </a>
        </div>
            <form class="col d-flex mb-1 mb-lg-0 mt-1" role="search" method="get" action="/search">
                <div class="input-group input-group-sm mb-1 mt-1">
                    <input class="form-control me-2 bg-dark whitePlaceholder text-light" type="search"
                           placeholder="Search"
                           aria-label="Search"
                           name="search"
                           aria-describedby="inputGroup-sizing-sm">
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>