package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/doLogin")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = DBManager.getInstance().getUserDAO().findByPrimaryKey(username);
        boolean authorized;
        if (user == null)
        {
            System.out.println("You are not authorized.");
            authorized = false;
        } else {
            System.out.println("User " + user.getUsername() + " found.");
            if (password.equals(user.getPassword()))
            {
                System.out.println("Password matching.");
                authorized = true;

                HttpSession session = req.getSession();
                System.out.println("Session ID: " + session.getId());
                session.setAttribute("user", user);
                resp.sendRedirect("/");
            } else {
                System.out.println("Password not matching.");
                authorized = false;
            }

            if (!authorized)
            {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/views/noAuth.html");
                dispatcher.forward(req, resp);
            }
        }
    }
}
