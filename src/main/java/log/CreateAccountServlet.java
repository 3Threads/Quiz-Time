package log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CreateAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String username = httpServletRequest.getParameter("user");
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        SQLconnect sql = (SQLconnect) httpServletRequest.getServletContext().getAttribute("db");
        try {
            if(!sql.checkEmail(email)) {
                sql.addUser(username, email, password);
                httpServletRequest.getRequestDispatcher
                        ("index.jsp").forward(httpServletRequest , httpServletResponse );
            } else httpServletRequest.getRequestDispatcher
                    ("register.jsp").forward(httpServletRequest, httpServletResponse);
        } catch (SQLException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
