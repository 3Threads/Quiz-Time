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
<%@ page import="Types.Quiz" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.util.Date" %>
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
    AnnouncementsDAO announcementsDAO = (AnnouncementsDAO) application.getAttribute("announcementsDB");
    RatingsDAO ratingsDAO = (RatingsDAO) application.getAttribute("ratingsDB");
%>
<script>
    let haveRequestsFrom = [];
    let haveChallengeFrom = [];
    let haveChatsNotificationFrom = [];

    function requestConstructor(reqId, reqUsername, myId) {
        return "<li>\n <div class='row' id='request" + reqId + "'>\n <div class='col d-flex align-items-center'>\n<a href='/profile?user=" + reqId + "'>" + reqUsername + "</a>\n </div>\n<div class='col-auto'>\n<a title='Accept friend request'> <button onclick=\"requestAction(" + myId + "," + reqId + ", 'acceptRequest', " + reqId + ")\"  style='display: inline-block;'type='button'class='btn btn-outline-success notification-buttons'> <i class='bi bi-person-plus-fill'></i> Accept </button></a>\n<a title='Reject friend request'> <button onclick=\"requestAction(" + myId + "," + reqId + ", 'rejectRequest', " + reqId + ")\" style='display: inline-block;'type='button'class='btn btn-outline-danger notification-buttons'> <i class='bi bi-person-x-fill'></i> Reject </button> </a></div>\n</div>\n</li>\n";
    }

    function challengeConstructor(userId, userName, quizId, quizName) {
        return "<li>\n <div class=\"row\" id=\"challenge" + userId + "\">\n <div class=\"col d-flex align-items-center\">\n <a href=\"/profile?user=" + userId + "\">" + userName + "\n </a>\n <div style=\"margin-left: 3px\"> challenged you:</div>\n <a style=\"margin-left: 3px\"\n href=\"/quiz?quizId=" + quizId + "\">" + quizName + "\n </a>\n </div>\n <div class=\"col-auto\">\n <a title=\"Accept challenge\"> <button onclick=\"challengeAction(" + userId + ", 'acceptChallenge'," + userId + "," + quizId + ")\"\n  style=\"display: inline-block;\" type=\"button\" class=\"btn btn-outline-success notification-buttons\"><i class=\"bi bi-check-lg\"></i> Accept\n </button> <\a>\n <a title=\"Reject challenge\"> <button onclick=\"challengeAction(" + userId + ", 'rejectChallenge', " + userId + ", " + quizId + ")\"\n style=\"display: inline-block;\" type=\"button\" class=\"btn btn-outline-danger notification-buttons\"><i class=\"bi bi-x-lg\"></i> Reject\n </button> </a>\n </div>\n </div>\n </li>";
    }

    function chatConstructor(chatUserId, chatUserUsername) {
        return "<li>\n <div class=\"notification-name row\">\n <div class=\"col d-flex align-items-center\">\n<div>New message from</div>\n <a style=\"margin-left: 3px\" href=\"/profile?user=" + chatUserId + "\">" + chatUserUsername + "</a>\n </div>\n <div class=\"col-auto\">\n <a href=\"/chat?chatWith=" + chatUserId + "\"> <button style=\"display: inline-block;\" type=\"button\" class=\"btn btn-outline-primary notification-buttons\"> <i class=\"bi bi-envelope\"></i> Open </button> </a>\n </div>\n </div>\n </li>";
    }

    function requestAction(user1, user2, action, requestId) {
        $.post('notifications', {notification: 'request', user1: user1, user2: user2, action: action}, () => {
            $('#request' + requestId).remove();
            const index = haveRequestsFrom.indexOf(user2);
            haveRequestsFrom.splice(index, 1);
        });
    }

    function searchForArray(haystack, needle) {
        let i, j, current;
        for (i = 0; i < haystack.length; ++i) {
            if (needle.length === haystack[i].length) {
                current = haystack[i];
                for (j = 0; j < needle.length && needle[j] === current[j]; ++j) ;
                if (j === needle.length)
                    return i;
            }
        }
        return -1;
    }

    function challengeAction(user, action, challengeId, quizID) {
        $.post('notifications', {notification: 'challenge', userID: user, action: action, quizID: quizID}, () => {
            $('#challenge' + challengeId).remove();
            let index = searchForArray(haveChallengeFrom, [user, quizID]);
            haveChallengeFrom.splice(index, 1);
            if (action === 'acceptChallenge') {
                window.location.replace("/quiz?quizId=" + quizID);
            }
        });
    }
    let x = 0;
    function getNotifications() {
        $.get('notifications', (responseText) => {
            if (responseText.trim() === 'login') {
                $(location).attr('href', '/login');
                return;
            }
            let realStr = responseText.trim();
            notifi = realStr.split('$');
            if (realStr !== '') {
                if (notifi[0].trim() !== '') {
                    let challengeList = notifi[0].trim();
                    let challenges = challengeList.split("/");
                    challenges.forEach(challengeFunc);

                    function challengeFunc(challenge) {
                        let chall = challenge.trim();
                        let components;
                        components = chall.split("|");
                        if (searchForArray(haveChallengeFrom, [parseInt(components[0]), parseInt(components[2])]) === -1) {
                            haveChallengeFrom.push([parseInt(components[0]), parseInt(components[2])]);
                            $('#challengesList').append(challengeConstructor(components[0], components[1], components[2], components[3]));
                            $('#toastCont').append(toastContructor(++x, 'You Have Challenged on quiz '+ components[3] + ' From ' + components[1] + '!'));
                            var toast = new bootstrap.Toast($('#'+x));
                            toast.show();
                        }
                    }
                }
                if (notifi[1].trim() !== '') {
                    let chatList = notifi[1].trim();
                    let chats = chatList.split("/");
                    chats.forEach(chatFunc);

                    function chatFunc(chat) {
                        let ch = chat.trim();
                        let components = ch.split("|");
                        if (!haveChatsNotificationFrom.includes(parseInt(components[0]))) {
                            haveChatsNotificationFrom.push(parseInt(components[0]));
                            $('#chatNotifications').append(chatConstructor(components[0], components[1]));
                            $('#toastCont').append(toastContructor(++x, 'You Have New Message From ' + components[1]));
                            var toast = new bootstrap.Toast($('#'+x));
                            toast.show();
                        }
                    }
                }
                if (notifi[2].trim() !== '') {
                    let requestsList = notifi[2].trim();
                    let requests = requestsList.split("/");
                    requests.forEach(requestFunc);

                    function requestFunc(request) {
                        let req = request.trim();
                        let components = req.split("|");
                        if (!haveRequestsFrom.includes(parseInt(components[0]))) {
                            haveRequestsFrom.push(parseInt(components[0]));
                            $('#requestsList').append(requestConstructor(components[0], components[1], <%=myUser.getId()%>));
                            $('#toastCont').append(toastContructor(++x, 'You Have New Friend Request From ' + components[1]));
                            var toast = new bootstrap.Toast($('#'+x));
                            toast.show();
                        }
                    }
                }
                if (notifi[0].trim() !== '' || notifi[1].trim() !== '' || notifi[2].trim() !== '') shake();
            }
        });
    }

    function shake() {
        const $animate = $('.animate');
        $animate.addClass('uk-animation-shake');

        $animate.one('webkitAnimationEnd oanimationend msAnimationEnd animationend',
            function () {
                $animate.removeClass('uk-animation-shake');
            });
    }
    function toastContructor(id, msg) {
        return '<div id='+id+' class="toast GFG1 bg-primary fade show" role="alert" aria-live="assertive" aria-atomic="true">\n' +
            '            <div class="toast-header">\n' +
            '                <strong class="me-auto">Notification</strong>\n' +
            '                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Закрыть"></button>\n' +
            '            </div>\n' +
            '            <div class="toast-body">\n' +
            '                '+msg+'\n' +
            '            </div>\n' +
            '        </div>';
    }

    $(document).ready(function () {
        setInterval(getNotifications, 2000);
        setInterval(function() {
            var toastElList = [].slice.call(document.querySelectorAll('.hide'))
            toastElList.forEach(toast => toast.remove())
        }, 3000);
    });
