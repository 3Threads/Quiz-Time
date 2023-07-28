<%@ page import="Types.Message" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<script>
    function fc() {
        $.post('chat', {
            sendTo: document.getElementById("sendInp").value,
            message: document.getElementById("message").value
        }, (responseText) => {
            if (responseText.trim() == 'login') {
                window.location.replace("/login");
                return;
            }
            if (document.getElementById("message").value !== "") {
                let fr = "chat" + document.getElementById("sendInp").value;
                $('#' + fr).remove();
                $('#chatList').prepend(chatWithConstructor(document.getElementById("sendInp").value, document.getElementById("name").value));
                let msg = document.getElementById("message").value;
                $('#message').val('')
                $('#chat').append("<div class='uk-align-right bg-primary messageBox'><p class='messageParagraph'>" + msg + "</p></div>")
                $('.chatBox').scrollTop(function () {
                    return this.scrollHeight;
                });
            }
        });
    }

    function getMessages() {
        $.get('notSeen', {chatWith: <%=request.getParameter("chatWith")%>, action: "currentChat"}, (responseText) => {
            if (responseText !== '') {
                const chatBox = $(".chatBox");
                const shouldScroll = chatBox[0].scrollHeight - chatBox.scrollTop() - chatBox.outerHeight() < 1;
                $('#chat').html($('#chat').html() + responseText);
                if (responseText !== "" && shouldScroll) {
                    chatBox.scrollTop(function () {
                        return this.scrollHeight;
                    });
                }
            }
        });
    }

    function getMessagesChat() {
        $.get('notSeen', {
            chatWith: <%=request.getParameter("chatWith")%>,
            action: "notCurrentChat"
        }, (responseText) => {
            let realStr = responseText.trim();
            let fr = [];
            fr = realStr.split('/');
            fr.splice(fr.length - 1, 1);
            if (realStr !== '') {
                fr.forEach(myFunction);

                function myFunction(str) {
                    let params = str.trim().split("$");
                    let fr = "friend" + params[0];
                    let cur = parseInt(document.getElementById(fr).textContent);
                    console.log(cur);
                    if (cur !== parseInt(params[1])) {
                        let kk = "chat" + params[0];
                        $('#' + kk).remove();
                        $('#chatList').prepend(chatsConstructor(params[0], params[2]));
                        $('#' + fr).text(params[1]);
                    }
                }
            }
        });
    }

    let curChats = [];

    function chatWithConstructor(myFriendId, myFriendUsername) {
        return "<li id=\"chat" + myFriendId + "\"><div class=\"d-flex align-items-center\" style=\"background-color: #3e4042;\">\n <a class=\"fullWidthList\" href=\"/chat?chatWith=" + myFriendId + "\">" + myFriendUsername + "\n<div id=\"friend" + myFriendId + "\"> </div>\n </a>\n </div></li>"
    }

    function chatsConstructor(myFriendId, myFriendUsername) {
        return "<li id=\"chat" + myFriendId + "\"><div class=\"d-flex align-items-center\">\n <a class=\"fullWidthList\" href=\"/chat?chatWith=" + myFriendId + "\">" + myFriendUsername + "\n<div id=\"friend" + myFriendId + "\"> </div>\n </a>\n </div></li>"
    }

    function getChats() {
        $.get('getChats', {chatWith: <%=request.getParameter("chatWith")%>}, (responseText) => {
            if (responseText.trim() !== '') {
                if (curChats.length === 0) $('#chatList').html('');
                let fr = responseText.trim().split('$');
                fr.forEach(chatFunc);

                function chatFunc(str) {
                    if (str != '') {
                        let param = str.trim().split('/');
                        console.log(curChats);
                        if (!curChats.includes(parseInt(param[0]))) {
                            curChats.push(parseInt(param[0]));
                            if (parseInt(param[0]) === <%=request.getParameter("chatWith")%>) {
                                $('#chatList').append(chatWithConstructor(param[0], param[1]));
                            } else $('#chatList').append(chatsConstructor(param[0], param[1]))
                        }
                    }
                }

                getMessages();
                getMessagesChat();
            }
        });
    }

    $(document).ready(function () {
        setInterval(getChats, 5000);
        $(".chatBox").scrollTop(function () {
            return this.scrollHeight;
        });
    });
