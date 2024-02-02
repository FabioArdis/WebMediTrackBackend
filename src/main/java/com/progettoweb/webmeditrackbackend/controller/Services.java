package com.progettoweb.webmeditrackbackend.controller;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
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
}
