package Listeners;

import FunctionalClasses.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserConnect userConnect = new UserConnect(false);
        servletContextEvent.getServletContext().setAttribute("usersDB", userConnect);
        FriendsConnect friendsConnect = new FriendsConnect(false);
        servletContextEvent.getServletContext().setAttribute("friendsDB", friendsConnect);
        ChallengesConnect challengesConnect = new ChallengesConnect(false);
        servletContextEvent.getServletContext().setAttribute("challengesDB", challengesConnect);
        MessagesConnect messagesConnect = new MessagesConnect(false);
        servletContextEvent.getServletContext().setAttribute("messagesDB", messagesConnect);
        QuizzesConnect quizzesConnect = new QuizzesConnect(false);
        servletContextEvent.getServletContext().setAttribute("quizzesDB", quizzesConnect);
        ResultsConnect resultsConnect = new ResultsConnect(false);
        servletContextEvent.getServletContext().setAttribute("resultsDB", resultsConnect);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
