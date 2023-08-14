<%@ page import="Types.Announcement" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="header.jsp" %>
</head>
<body class="bg-dark text-light" style="overflow-x: clip">
<%
    ArrayList<User> users = usersDAO.searchUsers(request.getParameter("search"));
    ArrayList<Quiz> quizzes = quizzesDAO.searchQuizzes(request.getParameter("search"));
    ArrayList<Announcement> announcements = announcementsDAO.searchAnnouncement(request.getParameter("search"));
%>
<div class="container-fluid main" style="padding-top:0; padding-bottom: 0">

    <div class="row">
        <div class="col-3 back-color"
             style="position:fixed; height: calc(100vh - 95px); padding-top: 15px; border-right: 1px solid #666666;">
            <h3 style="margin:0">
                Search results for: <%=request.getParameter("search")%>
            </h3>
            <hr style="margin-top: 10px">

            <div class="headers" style="margin-bottom: 20px">
                Filters:
            </div>
            <div class="d-flex align-items-start">
                <div class="col nav flex-column nav-pills me-3" id="v-pills-tab" role="tablist"
                     aria-orientation="vertical"
                     style="width: 100%; text-align: left; font-size: 20px">
                    <button class="nav-link active" id="v-pills-all-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-all" type="button" role="tab" aria-controls="v-pills-home"
                            aria-selected="true" style="margin-bottom: 10px"
                            title="Everything">
                        <span class="icon-container"><i class="bi bi-files"></i></span>
                        <span class="text-container">everything</span>
                    </button>
                    <button class="nav-link" id="v-pills-users-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-users" type="button" role="tab" aria-controls="v-pills-home"
                            aria-selected="true" style="margin-bottom: 10px"
                            title="Users">
                        <span class="icon-container"><i class="bi bi-people"></i></span>
                        <span class="text-container">Users</span>
                    </button>
                    <button class="nav-link" id="v-pills-quizzes-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-quizzes" type="button" role="tab" aria-controls="v-pills-home"
                            aria-selected="true" style="margin-bottom: 10px"
                            title="Quizzes">
                        <span class="icon-container"><i class="bi bi-file-text"></i></span>
                        <span class="text-container">Quizzes</span>
                    </button>
                    <button class="nav-link" id="v-pills-announcements-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-announcements" type="button" role="tab"
                            aria-controls="v-pills-home"
                            aria-selected="true" style="margin-bottom: 10px"
                            title="Announcements">
                        <span class="icon-container"><i class="bi bi-megaphone"></i></span>
                        <span class="text-container">Announcements</span>
                    </button>
                </div>

            </div>
        </div>
        <div class="col-4"></div>
        <div class="col-7 mt-3"
             style="margin-bottom: 10px; overflow: auto;"
             uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
            <div class="tab-content" id="v-pills-tabContent">
                <div class="tab-pane fade show active" id="v-pills-all" role="tabpanel"
                     aria-labelledby="v-pills-all-tab" tabindex="0">

                    <%--Users--%>
                    <div class="uk-box-shadow-large uk-padding-small">

                        <div class="search-header ">
                            Users
                        </div>
                        <hr>
                        <%if (users.size() == 0) {%>
                        <div class="search-body">
                            Results Not Found
                        </div>
                        <%
                            }
                            for (User user : users) {
                        %>
                        <div class="search-body">
                            <a class="rank-<%=RankingSystem.countRank(user.getScore())%>" style="display: inline-block"
                               href=<%="/profile?user=" + user.getId()%>>
                                <%=user.getUsername()%>
                            </a>
                            <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                                -
                            </div>
                            <%
                                if (user.getId() != myUser.getId()) {
                            %>
                            <a href=<%="/chat?chatWith=" + user.getId()%>>
                                <div class=" btn-primary" style="display: inline-block;">
                                    <i class="bi bi-envelope"></i>
                                </div>
                            </a>
                            <%} else {%>
                            <div style="display: inline-block">
                                (You)
                            </div>
                            <%
                                }
                            %>
                        </div>
                        <%
                            }
                        %>
                    </div>

                    <br>

                    <%--Quizzes--%>
                    <div class="uk-box-shadow-large uk-padding-small">
                        <div class="search-header">
                            Quizzes
                        </div>
                        <hr>
                        <%if (quizzes.size() == 0) {%>
                        <div class="search-body">
                            Results Not Found
                        </div>
                        <%
                            }
                            for (Quiz quiz : quizzes) {
                        %>
                        <div class="search-body">
                            <a style="display: inline-block" href=<%="/quiz?quizId=" + quiz.getQuizId()%>>
                                <%=quiz.getQuizName()%>
                            </a>
                            <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                                created by
                            </div>
                            <a class="rank-<%=RankingSystem.countRank(usersDAO.getUserById(quiz.getCreatorID()).getScore())%>"
                               style="display: inline-block" href=<%="/profile?user=" + quiz.getCreatorID()%>>
                                <%=usersDAO.getUserById(quiz.getCreatorID()).getUsername()%>
                            </a>
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <br>

                    <%--Announcements--%>
                    <div class="uk-box-shadow-large uk-padding-small" style="padding-bottom: 0">
                        <div class="search-header">
                            Announcements
                        </div>
                        <hr>
                        <div class="search-body">
                            <%if (announcements.size() == 0) {%>
                            <div class="search-body">
                                Results Not Found
                            </div>
                            <%
                                }
                                for (Announcement announcement : announcements) {
                                    int userId = announcement.getWriterId();
                                    User user = usersDAO.getUserById(userId);
                                    String userName = user.getUsername();
                            %>

                            <div class="uk-grid-small uk-flex-middle">
                                <div class="uk-width-expand" style="padding-bottom:35px">
                                    <div class="uk-card-title uk-margin-remove-bottom text-light"
                                         style="display: inline-block; font-size: 18px;font-weight:bold; ">
                                        <div style="display: inline-block; ">
                                            <%=announcement.getTitle()%>
                                        </div>
                                        <div class="uk-text-meta uk-margin-remove-top"
                                             style="padding-left:5px; display: inline-block ">
                                            <%=userName%>
                                            <time datetime="2016-04-01T19:00">
                                                <%=announcement.getWriteTime()%>
                                            </time>
                                        </div>
                                    </div>
                                    <p style="font-size: 15px; margin:0">
                                        <%=
                                        announcement.getBody()
                                        %>
                                        <br>
                                    </p>
                                </div>
                            </div>

                            <%
                                }
                            %>
                        </div>
                    </div>
                    <br>

                </div>
                <div class="tab-pane fade" id="v-pills-users" role="tabpanel"
                     aria-labelledby="v-pills-users-tab" tabindex="0">
                    <%--Users--%>
                    <div class="search-header">
                        Users
                    </div>
                    <hr>
                    <%if (users.size() == 0) {%>
                    <div class="search-body">
                        Results Not Found
                    </div>
                    <%
                        }
                        for (User user : users) {
                    %>
                    <div class="search-body">
                        <a class="rank-<%=RankingSystem.countRank(user.getScore())%>" style="display: inline-block"
                           href=<%="/profile?user=" + user.getId()%>>
                            <%=user.getUsername()%>
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            -
                        </div>
                        <%
                            if (user.getId() != myUser.getId()) {
                        %>
                        <a href=<%="/chat?chatWith=" + user.getId()%>>
                            <div class=" btn-primary" style="display: inline-block;">
                                <i class="bi bi-envelope"></i>
                            </div>
                        </a>
                        <%} else {%>
                        <div style="display: inline-block">
                            (You)
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <%
                        }
                    %>
                </div>
                <div class="tab-pane fade" id="v-pills-quizzes" role="tabpanel"
                     aria-labelledby="v-pills-quizzes-tab" tabindex="0">

                    <%--Quizzes--%>
                    <div class="search-header">
                        Quizzes
                    </div>
                    <hr>
                    <%if (quizzes.size() == 0) {%>
                    <div class="search-body">
                        Results Not Found
                    </div>
                    <%
                        }
                        for (Quiz quiz : quizzes) {
                    %>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/quiz?quizId=" + quiz.getQuizId()%>>
                            <%=quiz.getQuizName()%>
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            created by
                        </div>
                        <a class="rank-<%=RankingSystem.countRank(usersDAO.getUserById(quiz.getCreatorID()).getScore())%>"
                           style="display: inline-block" href=<%="/profile?user=" + quiz.getCreatorID()%>>
                            <%=usersDAO.getUserById(quiz.getCreatorID()).getUsername()%>
                        </a>
                    </div>
                    <%
                        }
                    %>
                </div>
                <div class="tab-pane fade" id="v-pills-announcements" role="tabpanel"
                     aria-labelledby="v-pills-announcements-tab" tabindex="0">

                    <%--Announcements--%>
                    <div class="search-header">
                        Announcements
                    </div>
                    <hr>

                    <div class="search-body">
                        <%if (announcements.size() == 0) {%>
                        <div class="search-body">
                            Results Not Found
                        </div>
                        <%
                            }
                            for (Announcement announcement : announcements) {
                                int userId = announcement.getWriterId();
                                User user = usersDAO.getUserById(userId);
                                String userName = user.getUsername();
                        %>

                        <div class="uk-grid-small uk-flex-middle">
                            <div class="uk-width-expand">
                                <div class="uk-card-title uk-margin-remove-bottom text-light">
                                    <div style="font-size: 16px">
                                        <%=announcement.getTitle()%>
                                    </div>
                                </div>
                                <div class="rank-<%=RankingSystem.countRank(user.getScore())%> uk-text-meta uk-margin-remove-top"
                                     style="font-size:10px; margin-bottom: 5px">
                                    <%=userName%>
                                    <time datetime="2016-04-01T19:00">
                                        <%=announcement.getWriteTime()%>
                                    </time>
                                </div>
                                <p style="font-size: 15px">
                                    <%=
                                    announcement.getBody()
                                    %>
                                    <br><br>
                                </p>
                            </div>
                        </div>

                        <%
                            }
                        %>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

</body>
</html>
