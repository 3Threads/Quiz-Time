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
<div class="container text-center" style="height: 100%">
    <div class="row align-items-center" style="height: 100%">
        <div class="col">
            <div style="width: 60%; margin-left: 20%">
                <img src="images/logo.png" class="img-fluid" alt="...">
            </div>
            <div class="row mt-2">
                <div class="col"></div>
                <div class="col-4">
                    <form>
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="text" class="form-control bg-dark text-light whitePlaceholder"
                                       id="username" placeholder="Username">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="email" class="form-control bg-dark text-light whitePlaceholder"
                                       id="inputEmail3" placeholder="Email">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm">
                                <input type="password" class="form-control bg-dark text-light whitePlaceholder"
                                       id="inputPassword3" placeholder="Password">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col text-start">
                                <a href="index.jsp">Login</a>
                            </div>
                            <div class="col-auto">
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success ">Register</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col"></div>
            </div>
        </div>
    </div>

</div>

</body>
</html>
