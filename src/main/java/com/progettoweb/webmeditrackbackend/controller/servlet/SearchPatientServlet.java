package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/doSearch")
public class SearchPatientServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String username = req.getParameter("username");
        HttpSession session = req.getSession();

        Patient patient = DBManager.getInstance().getPatientDAO().findByPrimaryKey(username);

        if (patient == null)
        {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert(\"Patient not found.\");");
            out.println("window.location.href='/doSearchPatient';");
            out.println("</script>");
            return;
        }

        req.setAttribute("patient", patient);
        session.setAttribute("patient", patient);

        resp.sendRedirect("/doSearchPatient");
    }
}
