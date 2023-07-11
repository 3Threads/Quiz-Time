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
<body class="bg-dark text-light">
<%@include file="header.jsp" %>
<div class="container">
    <div class="row">

        <div class="col-6">
            <br>
            <div class="uk-card uk-card-default bg-dark text-light">
                <div class="uk-card-header">
                    <div class="uk-grid-small uk-flex-middle" uk-grid>
                        <div class="uk-width-auto">
                            <img class="uk-border-circle" width="40" height="40" src="images/avatar.jpg" alt="Avatar">
                        </div>
                        <div class="uk-width-expand">
                            <h3 class="uk-card-title uk-margin-remove-bottom text-light">lasha kuprashvili</h3>
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
                        <div class="uk-width-auto">
                            <img class="uk-border-circle" width="40" height="40" src="images/avatar.jpg" alt="Avatar">
                        </div>
                        <div class="uk-width-expand">
                            <h3 class="uk-card-title uk-margin-remove-bottom text-light">Niko Khetsuriani</h3>
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

        <div class="col-6">
            <br>
            <div>
                <ul class="uk-child-width-expand" data-uk-tab="{connect:'#my-id'}">
                    <li><a style="color: white" href="">Tab 1</a></li>
                    <li><a style="color: white" href="">Tab 2</a></li>
                    <li><a style="color: white" href="">Tab 3</a></li>
                    <li><a style="color: white" href="">Tab 4</a></li>
                </ul>
                <ul id="my-id" class="uk-switcher uk-margin">
                    <li><a href="#" id="autoplayer" data-uk-switcher-item="next"></a>
                        <table class="uk-table uk-table-divider">
                            <thead>
                            <tr>
                                <th>Table Heading</th>
                                <th>Table Heading</th>
                                <th>Table Heading</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Table Data</td>
                                <td>Table Data</td>
                                <td>Table Data</td>
                            </tr>
                            <tr>
                                <td>Table Data</td>
                                <td>Table Data</td>
                                <td>Table Data</td>
                            </tr>
                            <tr>
                                <td>Table Data</td>
                                <td>Table Data</td>
                                <td>Table Data</td>
                            </tr>
                            </tbody>
                        </table>
                    </li>
                    <li>Content 2</li>
                    <li>Content 3</li>
                </ul>
            </div>
        </div>
    </div>
</div>

</body>
</html>
