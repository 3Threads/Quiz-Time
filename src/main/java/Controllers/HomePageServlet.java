package Controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "homePage", value = "/homePage")
public class HomePageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!(boolean) httpServletRequest.getSession().getAttribute("isLoggedIn")) {
            httpServletResponse.sendRedirect("/login");
        } else {
            httpServletRequest.getRequestDispatcher("homePage.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
}