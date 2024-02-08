package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import com.progettoweb.webmeditrackbackend.persistence.model.Plan;
import com.progettoweb.webmeditrackbackend.persistence.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/doCreatePlan")
public class CreatePlanServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String patientUsername;

        String name = req.getParameter("planName");
        int length = Integer.parseInt(req.getParameter("planLength"));
        String type = req.getParameter("planType");

        Plan newPlan = null;

        String userType = (String) req.getSession().getAttribute("userType");

        switch (userType)
        {
            case "doctor" ->
            {
                patientUsername = req.getParameter("patientUsername");

                Doctor doc = (Doctor) req.getSession().getAttribute("user");
                Patient pat = DBManager.getInstance().getPatientDAO().findByPrimaryKey(patientUsername);

                if (pat != null) {
                    newPlan = new Plan();
                    newPlan.setName(name);
                    newPlan.setLength(length);
                    newPlan.setType(type);

                    int id = DBManager.getInstance().getPlanDAO().saveOrUpdate(newPlan);
                    DBManager.getInstance().getUserPlanDAO().addAssociation(pat.getUsername(), Integer.toString(id));

                    pat.addPlanId(id);
                    pat.loadPlansDetails();

                    resp.setContentType("text/html");
                    PrintWriter out = resp.getWriter();
                    out.println("<script>");
                    out.println("alert(\"Plan " + name + " for patient " + pat.getUsername() + " created.\");");
                    out.println("window.location.href='/';");
                    out.println("</script>");
                    return;
                } else {
                    resp.setContentType("text/html");
                    PrintWriter out = resp.getWriter();
                    out.println("<script>");
                    out.println("alert(\"Patient " + patientUsername + " not found.\");");
                    out.println("window.location.href='/goCreatePlan';");
                    out.println("</script>");
                    return;
                }
            }
            case "patient" ->
            {
                Patient pat = (Patient) req.getSession().getAttribute("user");

                newPlan = new Plan();
                newPlan.setName(name);
                newPlan.setLength(length);
                newPlan.setType(type);

                int id = DBManager.getInstance().getPlanDAO().saveOrUpdate(newPlan);

                DBManager.getInstance().getUserPlanDAO().addAssociation(pat.getUsername(), Integer.toString(id));

                pat.addPlanId(id);
                pat.loadPlansDetails();

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("<script>");
                out.println("alert(\"Plan created.\");");
                out.println("window.location.href='/';");
                out.println("</script>");
                return;
            }
        }
    }
}
