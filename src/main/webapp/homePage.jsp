<%@ page import="Types.*" %>
<html>
<head>
    <%@include file="header.jsp" %>
</head>
<body class="bg-dark text-light" style="overflow-x: clip">
<div class="container-fluid main">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
            <a href="#modalAnnouncements" class="mt-1" uk-toggle>
                <h4 class="headers">Announcements</h4>
            </a>
            <div id="modalAnnouncements" uk-modal class="main">
                <div class="uk-modal-dialog bg-dark">
                    <button class="uk-modal-close-default" type="button" uk-close></button>
                    <div class="uk-modal-header bg-dark">
                        <h2 class="uk-modal-title">Announcements</h2>
                    </div>
                    <div class="uk-modal-body">
                        <%
                            if (myUser.isAdmin()) {
                        %>
                        <form action="/homePage" method="post">
                            <input class="titleArea uk-margin form-control bg-dark whitePlaceholder text-light"
                                   type="text"
                                   placeholder="Title"
                                   aria-label="Title"
                                   name="title"
                                   id="titleField"
                                   style="width: 50%;">
                            <textarea class="txtArea uk-margin form-control bg-dark whitePlaceholder text-light"
                                      placeholder="Description"
                                      aria-label="Description"
                                      name="description"
                                      id="descriptionField"
                            ></textarea>
                            <button class="btn btn-success">Post</button>
                        </form>
                        <hr>

                        <%
                            }
                            ArrayList<Types.Announcement> announcements = announcementsDAO.getAnnouncements();
                            for (Announcement announcement : announcements) {
                                int userId = announcement.getWriterId();
                                User user = usersDAO.getUserById(userId);
                                String userName = user.getUsername();
                        %>

                        <div class="uk-card uk-card-default bg-dark text-light">
                            <div class="uk-card-header" style="height: 80px">
                                <div class="uk-grid-small uk-flex-middle">
                                    <div class="uk-width-expand">
                                        <h5 class="uk-card-title uk-margin-remove-bottom text-light">
                                            <div class="announcement-admins">
                                                <%=announcement.getTitle()%>
                                            </div>
                                        </h5>
                                        <p class="uk-text-meta uk-margin-remove-top announcement-admin-date">
                                            <%=userName%>
                                            <time datetime="2016-04-01T19:00">
                                                <%=announcement.getWriteTime()%>
                                            </time>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <%-- max 20 wordsssssssssss--%>
                            <p class="card-announcement-middle " style="word-wrap: break-word">
                                <%=announcement.getBody()%>
                                <br><br>
                            </p>
                        </div>

                        <%
                            }
                        %>

                    </div>
                </div>
            </div>
            <%
                int toFollow = Math.min(announcements.size(), 2);
                for (int i = 0; i < toFollow; i++) {
                    Announcement announcement = announcements.get(i);
                    int userId = announcement.getWriterId();
                    User user = usersDAO.getUserById(userId);
                    String userName = user.getUsername();

            %>
            <div class="uk-card uk-card-default bg-dark text-light">
                <div class="uk-card-header header">
                    <div class="uk-grid-small uk-flex-middle">
                        <div class="uk-width-expand">
                            <h5 class="uk-card-title uk-margin-remove-bottom text-light">
                                <div class="admins"><%=announcement.getTitle()%>
                                </div>
                            </h5>
                            <p class="uk-text-meta uk-margin-remove-top admin_date">
                                <%=userName%>
                                <time class="admin_date" datetime="2016-04-01T19:00"><%=announcement.getWriteTime()%>
                                </time>
                            </p>
                        </div>
                    </div>
                </div>

                <%-- max 20 wordsssssssssss--%>
                <p class="card-middle" style="word-wrap: break-word">
                    <%
                        String body = announcement.getBody();
                        if (body.length() > 100) {
                            body = body.substring(0, 100) + "...";
                        }
                        out.print(body);
                    %>
                </p>
                <div class="card-bottom uk-card-footer">
                    <%--class="read_more uk-button uk-text-meta"--%>
                    <a href="#modalAnnouncements" class="read_more" uk-toggle>Read more</a>
                </div>
            </div>
            <br>

            <%
                }
            %>

        </div>


        <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">
            <h4 class="headers text-center">Quizzes</h4>
            <div>
                <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                    <li><a style="color: white" href="">
                        <div class="table_name">Popular</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">Recent</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">Completed</div>
                    </a></li>
                    <li><a style="color: white" href="">
                        <div class="table_name">My Own</div>
                    </a></li>
                </ul>
                <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                    style="height: 475px; overflow: auto;">

                    <%--Popular Quizzes Tables--%>
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
                                    <div class="table_name">Completed</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> popularQuizzes = quizzesDAO.getPopularQuizzes();
                                toFollow = Math.min(popularQuizzes.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Quiz quiz = popularQuizzes.get(i);

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
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>

                    <%--Recent Quizzes Table--%>
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
                                    <div class="table_name">Completed</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> recentQuizzes = quizzesDAO.getRecentQuizzes();
                                toFollow = Math.min(recentQuizzes.size(), 10);
                                for (int i = 0; i < toFollow; i++) {
                                    Quiz quiz = recentQuizzes.get(i);

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
                                    </a></td>
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
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>

                    <%--My Written Quizzes Table--%>
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
                                    <div class="table_name">Completed</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
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
                                    </a></td>
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
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </li>

                    <%--My Creates Quizzes Table--%>
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
                                    <div class="table_name">Completed</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> myCreated = quizzesDAO.getMyCreatedQuizzes(myUser.getId());
                                toFollow = Math.min(myCreated.size(), 10);
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
                                    </a></td>
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