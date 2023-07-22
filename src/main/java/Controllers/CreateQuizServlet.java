package Controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "createQuiz", value = "/createQuiz")
public class CreateQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.getRequestDispatcher("createQuiz.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (httpServletRequest.getParameter("action") != null && httpServletRequest.getParameter("action").equals("addQuestion")) {
            if(httpServletRequest.getParameter("questionType").equals("questionResponse")){
                String questionText= httpServletRequest.getParameter("questionText");
                String[] answers = httpServletRequest.getParameterValues("answer");
            }
        }
    }
}
