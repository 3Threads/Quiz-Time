<%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: agurg
  Date: 8/9/2023
  Time: 12:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <%@include file="header.jsp" %>
</head>
<body class="bg-dark text-light" style="overflow-x: clip">
<%
    Set<String> categories = new HashSet<String>(Arrays.asList("Sports", "Entertainment", "Geography", "Science", "History", "Art", "Literature", "Music", "Technology", "Animals", "Economy", "Politics", "Other"));
    String title = request.getParameter("title");
    if (title == null) title = "";
    String category = request.getParameter("category");
    if (category == null) category = "";
    int star = 0;
    try {
        star = Integer.parseInt(request.getParameter("starNum"));
    } catch (NumberFormatException e) {

    }
    ArrayList<Quiz> searchedQuizzes = quizzesDAO.deepSearchQuizzes(title, star, category);
%>
<script>
    function search() {
        let num = 0;
        if (document.getElementById('rate-5').checked) {
            num = 5;
        } else if (document.getElementById('rate-4').checked) {
            num = 4
        } else if (document.getElementById('rate-3').checked) {
            num = 3
        } else if (document.getElementById('rate-2').checked) {
            num = 2
        } else if (document.getElementById('rate-1').checked) {
            num = 1
        }
        $('#starHidden').val(num);
        const quizCategory = $('#quizCategory').val();
        $('#categoryHidden').val(quizCategory);
    }
</script>
<div class="container-fluid main" uk-scrollspy="cls: uk-animation-fade; repeat: true">
<div >
    <form action="/quizzesList" method="get" onsubmit="return search()">
        <input type="hidden" name="starNum" value="0" id="starHidden">
        <input type="hidden" name="category" value="" id="categoryHidden">
        <div class="row mt-3">
            <div class="col-3">
                <input class="form-control bg-dark whitePlaceholder text-light"
                       type="search"
                       placeholder="Search"
                       aria-label="Search"
                       name="title"
                       value="<%=title%>"
                       aria-describedby="inputGroup-sizing-sm">
            </div>
            <div class="col-3">
                <select class="form-select bg-dark text-light" aria-label="Default select example"
                        id="quizCategory">
                    <option <%
                        if (category.equals("")) out.print("selected");
                    %> value="">Choose category
                    </option>
                    <%
                        for (String currCategory : categories) {
                    %>
                    <option <%
                        if (category.equals(currCategory)) out.print("selected");
                    %> value=<%=currCategory%>><%=currCategory%>
                    </option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="col-auto d-flex align-items-center">
                <div class="star-widget">
                    <input type="radio" name="rate" id="rate-5"
                        <%
                        if(star==5) out.print("checked");
                    %> >
                    <label for="rate-5" class="fas fa-star" style="font-size:20px; padding: 5px"></label>
                    <input type="radio" name="rate" id="rate-4"
                        <%
                        if(star==4) out.print("checked");
                    %>>
                    <label for="rate-4" class="fas fa-star" style="font-size:20px; padding: 5px"></label>
                    <input type="radio" name="rate" id="rate-3"
                        <%
                        if(star==3) out.print("checked");
                    %>>
                    <label for="rate-3" class="fas fa-star" style="font-size:20px; padding: 5px"></label>
                    <input type="radio" name="rate" id="rate-2"
                        <%
                        if(star==2) out.print("checked");
                    %>>
                    <label for="rate-2" class="fas fa-star" style="font-size:20px; padding: 5px"></label>
                    <input type="radio" name="rate" id="rate-1"
                        <%
                        if(star==1) out.print("checked");
                    %>>
                    <label for="rate-1" class="fas fa-star" style="font-size:20px; padding: 5px"></label>
                </div>
            </div>
            <div class="col"></div>
            <div class="col-auto">
                <button class="btn btn-primary">Search</button>
            </div>
        </div>
    </form>
</div>
<div >
    <table class="uk-table uk-table-divider" style="margin:0">
        <hr>
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
        <thead>

        <tbody>
        <%
            for (int i = 0; i < searchedQuizzes.size(); i++) {
                Quiz quiz = searchedQuizzes.get(i);
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
                </a>
            </td>
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
</div>
</div>

</body>
