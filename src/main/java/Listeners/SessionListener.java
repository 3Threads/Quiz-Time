package Listeners;


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().removeAttribute("userInfo");
        httpSessionEvent.getSession().removeAttribute("questions");
        httpSessionEvent.getSession().removeAttribute("title");
        httpSessionEvent.getSession().removeAttribute("description");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
