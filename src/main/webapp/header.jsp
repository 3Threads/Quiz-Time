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
<%@ page import="BusinessLogic.RankingSystem" %>
<html>
<head>
    <!-- UIkit CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/css/uikit.min.css"/>

    <!-- UIkit JS -->
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit-icons.min.js"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"/>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.7.0.js"
            integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM=" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"/>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>


    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>

    <link rel="icon" href="images/icon.png" type="image/x-icon"/>
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

    function requestConstructor(reqId, reqUsername, reqRank, myId) {
        return "<li>\n <div class='row' id='request" + reqId + "'>\n <div class='col d-flex align-items-center'>\n<a class='rank-" + reqRank + "' href='/profile?user=" + reqId + "'>" + reqUsername + "</a>\n </div>\n<div class='col-auto'>\n<a title='Accept friend request'> <button onclick=\"requestAction(" + myId + "," + reqId + ", 'acceptRequest', " + reqId + ")\" style='display: inline-block;' type='button' class='btn btn-success notification-buttons'> <i class='bi bi-person-plus-fill'></i> Accept </button></a>\n<a title='Reject friend request'> <button onclick=\"requestAction(" + myId + "," + reqId + ", 'rejectRequest', " + reqId + ")\" style='display: inline-block;' type='button' class='btn btn-danger notification-buttons'> <i class='bi bi-person-x-fill'></i> Reject </button> </a></div>\n</div>\n</li>\n";
    }

    function challengeConstructor(userId, userName, userRank, quizId, quizName) {
        return "<li>\n <div class=\"row\" id=\"challenge" + userId + "\">\n <div class=\"col d-flex align-items-center\">\n <a class='rank-" + userRank + "' href=\"/profile?user=" + userId + "\">" + userName + "\n </a>\n <div style=\"margin-left: 3px\"> challenged you:</div>\n <a style=\"margin-left: 3px\"\n href=\"/quiz?quizId=" + quizId + "\">" + quizName + "\n </a>\n </div>\n <div class=\"col-auto\">\n <a title=\"Accept challenge\"> <button onclick=\"challengeAction(" + userId + ", 'acceptChallenge'," + userId + "," + quizId + ")\"\n  style=\"display: inline-block;\" type=\"button\" class=\"btn btn-success notification-buttons\"><i class=\"bi bi-check-lg\"></i> Accept\n </button> <\a>\n <a title=\"Reject challenge\"> <button onclick=\"challengeAction(" + userId + ", 'rejectChallenge', " + userId + ", " + quizId + ")\"\n style=\"display: inline-block;\" type=\"button\" class=\"btn btn-danger notification-buttons\"><i class=\"bi bi-x-lg\"></i> Reject\n </button> </a>\n </div>\n </div>\n </li>";
    }

    function chatConstructor(chatUserId, chatUserRank, chatUserUsername) {
        return "<li>\n <div class=\"notification-name row\">\n <div class=\"col d-flex align-items-center\">\n<div>New message from</div>\n <a class='rank-" + chatUserRank + "' style=\"margin-left: 3px\" href=\"/profile?user=" + chatUserId + "\">" + chatUserUsername + "</a>\n </div>\n <div class=\"col-auto\">\n <a href=\"/chat?chatWith=" + chatUserId + "\"> <button style=\"display: inline-block;\" type=\"button\" class=\"btn btn-primary notification-buttons\"> <i class=\"bi bi-envelope\"></i> Open </button> </a>\n </div>\n </div>\n </li>";
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
                            $('#challengesList').append(challengeConstructor(components[0], components[1], components[4], components[2], components[3]));
                            $('#toastCont').append(toastContructor(++x, 'You Have Challenged on quiz ' + components[3] + ' From ' + components[1] + '!'));
                            var toast = new bootstrap.Toast($('#' + x));
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
                            $('#chatNotifications').append(chatConstructor(components[0], components[2], components[1]));
                            $('#toastCont').append(toastContructor(++x, 'You Have New Message From ' + components[1]));
                            var toast = new bootstrap.Toast($('#' + x));
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
                            $('#requestsList').append(requestConstructor(components[0], components[1], components[2], <%=myUser.getId()%>));
                            $('#toastCont').append(toastContructor(++x, 'You Have New Friend Request From ' + components[1]));
                            var toast = new bootstrap.Toast($('#' + x));
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
        return '<div id=' + id + ' class="toast GFG1 bg-primary fade show" role="alert" aria-live="assertive" aria-atomic="true">\n' +
            '            <div class="toast-header">\n' +
            '                <strong class="me-auto">Notification</strong>\n' +
            '                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Закрыть"></button>\n' +
            '            </div>\n' +
            '            <div class="toast-body">\n' +
            '                ' + msg + '\n' +
            '            </div>\n' +
            '        </div>';
    }

    $(document).ready(function () {
        setInterval(getNotifications, 2000);
        setInterval(function () {
            var toastElList = [].slice.call(document.querySelectorAll('.hide'))
            toastElList.forEach(toast => toast.remove())
        }, 3000);
    });
