package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/doRegister")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        User check = DBManager.getInstance().getUserDAO().findByPrimaryKey(req.getParameter("username"));
        if (check == null)
        {
            User user = new User();
            user.setUsername(req.getParameter("username"));
            user.setPassword(req.getParameter("password"));
            user.setEmail(req.getParameter("email"));
            user.setName(req.getParameter("name"));
            user.setSurname(req.getParameter("surname"));
            user.setBirthDate(java.sql.Date.valueOf(req.getParameter("birthDate")));

            DBManager.getInstance().getUserDAO().saveOrUpdate(user);
            System.out.println("Created user " + user.getUsername() + ".");
            resp.sendRedirect("/");
        } else {
            System.out.println("User " + req.getParameter("username") + " already found.");
            resp.sendRedirect("/");
        }
    }
}
