package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/doUpdate")
public class UpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        Object user = req.getSession().getAttribute("user");

        if (user instanceof Doctor)
        {
            DBManager.getInstance().getDoctorDAO().saveOrUpdate((Doctor) user);
            resp.sendRedirect("/userInfo");
        } else if (user instanceof Patient) {
            DBManager.getInstance().getPatientDAO().saveOrUpdate((Patient) user);
            resp.sendRedirect("/userInfo");
        }
    }
}
