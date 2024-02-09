package com.progettoweb.webmeditrackbackend.controller;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import com.progettoweb.webmeditrackbackend.persistence.model.Token;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Services {
    @GetMapping("/userInfo")
    public String showUserInfo(Model model, HttpSession session) {
        Object userObject = session.getAttribute("user");

        if (userObject instanceof Doctor)
        {
            DBManager.getInstance().getDoctorDAO().saveOrUpdate((Doctor) userObject);
        } else if (userObject instanceof Patient) {
            DBManager.getInstance().getPatientDAO().saveOrUpdate((Patient) userObject);
        }

        String userType = (userObject != null) ? userObject.getClass().getSimpleName() : "Unknown";

        model.addAttribute("userType", userType);

        return "userInfo";
    }

    @GetMapping("/doLogout")
    public String logout(Model model, HttpSession session)
    {
        if (session != null) {
            session.invalidate();
            return "/index";
        } else {
            return "/index";
        }
    }

    @GetMapping("/doSearchPatient")
    public String searchPatient(Model model, HttpSession session)
    {
        return "searchPatient";
    }

    @GetMapping("/doSearchMeds")
    public String searchMeds(Model model, HttpSession session) { return "searchMed"; }

    @GetMapping("/goCreatePlan")
    public String createPlan(Model model, HttpSession session) { return "createPlan"; }

    @GetMapping("/confirmRegistration")
    public String confirmRegistration(Model model, HttpSession session, HttpServletRequest request) {
        String token = request.getParameter("token");
        Token userToken = DBManager.getInstance().getTokenDAO().findByPrimaryKey(token);
        if (token != null) {
            if (!userToken.isExpired()) {
                userToken.setAuthorized(true);
                DBManager.getInstance().getTokenDAO().saveOrUpdate(userToken);
            } else {
                DBManager.getInstance().getTokenDAO().delete(userToken);
            }
        }
        return "/index";}
}
