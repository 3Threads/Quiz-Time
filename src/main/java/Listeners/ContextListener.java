package Listeners;

import FunctionalClasses.*;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BasicDataSource dataSource = DataSource.getDataSource(false);

        UserConnect userConnect = new UserConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("usersDB", userConnect);
        FriendsConnect friendsConnect = new FriendsConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("friendsDB", friendsConnect);
        ChallengesConnect challengesConnect = new ChallengesConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("challengesDB", challengesConnect);
        MessagesConnect messagesConnect = new MessagesConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("messagesDB", messagesConnect);
        QuizzesConnect quizzesConnect = new QuizzesConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("quizzesDB", quizzesConnect);
        ResultsConnect resultsConnect = new ResultsConnect(dataSource);
        servletContextEvent.getServletContext().setAttribute("resultsDB", resultsConnect);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
