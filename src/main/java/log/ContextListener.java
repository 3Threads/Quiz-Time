package log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SQLconnect connect = new SQLconnect();
        servletContextEvent.getServletContext().setAttribute("db", connect);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
