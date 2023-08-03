<%@ page import="Types.Announcement" %>
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

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>search</title>
</head>

<body class="bg-dark text-light" style="overflow-x: clip">
<%@include file="header.jsp" %>

<div class="container">

    <div class="row" style="height: 80%; min-height: 700px;">
        <div class="col-4 mt-3 uk-box-shadow-large uk-padding-small" style="margin-bottom: 20px; height: 95%">
            <div class="headers">
                Search results for: niko
            </div>
            <hr>

            <div class="headers" style="margin-bottom: 20px">
                Filters:
            </div>
            <%--        <div class="uk-child-width-1-2@s" uk-grid>--%>
            <%--            <div>--%>
            <%--                <ul class="uk-tab-right" uk-tab>--%>

            <%--                    <li class="uk-active"><a href="#" style="height:60px; font-size: 25px; color: white">--%>
            <%--                        <i class="bi bi-people"></i>--%>
            <%--                        Users--%>
            <%--                    </a></li>--%>
            <%--                    <li><a href="#" style="height:60px; font-size: 25px; color: white">--%>
            <%--                        <i class="bi bi-file-text"></i>--%>
            <%--                        Quizzes--%>
            <%--                    </a></li>--%>
            <%--                    <li><a href="#" style="height:60px; font-size: 25px; color: white">--%>
            <%--                        <i class="bi bi-megaphone"></i>--%>
            <%--                        Announcements--%>
            <%--                    </a></li>--%>
            <%--                </ul>--%>
            <%--            </div>--%>
            <%--        </div>--%>
            <div class="d-flex align-items-start">
                <div class="nav flex-column nav-pills me-3" id="v-pills-tab" role="tablist" aria-orientation="vertical"
                     style="width: 100%; text-align: left; font-size: 20px">
                    <button class="nav-link active" id="v-pills-all-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-all" type="button" role="tab" aria-controls="v-pills-home"
                            aria-selected="true" style="text-align: left; margin-bottom: 10px">
                        <i class="bi bi-files"></i>
                        everything
                    </button>
                    <button class="nav-link " id="v-pills-users-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-users" type="button" role="tab" aria-controls="v-pills-home"
                            aria-selected="false" style="text-align: left; margin-bottom: 10px">
                        <i class="bi bi-people"></i>
                        Users
                    </button>
                    <button class="nav-link" id="v-pills-quizzes-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-quizzes" type="button" role="tab" aria-controls="v-pills-profile"
                            aria-selected="false" style="text-align: left; margin-bottom: 10px">
                        <i class="bi bi-file-text"></i>
                        Quizzes
                    </button>
                    <button class="nav-link" id="v-pills-announcements-tab" data-bs-toggle="pill"
                            data-bs-target="#v-pills-announcements" type="button" role="tab"
                            aria-controls="v-pills-messages"
                            aria-selected="false" style="text-align: left; margin-bottom: 10px">
                        <i class="bi bi-megaphone"></i>
                        Announcements
                    </button>
                </div>

            </div>
        </div>
        <div class="col-1"></div>
        <div class="col-7 mt-3 uk-box-shadow-large uk-padding-small"
             style="margin-bottom: 20px; overflow: auto; max-height: 95%"
             uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
            <div class="tab-content" id="v-pills-tabContent">
                <div class="tab-pane fade show active" id="v-pills-all" role="tabpanel"
                     aria-labelledby="v-pills-all-tab" tabindex="0">

                    <%--Users--%>
                    <div class="search-header">
                        Users
                    </div>
                    <hr>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            user1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            -
                        </div>
                        <a href=<%="/chat?chatWith="%>>
                            <div class=" btn-primary" style="display: inline-block;">
                                <i class="bi bi-envelope"></i>
                            </div>
                        </a>
                    </div>

                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            -
                        </div>
                        <a href=<%="/chat?chatWith="%>>
                            <div class=" btn-primary" style="display: inline-block;">
                                <i class="bi bi-envelope"></i>
                            </div>
                        </a>
                    </div>
                    <br><br>

                    <%--Quizzes--%>
                    <div class="search-header">
                        Quizzes
                    </div>
                    <hr>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            Quiz1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            created by
                        </div>
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                    </div>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            Quiz1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            created by
                        </div>
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                    </div>
                    <br><br>

                    <%--Announcements--%>
                    <div class="search-header">
                        Announcements
                    </div>
                    <hr>
                    <div class="search-body">
                        <%

                            ArrayList<Types.Announcement> announcements = announcementsDAO.getAnnouncements();

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
                                <div class="uk-text-meta uk-margin-remove-top"
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
                <div class="tab-pane fade" id="v-pills-users" role="tabpanel"
                     aria-labelledby="v-pills-users-tab" tabindex="0">
                    <%--Users--%>
                    <div class="search-header">
                        Users
                    </div>
                    <hr>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            user1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            -
                        </div>
                        <a href=<%="/chat?chatWith="%>>
                            <div class=" btn-primary" style="display: inline-block;">
                                <i class="bi bi-envelope"></i>
                            </div>
                        </a>
                    </div>

                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            -
                        </div>
                        <a href=<%="/chat?chatWith="%>>
                            <div class=" btn-primary" style="display: inline-block;">
                                <i class="bi bi-envelope"></i>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="tab-pane fade" id="v-pills-quizzes" role="tabpanel"
                     aria-labelledby="v-pills-quizzes-tab" tabindex="0">

                    <%--Quizzes--%>
                    <div class="search-header">
                        Quizzes
                    </div>
                    <hr>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            Quiz1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            created by
                        </div>
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                    </div>
                    <div class="search-body">
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            Quiz1
                        </a>
                        <div style="margin-left: 5px; margin-right: 5px; display: inline-block">
                            created by
                        </div>
                        <a style="display: inline-block" href=<%="/profile?user=" %>>
                            dachiGoshadze
                        </a>
                    </div>

                </div>
                <div class="tab-pane fade" id="v-pills-announcements" role="tabpanel"
                     aria-labelledby="v-pills-announcements-tab" tabindex="0">

                    <%--Announcements--%>
                    <div class="search-header">
                        Announcements
                    </div>
                    <hr>

                        <div class="search-body">
                            <%
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
                                    <div class="uk-text-meta uk-margin-remove-top"
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
