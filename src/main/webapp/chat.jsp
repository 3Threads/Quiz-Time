<%@ page import="Types.Message" %>
<%@ page import="BusinessLogic.RankingSystem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="header.jsp" %>
</head>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<script>
    function fc() {
        $.post('chat', {
            sendTo: document.getElementById("sendInp").value,
            message: document.getElementById("message").value
        }, (responseText) => {
            if (responseText.trim() === 'login') {
                $(location).attr('href', '/login');
                return;
            }
            if (document.getElementById("message").value !== "") {
                let fr = "chat" + document.getElementById("sendInp").value;
                $('#' + fr).remove();
                $('#chatList').prepend(chatWithConstructor(document.getElementById("sendInp").value, document.getElementById("rank").value, document.getElementById("name").value));
                let msg = document.getElementById("message").value;
                msg = msg.replaceAll("<3", "<i class='bi bi-heart-fill' style='color: #C30000;'> </i>");
                msg = msg.replaceAll(":)", "<i class='bi bi-emoji-smile-fill' style='color: #FFD400;'> </i>");
                msg = msg.replaceAll(":D", "<i class='bi bi-emoji-laughing-fill' style='color: #FFD400;'> </i>");
                msg = msg.replaceAll(":|", "<i class='bi bi-emoji-neutral-fill' style='color: #FFD400;'> </i>");
                msg = msg.replaceAll(":(", "<i class='bi bi-emoji-frown-fill' style='color: #FFD400;'> </i>");
                $('#message').val('')
                $('#chat').append("<div class='row justify-content-end' style='margin-right: 1px;'> <div class='bg-primary messageBox'> <p class='messageParagraph'>" + msg + " </p> </div> </div>")
                $('.chatBox').scrollTop(function () {
                    return this.scrollHeight;
                });
            }
        });
    }

    function writeEmoji(ind) {
        const messageInp = $('#message');
        switch (ind) {
            case 0:
                messageInp.val(messageInp.val() + ":)")
                break
            case 1:
                messageInp.val(messageInp.val() + ":D")
                break
            case 2:
                messageInp.val(messageInp.val() + ":|")
                break
            case 3:
                messageInp.val(messageInp.val() + ":(")
                break
            case 4:
                messageInp.val(messageInp.val() + "<3")
                break
        }
    }

    function getMessages() {
        $.get('notSeen', {chatWith: <%=request.getParameter("chatWith")%>, action: "currentChat"}, (responseText) => {
            if (responseText !== '') {
                if (responseText.trim() === 'login') {
                    $(location).attr('href', '/login');
                    return;
                }
                const chatBox = $(".chatBox");
                const shouldScroll = chatBox[0].scrollHeight - chatBox.scrollTop() - chatBox.outerHeight() < 1;
                responseText = responseText.replaceAll("<3", "<i class='bi bi-heart-fill' style='color: #C30000;'> </i>");
                responseText = responseText.replaceAll(":)", "<i class='bi bi-emoji-smile-fill' style='color: #FFD400;'> </i>");
                responseText = responseText.replaceAll(":D", "<i class='bi bi-emoji-laughing-fill' style='color: #FFD400;'> </i>");
                responseText = responseText.replaceAll(":|", "<i class='bi bi-emoji-neutral-fill' style='color: #FFD400;'> </i>");
                responseText = responseText.replaceAll(":(", "<i class='bi bi-emoji-frown-fill' style='color: #FFD400;'> </i>");
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
            if (responseText.trim() === 'login') {
                $(location).attr('href', '/login');
                return;
            }
            let realStr = responseText.trim();
            let fr;
            fr = realStr.split('/');
            fr.splice(fr.length - 1, 1);
            if (realStr !== '') {
                fr.forEach(myFunction);

                function myFunction(str) {
                    let params = str.trim().split("$");
                    let fr = "friend" + params[0];
                    let cur = parseInt(document.getElementById(fr).textContent);
                    if (cur !== parseInt(params[1])) {
                        let kk = "chat" + params[0];
                        $('#' + kk).remove();
                        $('#chatList').prepend(chatsConstructor(params[0], params[2], params[3]));
                        $('#' + fr).text(params[1]);
                    }
                }
            }
        });
    }

    let curChats = [];

    function chatWithConstructor(myFriendId, myFriendRank, myFriendUsername) {
        return "<li id=\"chat" + myFriendId + "\"><div class=\"d-flex align-items-center\" style=\"background-color: #3e4042;\">\n <a class=\"fullWidthList rank-" + myFriendRank + "\" href=\"/chat?chatWith=" + myFriendId + "\">" + myFriendUsername + "\n<div id=\"friend" + myFriendId + "\"> </div>\n </a>\n </div></li>"
    }

    function chatsConstructor(myFriendId, myFriendUsername, myFriendRank) {
        return "<li id=\"chat" + myFriendId + "\"><div class=\"d-flex align-items-center\">\n <a class=\"fullWidthList rank-" + myFriendRank + "\" href=\"/chat?chatWith=" + myFriendId + "\">" + myFriendUsername + "\n<div id=\"friend" + myFriendId + "\"> </div>\n </a>\n </div></li>"
    }

    function getChats() {
        $.get('getChats', {chatWith: <%=request.getParameter("chatWith")%>}, (responseText) => {
            if (responseText.trim() !== '') {
                if (responseText.trim() === 'login') {
                    $(location).attr('href', '/login');
                    return;
                }
                if (curChats.length === 0) $('#chatList').html('');
                let fr = responseText.trim().split('$');
                fr.forEach(chatFunc);

                function chatFunc(str) {
                    if (str !== '') {
                        let param = str.trim().split('/');
                        if (!curChats.includes(parseInt(param[0]))) {
                            curChats.push(parseInt(param[0]));
                            if (parseInt(param[0]) === <%=request.getParameter("chatWith")%>) {
                                $('#chatList').append(chatWithConstructor(param[0], param[1], param[2]));
                            } else $('#chatList').append(chatsConstructor(param[0], param[1], param[2]));
                        }
                    }
                }

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
<% Integer chatId;%>
<div class="container-fluid bg-dark main" style="min-height: 460px; padding-top: 0; height: calc(100vh - 95px);">
    <div class="row mt-1" style="height: 100%">
        <div class="col-3 back-color"
             style="height: 100%; padding-left: 0; padding-right: 0; position: fixed; border-right: 1px solid #666666;">
            <h3 style="padding-left:10px; padding-top:10px">
                Friends:
            </h3>
            <hr style="margin-top:0; margin-bottom: 0; margin-left:10px; margin-right:10px">
            <ul id="chatList" class="uk-list container-fluid uk-padding-small overflow-auto"
                style="height: calc(100% - 148px);">
                <%
                    HashMap<Integer, ArrayList<String>> notSeenMessages = messagesDAO.getNotSeenMessage(myUser.getId());
                    ArrayList<Integer> interactors = messagesDAO.getInteractorsList(myUser.getId());
                    String chatWith = request.getParameter("chatWith");
                    if (chatWith != null && !chatWith.trim().equals("")) {
                        chatId = Integer.parseInt(chatWith);
                        User myFriend = usersDAO.getUserById(chatId);
                        if (!interactors.contains(myFriend.getId())) {
                %>
                <li id=<%="chat" + myFriend.getId()%>>
                    <script>
                        curChats.push(<%=myFriend.getId()%>)
                    </script>
                    <div class="d-flex align-items-center" style="background-color: #3e4042;">
                        <a class="fullWidthList rank-<%=RankingSystem.countRank(myFriend.getScore())%>"
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
                        curChats.push(<%=myFriend.getId()%>)
                    </script>
                    <div class="d-flex align-items-center" <%
                        if (chatWith != null && !chatWith.trim().equals("") && myFriend.getId() == Integer.parseInt(chatWith)) {
                    %> style="background-color: #3e4042;" <%}%>>
                        <a class="fullWidthList rank-<%=RankingSystem.countRank(myFriend.getScore())%>"
                           href=<%="/chat?chatWith=" + myFriend.getId()%>><%=myFriend.getUsername()%>
                            <div id=<%="friend" + myFriend.getId()%>>
                                <%
                                    if (notSeenMessages.containsKey(myFriend.getId())) {
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
        <% if (chatWith != null && !chatWith.trim().equals("")) {
            chatId = Integer.parseInt(chatWith);%>
        <div class="col-4"></div>
        <div class="col-8" style="padding-top: 10px">
            <div class="overflow-auto chatBox"
                 style="height: calc(100% - 55px); border: darkgrey 1px solid; border-radius: 4px;">
                <div id="chat" class="container-fluid uk-padding-small">
                    <%
                        ArrayList<Message> messages = messagesDAO.getMessagesWith(myUser.getId(), chatId);
                        for (Message message : messages) {
                            String msg = message.getMessage();
                            msg = msg.replaceAll("<3", "<i class='bi bi-heart-fill' style='color: #C30000;'> </i>");
                            msg = msg.replaceAll(":\\)", "<i class='bi bi-emoji-smile-fill' style='color: #FFD400;'> </i>");
                            msg = msg.replaceAll(":D", "<i class='bi bi-emoji-laughing-fill' style='color: #FFD400;'> </i>");
                            msg = msg.replaceAll(":\\|", "<i class='bi bi-emoji-neutral-fill' style='color: #FFD400;'> </i>");
                            msg = msg.replaceAll(":\\(", "<i class='bi bi-emoji-frown-fill' style='color: #FFD400;'> </i>");
                            if (message.getFrom() == chatId) {
                    %>
                    <div class="row justify-content-start" style="margin-left: 1px;">
                        <div class="messageBox" style="background-color: #3e4042;">
                            <p class="messageParagraph"><%=msg%>
                            </p>
                        </div>
                    </div>
                    <%
                    } else {
                    %>
                    <div class="row justify-content-end" style="margin-right: 1px;">
                        <div class="bg-primary messageBox">
                            <p class="messageParagraph"><%=msg%>
                            </p>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
            <div class="mt-2">
                <form class="d-flex row" onsubmit="fc(); return false;">
                    <div class="col input-group input-group-md row">
                        <input type="hidden" name="sendTo" id="sendInp" value=<%=chatId%>>
                        <input type="hidden" name="sendTo" id="name"
                               value=<%=usersDAO.getUserById(chatId).getUsername()%>>
                        <input type="hidden" name="sendTo" id="rank"
                               value="<%=RankingSystem.countRank(usersDAO.getUserById(chatId).getScore())%>">
                        <input id="message" class="form-control bg-dark whitePlaceholder text-light input-md col"
                               type="text"
                               style="margin-left: 12px; border-radius:6px"
                               placeholder="Input message"
                               name="message"/>
                        <div class="uk-inline col-auto d-flex align-items-center"
                             style="margin-left: 10px; margin-right: 10px; padding: 0; ">
                            <a class="btn-primary" style="display: inline-block;">
                                <i class='bi bi-emoji-smile-fill' style='color: #FFD400;'></i>
                            </a>

                            <div class="uk-card uk-card-body bg-dark text-light"
                                 uk-drop="pos: top-center; mode:click"
                                 style="padding: 10px;">
                                <div class="row justify-content-center">
                                    <button type="button" class="col-auto text-center btn btn-dark"
                                            onclick="writeEmoji(0)"
                                    >
                                        <i class='bi bi-emoji-smile-fill' style='color: #FFD400;'> </i>
                                    </button>
                                    <button type="button" class="col-auto text-center btn btn-dark"
                                            onclick="writeEmoji(1)"
                                    >
                                        <i class='bi bi-emoji-laughing-fill' style='color: #FFD400;'> </i>
                                    </button>
                                    <button type="button" class="col-auto text-center btn btn-dark"
                                            onclick="writeEmoji(2)"
                                    >
                                        <i class='bi bi-emoji-neutral-fill' style='color: #FFD400;'> </i>
                                    </button>
                                    <button type="button" class="col-auto text-center btn btn-dark"
                                            onclick="writeEmoji(3)"
                                    >
                                        <i class='bi bi-emoji-frown-fill' style='color: #FFD400;'> </i>
                                    </button>
                                    <button type="button" class="col-auto text-center btn btn-dark"
                                            onclick="writeEmoji(4)"
                                    >
                                        <i class='bi bi-heart-fill' style='color: #C30000;'> </i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-auto" style="padding-left: 0">
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
