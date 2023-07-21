package Controllers;

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
        if (httpServletRequest.getSession().getAttribute("userInfo") == null) {
            httpServletResponse.sendRedirect("/login");
        } else {
            if (httpServletRequest.getParameter("chatWith") != null) {
                User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
                if (Integer.parseInt(httpServletRequest.getParameter("chatWith")) == myUser.getId()) {
                    httpServletResponse.sendRedirect("/chat");
                    return;
                }
                MessagesDAO msg = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
                User curUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
                msg.setMessagesSeen(curUser.getId(), Integer.parseInt(httpServletRequest.getParameter("chatWith")));
            }
            httpServletRequest.getRequestDispatcher("chat.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String message = httpServletRequest.getParameter("message");
        if (httpServletRequest.getParameter("sendTo") != null && !message.trim().equals("")) {
            int sendTo = Integer.parseInt(httpServletRequest.getParameter("sendTo"));
            User myUser = (User) httpServletRequest.getSession().getAttribute("userInfo");
            MessagesDAO connect = (MessagesDAO) httpServletRequest.getServletContext().getAttribute("messagesDB");
            connect.sendMessage(myUser.getId(), sendTo, message);
        }
    }
}
