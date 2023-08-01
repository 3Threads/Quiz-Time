package Controllers;

import BusinessLogic.SessionRemove;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "logout", value = "/logout")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        SessionRemove.removeQuizAttributes(httpServletRequest);
        httpServletRequest.getSession().removeAttribute("userInfo");
        httpServletResponse.sendRedirect("/login");
    }
}
