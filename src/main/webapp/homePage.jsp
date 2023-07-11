<%--
  Created by IntelliJ IDEA.
  User: agurg
  Date: 7/5/2023
  Time: 8:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
            crossorigin="anonymous"></script>

    <link href="style.css" rel="stylesheet">

    <title>Quiz Time</title>
</head>
<body class="bg-dark text-light" style="overflow-x: clip">
<%@include file="header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-4" uk-scrollspy="cls: uk-animation-slide-left; repeat: true">
            <br>
            <h4>Announcements</h4>
            <div class="uk-card uk-card-default bg-dark text-light">
                <div class="uk-card-header">
                    <div class="uk-grid-small uk-flex-middle" uk-grid>
                        <div class="uk-width-expand">
                            <h5 class="uk-card-title uk-margin-remove-bottom text-light">lasha kuprashvili</h5>
                            <p class="uk-text-meta uk-margin-remove-top">
                                <time datetime="2016-04-01T19:00">July 10, 2023</time>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="uk-card-body">
                    <p>bodyyyy.</p>
                </div>
                <div class="uk-card-footer">
                    <a href="#" class="uk-button uk-text-meta">Read more</a>
                </div>
            </div>
            <br>
            <div class="uk-card uk-card-default bg-dark text-light">
                <div class="uk-card-header">
                    <div class="uk-grid-small uk-flex-middle" uk-grid>
                        <div class="uk-width-expand">
                            <h5 class="uk-card-title uk-margin-remove-bottom text-light">Niko Khetsuriani</h5>
                            <p class="uk-text-meta uk-margin-remove-top">
                                <time datetime="2016-04-01T19:00">April 01, 2016</time>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="uk-card-body">
                    <p>I agree.</p>
                </div>
                <div class="uk-card-footer">
                    <a href="#" class="uk-button uk-text-meta">Read more</a>
                </div>
            </div>
        </div>

        <div class="col-8" uk-scrollspy="cls: uk-animation-slide-right; repeat: true">
            <br>
            <h4 class="text-center">Quizzes</h4>
            <div>
                <ul class="uk-child-width-expand text-center  d-flex align-items-end" data-uk-tab="{connect:'#tables'}">
                    <li><a style="color: white" href="">Popular</a></li>
                    <li><a style="color: white" href="">Recent</a></li>
                    <li><a style="color: white" href="">My Written</a></li>
                    <li><a style="color: white" href="">My Created</a></li>
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

</body>
</html>