</script>
<body class="text-light bg-dark">
<div id="toastCont" class="toast-container position-absolute bottom-0 end-0 p-3">
</div>
<div class="container-fluid top-head fixed-top back-color">
    <div class="row">
        <div class="col-auto">
            <a href="/homePage"><img src="/images/icon.png" alt="logo"></a>
        </div>
        <div class="col">
            <div class="row">
                <div class="col d-flex align-items-end">
                    <div class="container-fluid">
                        <div class="d-flex flex-row-reverse align-items-center mb-1">
                            <a class="table_name" style="margin-left: 5px" href="/logout">
                                Log Out
                            </a>
                            <div class="table_name">|</div>
                            <a href="#modal-notifications" uk-toggle>
                                <div class="animate table_name" style="margin-right: 3px">
                                    <i class="bi bi-bell-fill"
                                       style="margin-right: 1px"></i>
                                </div>
                            </a>
                            <div id="modal-notifications" class="main" uk-modal>
                                <div class="uk-modal-dialog bg-dark">
                                    <button class="uk-modal-close-default" type="button" uk-close></button>
                                    <div class="uk-modal-header bg-dark">
                                        <h2 class="notification-title uk-modal-title">Notifications</h2>
                                    </div>
                                    <div class="uk-modal-body">
                                        <ul class="uk-flex-left" data-uk-tab="{connect:'#notification tab'}">
                                            <li><a class="notification-titles" style="color: white" href="">Friend
                                                Requests</a>
                                            </li>
                                            <li><a class="notification-titles" style="color: white"
                                                   href="">Challenges</a>
                                            </li>
                                            <li><a class="notification-titles" style="color: white" href="">Messages</a>
                                            </li>
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
                                                                    <a class="rank-<%=RankingSystem.countRank(reqUserInfo.getScore())%>"
                                                                       href=<%="/profile?user=" + reqUserInfo.getId()%>>
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
                                                                                class="btn btn-success notification-buttons">
                                                                            <i class="bi bi-person-plus-fill"></i>
                                                                            Accept
                                                                        </button>
                                                                    </a>

                                                                    <a title="Reject friend request">
                                                                        <button onclick="requestAction(<%=myUser.getId()%>,<%=reqUserInfo.getId()%>, 'rejectRequest', <%=requestId%>)"
                                                                                style="display: inline-block;"
                                                                                type="button"
                                                                                class="btn btn-danger notification-buttons">
                                                                            <i class="bi bi-person-x-fill"></i> Reject
                                                                        </button>
                                                                    </a>
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
                                                                    <a class="rank-<%=RankingSystem.countRank(challUserInfo.getScore())%>"
                                                                       href=<%= "/profile?user=" + challUserInfo.getId()%>><%=challUserInfo.getUsername()%>
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
                                                                                class="btn btn-success notification-buttons">
                                                                            <i class="bi bi-check-lg"></i>
                                                                            Accept
                                                                        </button>
                                                                    </a>

                                                                    <a title="Reject challenge">
                                                                        <button onclick="challengeAction(<%=challUserInfo.getId()%>, 'rejectChallenge', <%=challId%>, <%=challenge.getQuizId()%>)"
                                                                                style="display: inline-block;"
                                                                                type="button"
                                                                                class="btn btn-danger notification-buttons">
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
                                                                    <a class="rank-<%=RankingSystem.countRank(chatUser.getScore())%>"
                                                                       style="margin-left: 3px;"
                                                                       href=<%="/profile?user=" + chatUser.getId()%>><%=chatUser.getUsername()%>
                                                                    </a>

                                                                </div>
                                                                <div class="col-auto">
                                                                    <a title="Open chat"
                                                                       href=<%="/chat?chatWith=" + chatUser.getId()%>>
                                                                        <button style="display: inline-block;"
                                                                                type="button"
                                                                                class="btn btn-primary notification-buttons">
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

                                <a class="rank-<%=RankingSystem.countRank(myUser.getScore())%>"
                                   style="margin-right: 3px"
                                   href="/profile?user=<%= myUser.getId()%>"><%=  myUser.getUsername()%>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <hr style="margin: 0">

            <nav class="navbar navbar-expand-lg navbar-dark text-light back-color">
                <div class="container-fluid" style="padding-left: 0; padding-right: 0">
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="col navbar-nav">
                            <%
                                ArrayList<Quiz> allQuizzesList = quizzesDAO.getPopularQuizzes();
                                Random rand = new Random(new Date().getTime());
                                int randInd = rand.nextInt(allQuizzesList.size());
                                int randQuizId = allQuizzesList.get(randInd).getQuizId();
                            %>

                            <li class="nav-item" style="margin-left:3px; margin-top: 3px; margin-bottom: 3px">
                                <a href="/createQuiz" class="buttons btn text-light back-color">
                                    Create Quiz
                                </a>
                            </li>
                            <li class="nav-item" style="margin-left:3px; margin-top: 3px; margin-bottom: 3px">
                                <a href="/quiz?quizId=<%=randQuizId%>" class="buttons btn text-light back-color">
                                    Random Quiz
                                </a>
                            </li>
                            <li class="nav-item" style="margin-left:3px; margin-top: 3px; margin-bottom: 3px">
                                <a href="/chat" class="buttons btn text-light back-color">
                                    Chats
                                </a>
                            </li>
                            <li class="nav-item" style="margin-left:3px; margin-top: 3px; margin-bottom: 3px">
                                <a href="/quizzesList" class="buttons btn text-light back-color">
                                    Quizzes
                                </a>
                            </li>
                        </ul>
                    </div>
                    <form class="d-flex" style="padding-left:12px" role="search" method="get" action="/search">
                        <input class="form-control me-2 whitePlaceholder text-light header-search"
                               style="background-color: #1d1f24; "
                               type="search"
                               placeholder="Search"
                               aria-label="Search"
                               name="search"
                               id="search"
                               aria-describedby="inputGroup-sizing-sm" required>
                    </form>
                </div>
            </nav>
        </div>
    </div>
</div>

</body>
</html>