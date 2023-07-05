package log;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;


public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    }
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        SQLconnect sql = (SQLconnect) httpServletRequest.getServletContext().getAttribute("sql");
        try {
                if(sql.checkUser(email, password)) {
                    httpServletRequest.getRequestDispatcher
                                ("homePage.jsp").forward(httpServletRequest, httpServletResponse);
                } else httpServletRequest.getRequestDispatcher
                        ("index.jsp").forward(httpServletRequest, httpServletResponse);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
