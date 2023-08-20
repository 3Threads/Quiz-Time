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
                        $('#requestsList').append(requestConstructor(components[0], components[1], components[2], components[3]));
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
$(document).ready(function () {
    getNotifications();
    setInterval(getNotifications, 2000);
});