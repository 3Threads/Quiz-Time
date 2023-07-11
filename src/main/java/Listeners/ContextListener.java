package Listeners;

import FunctionalClasses.UserConnect;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserConnect userConnect = new UserConnect(false);
        servletContextEvent.getServletContext().setAttribute("usersDB", userConnect);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
