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
            crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="style.css">
    <title>Quiz Time</title>
</head>
<script>
    function fc() {
        const http = new XMLHttpRequest();
        const msg = document.getElementById("message").value;
        const url = "chat?sendTo=" + document.getElementById("sendInp").value + "&message=" + msg;
        http.onreadystatechange = function () {
            if (http.readyState === 4 && msg.trim() !== "") {
                $('#message').val('')
                $('#chat').append("<div class='uk-align-right bg-primary messageBox'><p class='messageParagraph'>" + msg + "</p></div>")
                $('.chatBox').scrollTop(function () {
                    return this.scrollHeight;
                });
            }
        }
        http.open("POST", url);
        http.send(null);
    }

    function getMessages() {
        $.get('notSeen', {chatWith: <%=request.getParameter("chatWith")%>}, (responseText) => {
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
        $.get('notSeenChat', {chatWith: <%=request.getParameter("chatWith")%>}, (responseText) => {
            let realStr = responseText.trim();
            let fr = [];
            fr = realStr.split('/');
            fr.splice(fr.length - 1, 1);
            if(realStr !== '') {
                console.log(realStr);
                fr.forEach(myFunction);

                function myFunction(str) {
                    let str1 = "";
                    let newInd = -1;
                    for (let i = 0; i < str.length; i++) {
                        if (str[i] === '$') {
                            newInd = i;
                            break;
                        }
                        str1 += str[i];
                    }
                    let id = parseInt(str1);
                    let numNotSeen = "";
                    for (let i = newInd + 1; i < str.length; i++) {
                        numNotSeen += str[i];
                    }
                    let fr = "friend" + id;
                    $('#' + fr).text(numNotSeen);
                }
            }
        });
    }
    function getChats() {
        $.get('getChats', {chatWith: <%=request.getParameter("chatWith")%>}, (responseText) => {
            if(responseText !== '') {
                console.log(responseText);
                $('#chatList').html(responseText);
                getMessages();
                getMessagesChat();
            }
        });
    }
    $(document).ready(function () {
        setInterval(getChats, 2000);
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
                    HashMap<Integer,ArrayList<String>> notSeenMessages = messagesDAO.getNotSeenMessage(myUser.getId());
                    ArrayList<Integer> interactors = messagesDAO.getInteractorsList(myUser.getId());
                    if (request.getParameter("chatWith") != null) {
                        chatId = Integer.parseInt(request.getParameter("chatWith"));
                        User myFriend = usersDAO.getUserById(chatId);
                        if (!interactors.contains(myFriend.getId())) {
                %>
                <div class="d-flex align-items-center" style="background-color: #3e4042;">
                    <a class="fullWidthList" href=<%="/chat?chatWith=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                    </a>
                    <div id=<%="friend" + myFriend.getId()%>></div>
                </div>
                <%
                        }
                    }
                    for (Integer person : interactors) {
                        User myFriend = usersDAO.getUserById(person);
                %>
                <li>
                    <div class="d-flex align-items-center" <%
                        if (request.getParameter("chatWith") != null
                                && myFriend.getId() == Integer.parseInt(request.getParameter("chatWith"))) {
                    %> style="background-color: #3e4042;" <%}%>>
                        <a class="fullWidthList"
                           href=<%="/chat?chatWith=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                            <div id=<%="friend" + myFriend.getId()%>>
                                <%
                                if(notSeenMessages.keySet().contains(myFriend.getId())){
                                    out.println(notSeenMessages.get(myFriend.getId()).size());
                                }%>
                            </div>
                        </a>
                    </div>
                </li>
                <%
                    }
                %>
            </ul>
        </div>

        <div class="col"></div>
        <div class="col-8 " style="max-height: 100%;">
            <div class="overflow-auto chatBox" style="height: 90%; border: darkgrey 1px solid; border-radius: 10px;">
                <div id="chat" class="container-fluid uk-padding-small">
                    <% if (request.getParameter("chatWith") != null) {
                        chatId = Integer.parseInt(request.getParameter("chatWith"));%>
                    <%
                        ArrayList<Message> messages = messagesDAO.getMessagesWith(myUser.getId(), chatId);
                        for (Message message : messages) {
                            if (message.getFrom() == chatId) {
                    %>
                    <div class="uk-align-left messageBox" style="background-color: #3e4042;">
                        <p class="messageParagraph"><%=message.getMessage()%>
                        </p>
                    </div>
                    <% } else { %>
                    <div class="uk-align-right bg-primary messageBox">
                        <p class="messageParagraph"><%=message.getMessage()%>
                        </p>
                    </div>
                    <% }
                    }
                    } %>
                </div>
            </div>
            <div class="mt-2" style="height: 10%;">
                <form class="d-flex row" onsubmit="fc(); return false;">
                    <div class="col input-group input-group-md">
                        <input type="hidden" name="sendTo" id="sendInp" value=<%=chatId%>>
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
        </div>
    </div>
</div>
</body>
</html>
