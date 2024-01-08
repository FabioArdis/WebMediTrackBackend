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


@WebServlet("/doRegister")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String userType = req.getParameter("userType");

        switch (userType)
        {
            case "doctor" ->
            {
                Doctor check = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(req.getParameter("username"));
                if (check == null)
                {
                    Doctor doctor = new Doctor();
                    doctor.setUsername(req.getParameter("username"));
                    doctor.setPassword(req.getParameter("password"));
                    doctor.setEmail(req.getParameter("email"));
                    doctor.setName(req.getParameter("name"));
                    doctor.setSurname(req.getParameter("surname"));
                    doctor.setBirthDate(java.sql.Date.valueOf(req.getParameter("birthDate")));
                    doctor.setCf(req.getParameter("cf"));
                    doctor.setDoctorId(req.getParameter("doctorId"));
                    doctor.setSpec(req.getParameter("spec"));
                    doctor.setDocAvailTime(req.getParameter("docAvailTime"));

                    DBManager.getInstance().getDoctorDAO().saveOrUpdate(doctor);
                    System.out.println("Created doctor " + doctor.getUsername() + ".");
                    resp.sendRedirect("/");
                } else {
                    System.out.println("Doctor " + req.getParameter("username") + " already found.");
                    resp.sendRedirect("/");
                }
            }
            case "patient" ->
            {
                Patient check = DBManager.getInstance().getPatientDAO().findByPrimaryKey(req.getParameter("username"));
                if (check == null)
                {
                    Patient patient = new Patient();
                    patient.setUsername(req.getParameter("username"));
                    patient.setPassword(req.getParameter("password"));
                    patient.setEmail(req.getParameter("email"));
                    patient.setName(req.getParameter("name"));
                    patient.setSurname(req.getParameter("surname"));
                    patient.setBirthDate(java.sql.Date.valueOf(req.getParameter("birthDate")));
                    patient.setCf(req.getParameter("cf"));
                    patient.setTScode(req.getParameter("tsCode"));

                    DBManager.getInstance().getPatientDAO().saveOrUpdate(patient);
                    System.out.println("Created patient " + patient.getUsername() + ".");
                    resp.sendRedirect("/");
                } else {
                    System.out.println("Patient " + req.getParameter("username") + " already found.");
                    resp.sendRedirect("/");
                }
            }
        }
    }
}
