package Listeners;

import DAO.*;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BasicDataSource dataSource = DataSource.getDataSource(false);

        UsersDAO usersDAO = new UsersDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("usersDB", usersDAO);
        FriendsDAO friendsDAO = new FriendsDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("friendsDB", friendsDAO);
        ChallengesDAO challengesDAO = new ChallengesDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("challengesDB", challengesDAO);
        MessagesDAO messagesDAO = new MessagesDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("messagesDB", messagesDAO);
        QuizzesDAO quizzesDAO = new QuizzesDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("quizzesDB", quizzesDAO);
        ResultsDAO resultsDAO = new ResultsDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("resultsDB", resultsDAO);
        QuestionsDAO questionsDAO = new QuestionsDAO(dataSource);
        servletContextEvent.getServletContext().setAttribute("questionsDB", questionsDAO);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
