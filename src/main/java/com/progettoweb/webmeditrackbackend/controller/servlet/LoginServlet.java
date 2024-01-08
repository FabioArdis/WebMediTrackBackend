package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/doLogin")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String userType = req.getParameter("userType");

        switch (userType)
        {
            case "doctor" ->
            {
                Doctor doctor = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(username);

                if (doctor == null)
                {
                    System.out.println("Can't find user " + username);
                    resp.setContentType("text/html");
                    PrintWriter out = resp.getWriter();
                    out.println("<script>");
                    out.println("alert(\"Username or password not matching.\");");
                    out.println("window.location.href='/login.html?user=doctor';");
                    out.println("</script>");
                } else {
                    System.out.println("Doctor " + doctor.getFullName() + " (" + username +  ") found.");
                    if (password.equals(doctor.getPassword()))
                    {
                        System.out.println("Passwords matching.");
                        HttpSession session = req.getSession();
                        System.out.println("Session ID: " + session.getId());
                        session.setAttribute("userType", "doctor");
                        session.setAttribute("user", doctor);
                        resp.sendRedirect("/");
                    } else {
                        System.out.println("Passwords not matching.");
                        resp.setContentType("text/html");
                        PrintWriter out = resp.getWriter();
                        out.println("<script>");
                        out.println("alert(\"Username or password not matching.\");");
                        out.println("window.location.href='/login.html?user=doctor';");
                        out.println("</script>");
                    }
                }
            }
            case "patient" ->
            {
                Patient patient = DBManager.getInstance().getPatientDAO().findByPrimaryKey(username);
                boolean authorized;

                if (patient == null)
                {
                    System.out.println("Can't find patient " + username);
                    resp.setContentType("text/html");
                    PrintWriter out = resp.getWriter();
                    out.println("<script>");
                    out.println("alert(\"Username or password not matching.\");");
                    out.println("window.location.href='/login.html?user=patient';");
                    out.println("</script>");
                } else {
                    System.out.println("Patient " + patient.getFullName() + " (" + username +  ") found.");
                    if (password.equals(patient.getPassword()))
                    {
                        System.out.println("Passwords matching.");
                        authorized = true;
                        HttpSession session = req.getSession();
                        System.out.println("Session ID: " + session.getId());
                        session.setAttribute("user", patient);
                        session.setAttribute("userType", "patient");
                        resp.sendRedirect("/");
                    } else {
                        System.out.println("Passwords not matching.");
                        resp.setContentType("text/html");
                        PrintWriter out = resp.getWriter();
                        out.println("<script>");
                        out.println("alert(\"Username or password not matching.\");");
                        out.println("window.location.href='/login.html?user=patient';");
                        out.println("</script>");
                    }
                }
            }
        }
    }
}
