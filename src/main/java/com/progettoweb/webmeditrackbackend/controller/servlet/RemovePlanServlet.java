package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import com.progettoweb.webmeditrackbackend.persistence.model.Plan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/removePlan")
public class RemovePlanServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Patient pat = (Patient) req.getSession().getAttribute("user");
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(req.getParameter("planId")));
        if (DBManager.getInstance().getUserPlanDAO().ifExists(pat.getUsername(), Integer.toString(plan.getId())))
        {
            DBManager.getInstance().getUserPlanDAO().removeAssociation(pat.getUsername(), req.getParameter("planId"));
            DBManager.getInstance().getPlanDAO().delete(plan);
            pat.removePlan(plan);
            pat.loadPlansDetails();
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert(\"Plan " + plan.getName() + " removed.\");");
            out.println("window.location.href='/userInfo';");
            out.println("</script>");
        } else {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert(\"Can't find " + plan.getName() + ".\");");
            out.println("window.location.href='/userInfo';");
            out.println("</script>");
        }
    }
}
