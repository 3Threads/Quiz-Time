package BusinessLogic;

import DAO.UsersDAO;
import Types.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class SessionRemove {
    public static void removeQuizAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute("writingQuestions");
        request.getSession().removeAttribute("userAnswers");
        request.getSession().removeAttribute("title");
        request.getSession().removeAttribute("description");
        request.getSession().removeAttribute("questions");
        request.getSession().removeAttribute("startTime");
    }
    public static boolean checkUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UsersDAO usersDAO = (UsersDAO) request.getServletContext().getAttribute("usersDB");
        User myUser = (User) request.getSession().getAttribute("userInfo");
        if(myUser == null) return false;
        int id = usersDAO.getUserId(myUser.getUsername());
        if (id == -1) {
            request.getSession().removeAttribute("userInfo");
            removeQuizAttributes(request);
            return false;
        } else {
            User user = usersDAO.getUserById(id);
            request.getSession().setAttribute("userInfo", user);
            return true;
        }
    }
}