</script>
<body>
<%@include file="header.jsp" %>
<% Integer chatId = null;%>
<div class="container">

    <div class="row mt-3" style="height: 65%">
        <div class="col-3" style="height: 100%;">
            <ul id="chatList" class="uk-list container-fluid uk-padding-small overflow-auto"
                style="height:100%; border: darkgrey 1px solid; border-radius: 10px;">
                <%
                    HashMap<Integer, ArrayList<String>> notSeenMessages = messagesDAO.getNotSeenMessage(myUser.getId());
                    ArrayList<Integer> interactors = messagesDAO.getInteractorsList(myUser.getId());
                    if (request.getParameter("chatWith") != null) {
                        chatId = Integer.parseInt(request.getParameter("chatWith"));
                        User myFriend = usersDAO.getUserById(chatId);
                        if (!interactors.contains(myFriend.getId())) {
                %>
                <li id=<%="chat" + myFriend.getId()%>>
                    <script>
                        curChats.push(<%=myFriend.getId()%>)
                    </script>
                    <div class="d-flex align-items-center" style="background-color: #3e4042;">
                        <a class="fullWidthList"
                           href=<%="/chat?chatWith=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                        </a>
                        <div id=<%="friend" + myFriend.getId()%>></div>
                    </div>
                </li>
                <%
                        }
                    }
                    for (Integer person : interactors) {
                        User myFriend = usersDAO.getUserById(person);
                %>
                <li id=<%="chat" + myFriend.getId()%>>
                    <script>
                        console.log("pushing " + <%=myFriend.getId()%>);
                        curChats.push(<%=myFriend.getId()%>)
                    </script>
                    <div class="d-flex align-items-center" <%
                        if (request.getParameter("chatWith") != null
                                && myFriend.getId() == Integer.parseInt(request.getParameter("chatWith"))) {
                    %> style="background-color: #3e4042;" <%}%>>
                        <a class="fullWidthList"
                           href=<%="/chat?chatWith=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                            <div id=<%="friend" + myFriend.getId()%>>
                                <%
                                    if (notSeenMessages.keySet().contains(myFriend.getId())) {
                                        out.println(notSeenMessages.get(myFriend.getId()).size());
                                    }
                                %>
                            </div>
                        </a>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <% if (request.getParameter("chatWith") != null) {
            chatId = Integer.parseInt(request.getParameter("chatWith"));%>
        <div class="col"></div>
        <div class="col-8 " style="max-height: 100%;">
            <div class="overflow-auto chatBox" style="height: 90%; border: darkgrey 1px solid; border-radius: 10px;">
                <div id="chat" class="container-fluid uk-padding-small">
                    <%
                        ArrayList<Message> messages = messagesDAO.getMessagesWith(myUser.getId(), chatId);
                        for (Message message : messages) {
                            if (message.getFrom() == chatId) {
                    %>
                    <div class="uk-align-left messageBox" style="background-color: #3e4042;">
                        <p class="messageParagraph"><%=message.getMessage()%>
                        </p>
                    </div>
                    <%
                    } else {
                    %>
                    <div class="uk-align-right bg-primary messageBox">
                        <p class="messageParagraph"><%=message.getMessage()%>
                        </p>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
            <div class="mt-2" style="height: 10%;">
                <form class="d-flex row" onsubmit="fc(); return false;">
                    <div class="col input-group input-group-md">
                        <input type="hidden" name="sendTo" id="sendInp" value=<%=chatId%>>
                        <input type="hidden" name="sendTo" id="name"
                               value=<%=usersDAO.getUserById(chatId).getUsername()%>>
                        <input id="message" class="form-control bg-dark whitePlaceholder text-light input-md"
                               type="text"
                               placeholder="Input message"
                               name="message"/>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Send</button>
                    </div>
                </form>
            </div>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>
