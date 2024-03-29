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
    <link rel="icon" href="images/icon.png" type="image/x-icon"/>
</head>
<%
    response.setCharacterEncoding("UTF-8");
%>
<body class="bg-dark text-light">
<div class="container text-center" style="height: 100%">
    <div class="row align-items-center" style="height: 100%">
        <div class="col">
            <div style="width: 60%; margin-left: 20%">
                <img src="images/logo.png" class="img-fluid" alt="...">
            </div>
            <div class="row mt-2">
                <div class="col"></div>
                <div class="col-4">
                    <form method="post" action="/createAccount">
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="text" class="form-control bg-dark text-light whitePlaceholder"
                                       placeholder="Username" name="username" maxlength="20" minlength="4"
                                       pattern="[^$\|\/]+" title="Your username must not exist '$','|' or '/'" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="password" class="form-control bg-dark text-light whitePlaceholder"
                                       name="password" placeholder="Password" pattern="[A-Za-z0-9]+" minlength="5"
                                       title="Your password must be a minimum 5 length and you can use [a-z],[A-Z],[0-9]
                                                    and symbols"required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col text-start">
                                <a href="/login">Login</a>
                            </div>
                            <div class="col-auto">
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success ">Register</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <%
                        if (request.getParameter("userAlreadyExists") != null && request.getParameter("userAlreadyExists").equals("true")) {
                            out.print("<div class='text-danger'> Account with this username already exists</div>");
                        }
                    %>
                </div>
                <div class="col"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
