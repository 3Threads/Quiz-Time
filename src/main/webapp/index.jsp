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
    if (session.getAttribute("userInfo") != null) {
        response.sendRedirect("/homePage");
    }
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
                    <form method="post" action="/login">
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="text" class="form-control bg-dark text-light whitePlaceholder"
                                       name="username" placeholder="Username">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="password" class="form-control bg-dark text-light whitePlaceholder"
                                       name="password" placeholder="Password">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col text-start">
                                <a href="/createAccount">Create new account</a>
                            </div>
                            <div class="col-auto">
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success ">Login</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <%
                        if (request.getParameter("loginFailed") != null && request.getParameter("loginFailed").equals("true")) {
                            out.print("<div class='text-danger'>Invalid username or password</div>");
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
