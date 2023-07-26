<%@ page import="Types.Quiz" %>
<%@ page import="Types.Result" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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

    <title>Quiz Time</title>
</head>
<body class="bg-dark text-light" style="overflow-x: clip">
<%@include file="header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
            <br>
            <%--            557.8--%>
            <h4 class="headers">Announcements</h4>
            <div class="uk-card uk-card-default bg-dark text-light">
                <div class="uk-card-header" style="max-height: 80px">
                    <div class="uk-grid-small uk-flex-middle">
                        <div class="uk-width-expand">
                            <h5 class="uk-card-title uk-margin-remove-bottom text-light">
                                <div class="admins">lasha kuprashivii  </div>
                            </h5>
                            <p class="uk-text-meta uk-margin-remove-top">
                                <time class="admin_date" datetime="2016-04-01T19:00">July 10, 2023</time>
                            </p>
                        </div>
                    </div>
                </div>
                <%-- max 20 wordsssssssssss--%>
                <p class="card-middle" style="margin-right: 7px">
                    Today is our Anniversary, and we want to tell you that it was honor ...
                </p>
                <div class="card-bottom uk-card-footer">
                    <a class="read_more" href="#" class="uk-button uk-text-meta">Read more</a>
                </div>
            </div>
            <br>
            <div class="uk-card uk-card-default bg-dark text-light" >
                <div class="uk-card-header" style="max-height: 80px">
                    <div class="uk-grid-small uk-flex-middle">
                        <div class="uk-width-expand">
                            <h5 class="uk-card-title uk-margin-remove-bottom text-light">
                                <div class="admins">niko khetsuriani </div>
                            </h5>
                            <p class="uk-text-meta uk-margin-remove-top">
                                <time class="admin_date" datetime="2016-04-01T19:00">July 10, 2023</time>
                            </p>
                        </div>
                    </div>
                </div>
                <%-- max 20 wordsssssssssss--%>
                <p class="card-middle" style="margin-right: 7px">
                    bodyysssssssssssssssssssssssss sssssssssssssssss a a a a asssssssssssss ssssssssssssssss ssssssssssssssss
                </p>
                <div class="card-bottom uk-card-footer">
                    <a class="read_more" href="#" class="uk-button uk-text-meta">Read more</a>
                </div>
            </div>

        </div>

        <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">
            <br>
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
                        <table name="Popular" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
                                </th>
                                <th>
                                    <div class="table_name">Users</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> popularQuizzes = quizzesDAO.getPopularQuizzes(10);
                                int toFollow = Math.min(popularQuizzes.size(), 10);
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

                    <%--Recent Quizzes Table--%>
                    <li>
                        <table name="Recent" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
                                </th>
                                <th>
                                    <div class="table_name">Users</div>
                                </th>
                                <th>
                                    <div class="table_name">Creation</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Quiz> recentQuizzes = quizzesDAO.getLastDayQuizzes();
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
                        <table name="My Written" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
                                </th>
                                <th>
                                    <div class="table_name">Users</div>
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
                        <table name="My Created" class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>
                                    <div class="table_name">#</div>
                                </th>
                                <th>
                                    <div class="table_name">Name</div>
                                </th>
                                <th>
                                    <div class="table_name">Users</div>
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