</script>
<body class="bg-dark text-light">
<div id="toastCont" class="toast-container position-absolute bottom-0 end-0 p-3">
</div>
<div class="container">
    <div class="row">
        <div class="col">
            <a href="/homePage"><img src="images/logo.png" class="img-fluid logo" alt="..."></a>
        </div>
        <div class="col-2">
        </div>
        <div class="col d-flex align-items-end">
            <div class="container">
                <div class="d-flex flex-row-reverse align-items-center mb-2">
                    <a class="table_name" style="margin-left: 5px" href="/logout">
                        Log Out
                    </a>
                    <div class="table_name">|</div>
                    <a href="#modal-notifications" class="mt-1" uk-toggle>
                        <div class="animate table_name">
                            <i class="bi bi-bell-fill mt-1"
                               style="margin-right: 5px"></i>
                        </div>
                    </a>
                    <div id="modal-notifications" uk-modal>
                        <div class="uk-modal-dialog bg-dark">
                            <button class="uk-modal-close-default" type="button" uk-close></button>
                            <div class="uk-modal-header bg-dark">
                                <h2 class="notification-title uk-modal-title">Notifications</h2>
                            </div>
                            <div class="uk-modal-body">
                                <ul class="uk-flex-left" data-uk-tab="{connect:'#notification tab'}">
                                    <li><a class="notification-titles" style="color: white" href="">Friend Requests</a>
                                    </li>
                                    <li><a class="notification-titles" style="color: white" href="">Challenges</a></li>
                                    <li><a class="notification-titles" style="color: white" href="">Messages</a></li>
                                </ul>

                                <ul id="notification tab" class="uk-switcher uk-margin">

                                    <%--Friend requests--%>
                                    <li>
                                        <div class="notification-name uk-padding-small">
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
                                                            <a href=<%="/profile?user=" + reqUserInfo.getId()%>>
                                                                <%=reqUserInfo.getUsername()%>
                                                            </a>
                                                            <script>
                                                                haveRequestsFrom.push(<%=reqUserInfo.getId()%>);
                                                            </script>
                                                        </div>
                                                        <div class="col-auto">
                                                            <a title="Accept friend request">
                                                                <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'acceptRequest', <%=requestId%>)"
                                                                        style="display: inline-block;"
                                                                        type="button"
                                                                        class="btn btn-outline-success notification-buttons">
                                                                    <i class="bi bi-person-plus-fill"></i> Accept
                                                                </button>
                                                            </a>

                                                            <a title="Reject friend request">
                                                                <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'rejectRequest', <%=requestId%>)"
                                                                        style="display: inline-block;"
                                                                        type="button"
                                                                        class="btn btn-outline-danger notification-buttons">
                                                                    <i class="bi bi-person-x-fill"></i> Reject
                                                                </button>
                                                            </a>

                                                            <%--                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'acceptRequest', <%=requestId%>)"--%>
                                                            <%--                                                                    class="notification-buttons btn btn-success">Accept--%>
                                                            <%--                                                            </button>--%>
                                                            <%--                                                            <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'rejectRequest', <%=requestId%>)"--%>
                                                            <%--                                                                    class="notification-buttons btn btn-danger">Reject--%>
                                                            <%--                                                            </button>--%>
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

                                    <%--Challenges--%>
                                    <li>
                                        <div class="notification-name uk-padding-small">
                                            <ul id="challengesList" class="uk-list container-fluid "
                                                style="max-height: 200px; overflow: auto">

                                                <%
                                                    ArrayList<Challenge> challenges = challengesDAO.getChallenges(myUser.getId());
                                                    int challId = 1;
                                                    for (Challenge challenge : challenges) {
                                                        User challUserInfo = usersDAO.getUserById(challenge.getUserId());

                                                %>
                                                <li>
                                                    <script>
                                                        haveChallengeFrom.push([<%=challUserInfo.getId()%>, <%=challenge.getQuizId()%>]);
                                                    </script>
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
                                                            <a title="Accept challenge">
                                                                <button onclick="challengeAction(<%=challUserInfo.getId()%>, 'acceptChallenge', <%=challId%>, <%=challenge.getQuizId()%>)"
                                                                        style="display: inline-block;"
                                                                        type="button"
                                                                        class="btn btn-outline-success notification-buttons">
                                                                    <i class="bi bi-check-lg"></i>
                                                                    Accept
                                                                </button>
                                                            </a>

                                                            <a title="Reject challenge">
                                                                <button onclick="challengeAction(<%=challUserInfo.getId()%>, 'rejectChallenge', <%=challId%>, <%=challenge.getQuizId()%>)"
                                                                        style="display: inline-block;"
                                                                        type="button"
                                                                        class="btn btn-outline-danger notification-buttons">
                                                                    <i class="bi bi-x-lg"></i>
                                                                    Reject
                                                                </button>
                                                            </a>

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

                                    <%--Chat--%>
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
                                                    <script>
                                                        haveChatsNotificationFrom.push(<%=chatUser.getId()%>);
                                                    </script>
                                                    <div class="notification-name row">
                                                        <div class="col d-flex align-items-center">
                                                            <div>New message from</div>
                                                            <a style="margin-left: 3px"
                                                               href=<%="/profile?user=" + chatUser.getId()%>><%=chatUser.getUsername()%>
                                                            </a>

                                                        </div>
                                                        <div class="col-auto">
                                                            <a title="Open chat" href=<%="/chat?chatWith=" + chatUser.getId()%>>
                                                                <button style="display: inline-block;"
                                                                        type="button"
                                                                        class="btn btn-outline-primary notification-buttons">
                                                                    <i class="bi bi-envelope"></i> Open
                                                                </button>
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
                    <div class="table_name">

                        <a style="margin-right: 3px"
                           href="/profile?user=<%= myUser.getId()%>"><%=  myUser.getUsername()%>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="menuBar text-light ">
        <div class="container row d-flex align-items-center" style="width: 100%">
            <div class="col-9">
                <a href="/createQuiz">
                    <button class="buttons btn btn-dark text-light">Create Quiz</button>
                </a>
                <%
                    ArrayList<Quiz> allQuizzesList = quizzesDAO.getPopularQuizzes();
                    Random rand = new Random(new Date().getTime());
                    int randInd = rand.nextInt(allQuizzesList.size());
                    int randQuizId = allQuizzesList.get(randInd).getQuizId();
                %>
                <a href="/quiz?quizId=<%=randQuizId%>">
                    <button class="buttons btn btn-dark text-light">Random Quiz</button>
                </a>
                <a href="/chat">
                    <button class="buttons btn btn-dark text-light">Chats</button>
                </a>
                <a href="/quizzesList">
                    <button class="buttons btn btn-dark text-light">Quizzes</button>
                </a>
            </div>
            <form class="nav-bar col" role="search" method="get" action="/search">
                <div class="input-group input-group-sm mb-1 mt-1">
                    <input class="rounded search-nav-bar form-control me-2 bg-dark whitePlaceholder text-light"
                           type="search"
                           placeholder="Search"
                           aria-label="Search"
                           name="search"
                           id="search"
                           aria-describedby="inputGroup-sizing-sm" required>
                    <button class="uk-search-icon-flip" uk-search-icon></button>
                </div>
                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <li><a class="dropdown-item" href="#">Action</a></li>
                    <li><a class="dropdown-item" href="#">Another action</a></li>
                    <li><hr class="dropdown-divider" /></li>
                    <li><a class="dropdown-item" href="#">Something else here</a></li>
                </ul>
            </form>
        </div>
    </div>
</div>
</body>
</html>