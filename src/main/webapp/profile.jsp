<%@ page import="Types.User" %>
<%@ page import="FunctionalClasses.UserConnect" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: agurg
  Date: 7/10/2023
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- UIkit CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/css/uikit.min.css"/>

    <!-- UIkit JS -->
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.16.22/dist/js/uikit-icons.min.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<%
    int id = Integer.parseInt(request.getParameter("user"));
    UserConnect usersDB = (UserConnect) application.getAttribute("usersDB");
%>
<body class="bg-dark text-light">
<%@include file="header.jsp" %>
<br><br>
<div class="container">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls:uk-animation-fade delay: 500">
            <h3>Username: <%= usersDB.getUserById(id).getUsername()%></h3>

            <br>
            <button class="btn btn-success">Add Friend</button>

            <br><br><br>
            <div class="uk-padding-small "  uk-scrollspy="cls: uk-animation-slide-left; repeat: true" style="border: solid 1px gray; border-radius: 10px;">
                <h4>Friends:</h4>
                <ul class="uk-list uk-list-divider" style="max-height: 200px; overflow: auto">
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                    <li><a href="/profile?user=<%= 2%>">akaki</a></li>
                </ul>
            </div>
        </div>
        <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">
            <br>
<%--            <h4 class="text-center">Quizzes</h4>--%>
            <div>
                <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                    <li><a style="color: white" href="">Created quizzes</a></li>
                    <li><a style="color: white" href="">Result quizzes</a></li>
                </ul>
                <ul id="tables" class="uk-switcher uk-margin uk-box-shadow-large"
                    style="max-height: 485px; overflow: auto;">
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Completed</th>
                                <th>Creation</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>1</td>
                                <td><a href="#">Programming</a></td>
                                <td>80</td>
                                <td>23/10/2002</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td><a href="#">Programming</a></td>
                                <td>80</td>
                                <td>23/10/2002</td>
                            </tr>
                            </tbody>
                        </table>
                    </li>
                    <li>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Completed</th>
                                <th>Creation</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>1</td>
                                <td><a href="#">Programming</a></td>
                                <td>80</td>
                                <td>23/10/2002</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td><a href="#">Programming</a></td>
                                <td>80</td>
                                <td>23/10/2002</td>
                            </tr>
                            </tbody>
                        </table>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

    </div>
</div>
</div>
</body>
</html>
