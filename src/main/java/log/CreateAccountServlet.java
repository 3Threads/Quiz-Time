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
        String username = httpServletRequest.getParameter("username");
        String email = httpServletRequest.getParameter("inputEmail3");
        String password = httpServletRequest.getParameter("inputPassword3");
        SQLconnect sql = (SQLconnect) httpServletRequest.getServletContext().getAttribute("sql");
        try {
            if(!sql.checkEmail(email)) {
                sql.addUser(username, email, password);
                httpServletRequest.getRequestDispatcher
                        ("index.jsp");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
