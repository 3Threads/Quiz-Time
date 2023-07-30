package Controllers;

import BusinessLogic.SessionRemove;
import DAO.MessagesDAO;
import DAO.UsersDAO;
import Types.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "chat", value = "/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        int chatWith;
        if (httpServletRequest.getParameter("chatWith") != null) {
            try {
                chatWith = Integer.parseInt(httpServletRequest.getParameter("chatWith"));
            } catch (NumberFormatException e) {
                httpServletResponse.sendRedirect("/chat");
                return;
            }
            UsersDAO usersDAO = (UsersDAO) httpServletRequest.getServletContext().getAttribute("usersDB");
            if (usersDAO.getUserById(chatWith) == null) {
                httpServletResponse.sendRedirect("/chat");
                return;
            }
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            if (chatWith == myUser.getId()) {
                httpServletResponse.sendRedirect("/chat");
                return;
            }
            MessagesDAO msg = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
            msg.setMessagesSeen(myUser.getId(), chatWith);
        }
        httpServletRequest.getRequestDispatcher("chat.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest)) {
            httpServletResponse.getWriter().println("login");
            return;
        }
        String message = httpServletRequest.getParameter("message");
        String sendToStr = httpServletRequest.getParameter("sendTo");
        if (sendToStr != null && !message.trim().equals("") && !sendToStr.trim().equals("")) {
            int sendTo;
            try {
                sendTo = Integer.parseInt(httpServletRequest.getParameter("chatWith"));
            } catch (NumberFormatException e) {
                httpServletResponse.sendRedirect("/chat");
                return;
            }
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            MessagesDAO connect = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
            connect.sendMessage(myUser.getId(), sendTo, message);
        }
    }
}
