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
import java.io.PrintWriter;

@WebServlet("/removePatient")
public class RemovePatientServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Doctor doc = (Doctor) req.getSession().getAttribute("user");
        Patient pat = (Patient) req.getSession().getAttribute("patient");
        if (DBManager.getInstance().getDoctorPatientDAO().ifExists(doc.getUsername(), pat.getUsername()))
        {
            DBManager.getInstance().getDoctorPatientDAO().removeAssociation(doc.getUsername(), pat.getUsername());
            doc.removePatientUsername(pat.getUsername());
            doc.loadPatientsDetail();
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert(\"Patient removed.\");");
            out.println("window.location.href='/doSearchPatient';");
            out.println("</script>");
        }
        else {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert(\"You haven't added this patient yet.\");");
            out.println("window.location.href='/doSearchPatient';");
            out.println("</script>");
        }
    }
}
