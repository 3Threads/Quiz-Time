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
<%@ page import="Types.Challenge" %>
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
            crossorigin="anonymous">
    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<%
    User myUser = (User) session.getAttribute("userInfo");
    UsersDAO usersDAO = (UsersDAO) application.getAttribute("usersDB");
    FriendsDAO friendsDAO = (FriendsDAO) application.getAttribute("friendsDB");
    ChallengesDAO challengesDAO = (ChallengesDAO) application.getAttribute("challengesDB");
    MessagesDAO messagesDAO = (MessagesDAO) application.getAttribute("messagesDB");
    QuizzesDAO quizzesDAO = (QuizzesDAO) application.getAttribute("quizzesDB");
    ResultsDAO resultsDAO = (ResultsDAO) application.getAttribute("resultsDB");
    QuestionsDAO questionsDAO = (QuestionsDAO) application.getAttribute("questionsDB");
%>
<script>
    function requestConstructor(reqId, reqUsername, myId) {
        return "<li>\n <div class='row' id='request" + reqId + "'>\n <div class='col d-flex align-items-center'>\n<a href='/profile?user=" + reqId + "'>" + reqUsername + "</a>\n </div>\n<div class='col-auto'>\n<button onclick=\"requestAction(" + myId + "," + reqId + ", 'acceptRequest', " + reqId + ")\" class='btn btn-success'>Accept</button>\n<button onclick=\"requestAction(" + myId + "," + reqId + ", 'rejectRequest', " + reqId + ")\" class='btn btn-danger'>Reject</button>\n</div>\n</div>\n</li>\n";
    }

    function challengeConstructor(userId, userName, quizId, quizName) {
        return "<li>\n <div class=\"row\" id=\"challenge" + userId+"\">\n <div class=\"col d-flex align-items-center\">\n <a href=\"/profile?user=" + userId+"\">"+ userName   +"\n </a>\n <div style=\"margin-left: 3px\"> challenged you:</div>\n <a style=\"margin-left: 3px\"\n href=\"/quiz?quizId=" + quizId+ "\">" + quizName+ "\n </a>\n </div>\n <div class=\"col-auto\">\n <button onclick=\"challengeAction("+userId+", 'acceptChallenge',"+ userId+ "," + quizId+")\"\n class=\"btn btn-success\">Accept\n </button>\n <button onclick=\"challengeAction("+userId+", 'rejectChallenge', "+ userId+ ", " + quizId+")\"\n class=\"btn btn-danger\">Reject\n </button>\n </div>\n </div>\n </li>";
    }

    function chatConstructor(chatUserId, chatUserUsername) {
        return "<li>\n <div class=\"row\">\n <div class=\"col d-flex align-items-center\">\n<div>New message from</div>\n <a style=\"margin-left: 3px\" href=\"/profile?user=" + chatUserId + "\">" + chatUserUsername + "</a>\n </div>\n <div class=\"col-auto\">\n <a href=\"/chat?chatWith=" + chatUserId + "\"> <button class=\"btn btn-primary\">Open chat</button> </a>\n </div>\n </div>\n </li>";
    }

    function requestAction(user1, user2, action, requestId) {
        $.post('notifications', {notification: 'request', user1: user1, user2: user2, action: action}, () => {
            $('#request' + requestId).remove();
        });
    }

    function challengeAction(user, action, challengeId, quizID) {
        $.post('notifications', {notification: 'challenge', userID: user, action: action, quizID: quizID}, () => {
            $('#challenge' + challengeId).remove();
            if (action === 'acceptChallenge') {
                window.location.replace("/quiz?quizId=" + quizID);
            }
        });
    }
    function getNotifications() {
        $.get('notifications', (responseText) => {
            let realStr = responseText.trim();
            let notifi;
            notifi = realStr.split('$');
            if (realStr !== '') {
                if (notifi[0].trim() !== '') {
                    let challengeList = notifi[0].trim();
                    let challenges = challengeList.split("/");
                    $('#challengesList').html('');
                    challenges.forEach(challengeFunc);

                    function challengeFunc(challenge) {
                        let chall = challenge.trim();
                        let components;
                        components = chall.split("|");
                        $('#challengesList').append(challengeConstructor(components[0], components[1], components[2], components[3]));
                    }
                }
                if (notifi[1].trim() !== '') {
                    let chatList = notifi[1].trim();
                    let chats = chatList.split("/");
                    $('#chatNotifications').html('');
                    chats.forEach(chatFunc);

                    function chatFunc(chat) {
                            let ch = chat.trim();
                            let components = ch.split("|");
                            $('#chatNotifications').append(chatConstructor(components[0], components[1]));
                    }
                }
                if (notifi[2].trim() !== '') {
                    let requestsList = notifi[2].trim();
                    let requests = requestsList.split("/");
                    $('#requestsList').html('');
                    requests.forEach(requestFunc);

                    function requestFunc(request) {
                        let req = request.trim();
                        let components = req.split("|");
                        $('#requestsList').append(requestConstructor(components[0], components[1], <%=myUser.getId()%>));
                    }
                }
                if(notifi[0].trim() !== '' || notifi[1].trim() !== '' || notifi[2].trim() !== '') shake();
            }
        });
    }

    function shake() {
        const $animate = $('.animate');
        $animate.addClass('uk-animation-shake');

        $animate.one('webkitAnimationEnd oanimationend msAnimationEnd animationend',
            function() {
                $animate.removeClass('uk-animation-shake');
            });
    }

    $(document).ready(function () {
        setInterval(getNotifications, 5000);
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
                    <a href="#modal-notifications" class="mt-1" uk-toggle>
                        <div class="animate">
                        <i class="bi bi-bell-fill mt-1"
                                                                             style="margin-right: 5px"></i>
                        </div>
                    </a>
                    <div id="modal-notifications" uk-modal>
                        <div class="uk-modal-dialog bg-dark">
                            <button class="uk-modal-close-default" type="button" uk-close></button>
                            <div class="uk-modal-header bg-dark">
                                <h2 class="uk-modal-title">Notifications</h2>
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
                                                    <div class="row" id=<%="request" + requestId%>>
                                                        <div class="col d-flex align-items-center">
                                                            <a href=<%="/profile?user=" + reqUserInfo.getId()%>><%=reqUserInfo.getUsername()%>
                                                            </a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'acceptRequest', <%=requestId%>)"
                                                                    class="btn btn-success">accept
                                                            </button>
                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'rejectRequest', <%=requestId%>)"
                                                                    class="btn btn-danger">Reject
                                                            </button>
                                                        </div>
                                                    </div>
                                                </li>
                                                <%
                                                        requestId++;
                                                    }
                                                %>
                                            </ul>

                                        </div>
                                    </li>
                                    <li>
                                        <div id="challengesList" class="uk-padding-small">
                                            <ul class="uk-list container-fluid "
                                                style="max-height: 200px; overflow: auto">


                                                <%
                                                    ArrayList<Challenge> challenges = challengesDAO.getChallenges(myUser.getId());
                                                    int challId = 1;
                                                    for (Challenge challenge : challenges) {
                                                        User challUserInfo = usersDAO.getUserById(challenge.getUserId());

                                                %>
                                                <li>

                                                    <div class="row" id="<%="challenge" + challId%>">
                                                        <div class="col d-flex align-items-center">
                                                            <a href=<%= "/profile?user=" + challUserInfo.getId()%>><%=challUserInfo.getUsername()%>
                                                            </a>
                                                            <div style="margin-left: 3px"> challenged you:</div>
                                                            <a style="margin-left: 3px"
                                                               href=<%= "/quiz?quizId=" + challenge.getQuizId()%>><%=quizzesDAO.getQuizInfo(challenge.getQuizId()).getQuizName()%>
                                                            </a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <button onclick="challengeAction(<%=challUserInfo.getId()%>, 'acceptChallenge', <%=challId%>, <%=challenge.getQuizId()%>)"
                                                                    class="btn btn-success">Accept
                                                            </button>
                                                            <button onclick="challengeAction(<%=challUserInfo.getId()%>, 'rejectChallenge', <%=challId%>, <%=challenge.getQuizId()%>)"
                                                                    class="btn btn-danger">Reject
                                                            </button>
                                                        </div>
                                                    </div>

                                                </li>
                                                <%
                                                        challId++;
                                                    }
                                                %>

                                            </ul>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="uk-padding-small">
                                            <ul id="chatNotifications" class="uk-list container-fluid"
                                                style="max-height: 200px; overflow: auto">
                                                <%
                                                    HashMap<Integer, ArrayList<String>> notSeen = messagesDAO.getNotSeenMessage(myUser.getId());
                                                    for (int id : notSeen.keySet()) {
                                                        User chatUser = usersDAO.getUserById(id);
                                                %>
                                                <li>
                                                    <div class="row">
                                                        <div class="col d-flex align-items-center">
                                                            <div>New message from</div>
                                                            <a style="margin-left: 3px"
                                                               href=<%="/profile?user=" + chatUser.getId()%>><%=chatUser.getUsername()%>
                                                            </a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <a href=<%="/chat?chatWith=" + chatUser.getId()%>>
                                                                <button class="btn btn-primary">Open chat</button>
                                                            </a>
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