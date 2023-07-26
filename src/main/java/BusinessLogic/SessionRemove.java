package BusinessLogic;

import javax.servlet.http.HttpServletRequest;

public class SessionRemove {
    public static void removeQuizAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute("writingQuestions");
        request.getSession().removeAttribute("userAnswers");
        request.getSession().removeAttribute("title");
        request.getSession().removeAttribute("description");
        request.getSession().removeAttribute("questions");
    }
}
