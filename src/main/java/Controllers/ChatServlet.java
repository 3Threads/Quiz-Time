package Controllers;

import BusinessLogic.SessionRemove;
import DAO.MessagesDAO;
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
        if (!SessionRemove.checkUser(httpServletRequest, httpServletResponse)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        SessionRemove.removeQuizAttributes(httpServletRequest);
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        String chatWith = httpServletRequest.getParameter("chatWith");
        if (chatWith != null && !chatWith.trim().equals("")) {
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            if (Integer.parseInt(chatWith) == myUser.getId()) {
                httpServletResponse.sendRedirect("/chat");
                return;
            }
            MessagesDAO msg = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
            msg.setMessagesSeen(myUser.getId(), Integer.parseInt(chatWith));
        }
        httpServletRequest.getRequestDispatcher("chat.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!SessionRemove.checkUser(httpServletRequest, httpServletResponse)) {
            httpServletResponse.getWriter().println("login");
            return;
        }
        String message = httpServletRequest.getParameter("message");
        String sendToStr = httpServletRequest.getParameter("sendTo");
        if (sendToStr != null && !message.trim().equals("") && !sendToStr.trim().equals("")) {
            int sendTo = Integer.parseInt(sendToStr);
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            MessagesDAO connect = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
            connect.sendMessage(myUser.getId(), sendTo, message);
        }
    }
}
