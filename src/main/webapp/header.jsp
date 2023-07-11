<%--
  Created by IntelliJ IDEA.
  User: agurg
  Date: 7/5/2023
  Time: 8:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Types.User" %>
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

    <link rel="stylesheet" type="text/css" href="style.css">

    <title>Quiz Time</title>
</head>
<%
    User user = (User) session.getAttribute("userInfo");
%>
<body class="bg-dark text-light">
<div class="container">
    <div class="row">
        <div class="col">
            <a href="/homePage"><img src="images/logo.png" class="img-fluid" alt="..."></a>
        </div>
        <div class="col-2">
        </div>
        <div class="col d-flex align-items-end">
            <div class="container">
                <div class="d-flex flex-row-reverse mb-2">
                    <a style="margin-left: 5px" href="/logout">
                        Log Out
                    </a>
                    <a>|</a>
                    <i class="bi bi-bell-fill mt-1" style="margin-right: 5px"></i>
                    <a style="margin-right: 3px" href="/profile?user=<%= user.getId()%>"><%=  user.getUsername()%>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class=" menuBar text-light"
         style="height: 50px; border-style: solid; border-width: 2px; border-color: white; border-radius: 10px;">
        <div class="container row" style="width: 100%">
            <div class="col-9 mb-1 mb-lg-0  mt-1">
                <a href="/createQuiz">
                    <button class="btn btn-dark text-light">Create Quiz</button>
                </a>
                <a href="/randomQuiz">
                    <button class="btn btn-dark text-light">Random Quiz</button>
                </a>
            </div>
            <form class="col d-flex mb-1 mb-lg-0 mt-1" role="search" method="get" action="/search">
                <div class="input-group input-group-sm mb-1 mt-1">
                    <input class="form-control me-2 bg-dark whitePlaceholder text-light" type="search"
                           placeholder="Search"
                           aria-label="Search"
                           name="search"
                           aria-describedby="inputGroup-sizing-sm">
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>