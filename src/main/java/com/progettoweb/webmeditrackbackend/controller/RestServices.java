package com.progettoweb.webmeditrackbackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class RestServices {

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/doRegistering")
    public User registerUser(@RequestBody Map<String, String> requestBody) {
        String userType = requestBody.get("userType");

        switch (userType) {
            case "doctor" -> {
                Doctor check = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(requestBody.get("username"));
                if (check == null) {
                    Doctor doctor = new Doctor();
                    doctor.setUsername(requestBody.get("username"));
                    doctor.setPassword(requestBody.get("password"));
                    doctor.setEmail(requestBody.get("email"));
                    doctor.setName(requestBody.get("name"));
                    doctor.setSurname(requestBody.get("surname"));
                    doctor.setBirthDate(java.sql.Date.valueOf(requestBody.get("birthDate")));
                    doctor.setCf(requestBody.get("cf"));
                    doctor.setDoctorId(requestBody.get("doctorId"));
                    doctor.setSpec(requestBody.get("spec"));
                    doctor.setDocAvailTime(requestBody.get("docAvailTime"));

                    DBManager.getInstance().getDoctorDAO().saveOrUpdate(doctor);
                    System.out.println("Created doctor " + doctor.getUsername() + ".");
                    return doctor;
                } else {
                    System.out.println("Doctor " + requestBody.get("username") + " already found.");
                    return null;
                }
            }
            case "patient" -> {
                Patient check = DBManager.getInstance().getPatientDAO().findByPrimaryKey(requestBody.get("username"));
                if (check == null) {
                    Patient patient = new Patient();
                    patient.setUsername(requestBody.get("username"));
                    patient.setPassword(requestBody.get("password"));
                    patient.setEmail(requestBody.get("email"));
                    patient.setName(requestBody.get("name"));
                    patient.setSurname(requestBody.get("surname"));
                    patient.setBirthDate(java.sql.Date.valueOf(requestBody.get("birthDate")));
                    patient.setCf(requestBody.get("cf"));
                    patient.setTScode(requestBody.get("tsCode"));

                    DBManager.getInstance().getPatientDAO().saveOrUpdate(patient);
                    System.out.println("Created patient " + patient.getUsername() + ".");
                    return patient;
                } else {
                    System.out.println("Patient " + requestBody.get("username") + " already found.");
                    return null;
                }
            }
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/doLogging")
    public User loginUser(@RequestBody Map<String, String> requestBody, HttpSession session) {
        String userType = requestBody.get("userType");
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        switch (userType) {
            case "doctor" -> {
                Doctor doctor = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(username);

                if (doctor == null) {
                    System.out.println("Can't find user " + username);
                    return null;
                } else {
                    System.out.println("Doctor " + doctor.getFullName() + " (" + username + ") found.");
                    if (password.equals(doctor.getPassword())) {
                        System.out.println("Passwords matching.");

                        System.out.println("Session ID: " + session.getId());
                        doctor.loadPatientsDetail();
                        return doctor;
                    } else {
                        System.out.println("Passwords not matching.");
                        return null;
                    }
                }
            }
            case "patient" -> {
                Patient patient = DBManager.getInstance().getPatientDAO().findByPrimaryKey(username);
                boolean authorized;

                if (patient == null) {
                    System.out.println("Can't find patient " + username);
                    return null;
                } else {
                    System.out.println("Patient " + patient.getFullName() + " (" + username + ") found.");
                    if (password.equals(patient.getPassword())) {
                        System.out.println("Passwords matching.");
                        authorized = true;
                        System.out.println("Session ID: " + session.getId());
                        patient.loadPlansDetails();
                        patient.loadDoctorsDetails();
                        return patient;
                    } else {
                        System.out.println("Passwords not matching.");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/search/medicine")
    public List<Medicine> searchMedicine(@RequestParam String name) throws ServletException, IOException {
        System.out.println("prova!" + name);
        String dbUrl = "http://localhost:8081/api/farmaci/searchByName";
        String fullUrl = dbUrl + "?name=" + name;
        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        List<Medicine> medicineList = new ArrayList<>();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
                responseContent.append(line);
            System.out.println(responseContent);
            reader.close();
        } else {
            responseContent.append("ERROR:").append(responseCode);
        }

        connection.disconnect();

        String result = responseContent.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(result);

        for (JsonNode jsonNode : jsonArray) {
            Medicine medicine = new Medicine();
            medicine.setId(jsonNode.get("id").asInt());
            medicine.setName(jsonNode.get("name").asText());
            medicine.setDosage(jsonNode.get("dosage").asInt());
            medicine.setManufacture(jsonNode.get("manufacturer").asText());
            medicine.setBatchNumber(jsonNode.get("batchNumber").asInt());
            Date expirationTime = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                expirationTime = dateFormat.parse(jsonNode.get("expirationTime").asText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            medicine.setExpirationTime(expirationTime);
            medicine.setNotes(jsonNode.get("notes").asText());

            medicineList.add(medicine);
        }

        return medicineList;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/search/patient")
    public Patient searchPatient(@RequestParam String name) throws ServletException, IOException {
        System.out.println(name);
        Patient patient = DBManager.getInstance().getPatientDAO().findByPrimaryKey(name);

        if (patient == null) {
            //TODO: gestire la mancanza
            return null;
        }

        return patient;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/user")
    public User updateUser(@RequestBody Map<String, String> requestBody) throws ServletException, IOException {
        System.out.println("richiedendo update di" + requestBody.get("username") + " " + requestBody.get("userType"));

        switch (requestBody.get("userType")) {
            case "doctor" -> {
                Doctor doctor = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(requestBody.get("username"));
                doctor.loadPatientsDetail();
                return doctor;
            }
            case "patient" -> {
                Patient patient = DBManager.getInstance().getPatientDAO().findByPrimaryKey(requestBody.get("username"));
                patient.loadDoctorsDetails();
                patient.loadPlansDetails();
                return patient;
            }
        }

        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/addPatient")
    public ResponseEntity<String> addPatient(@RequestBody Map<String, String> requestBody) {
        if (!DBManager.getInstance().getDoctorPatientDAO().ifExists(requestBody.get("username"), requestBody.get("patUsername")))
        {
            DBManager.getInstance().getDoctorPatientDAO().addAssociation(requestBody.get("username"), requestBody.get("patUsername"));
            Doctor doctor = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(requestBody.get("username"));
            doctor.addPatientUsername(requestBody.get("patUsername"));
            doctor.loadPatientsDetail();
            return new ResponseEntity<>("Patient " + requestBody.get("patUsername") + " added successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("ERROR: Patient " + requestBody.get("patUsername") + " already added.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/removePatient")
    public ResponseEntity<String> removePatient(@RequestBody Map<String, String> requestBody) {
        if (DBManager.getInstance().getDoctorPatientDAO().ifExists(requestBody.get("username"), requestBody.get("patUsername")))
        {
            DBManager.getInstance().getDoctorPatientDAO().removeAssociation(requestBody.get("username"), requestBody.get("patUsername"));
            Doctor doctor = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(requestBody.get("username"));
            doctor.removePatientUsername(requestBody.get("patUsername"));
            doctor.loadPatientsDetail();
            return new ResponseEntity<>("Patient " + requestBody.get("patUsername") + " removed successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("ERROR: Patient " + requestBody.get("patUsername") + " is already removed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/addPlan")
    public ResponseEntity<String> addPlan(@RequestBody Map<String, String> requestBody) {
        System.out.println("username:" + requestBody.get("username") + "\npatientUsername: " + requestBody.get("patientUsername"));
        String patientUsername = requestBody.get("patientUsername");

        Plan plan = null;

        switch (requestBody.get("userType"))
        {
            case "doctor" ->
            {
                Patient pat = DBManager.getInstance().getPatientDAO().findByPrimaryKey(patientUsername);

                if (pat != null)
                {
                    plan = new Plan();

                    plan.setName(requestBody.get("name"));
                    plan.setLength(Integer.parseInt(requestBody.get("planLength")));
                    plan.setType(requestBody.get("type"));

                    int id = DBManager.getInstance().getPlanDAO().saveOrUpdate(plan);
                    DBManager.getInstance().getUserPlanDAO().addAssociation(pat.getUsername(), Integer.toString(id));

                    pat.addPlanId(id);
                    pat.loadPlansDetails();

                    return new ResponseEntity<>("Plan " + plan.getName() + " created successfully.", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Could not find patient '" + patientUsername + "'.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            case "patient" ->
            {
                Patient pat = DBManager.getInstance().getPatientDAO().findByPrimaryKey(requestBody.get("username"));

                if (pat != null)
                {
                    plan = new Plan();

                    plan.setName(requestBody.get("name"));
                    plan.setLength(Integer.parseInt(requestBody.get("planLength")));
                    plan.setType(requestBody.get("type"));

                    int id = DBManager.getInstance().getPlanDAO().saveOrUpdate(plan);
                    DBManager.getInstance().getUserPlanDAO().addAssociation(requestBody.get("username"), Integer.toString(id));

                    pat.addPlanId(id);
                    pat.loadPlansDetails();
                    return new ResponseEntity<>("Plan " + plan.getName() + " created successfully.", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Could not find patient '" + patientUsername + "'.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>("Could not add plan " + requestBody.get("name") + ".", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/removePlan")
    public ResponseEntity<String> removePlan(@RequestBody Map<String, String> requestBody) {
        Patient pat = DBManager.getInstance().getPatientDAO().findByPrimaryKey(requestBody.get("username"));
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(requestBody.get("planId")));
        if (plan != null)
        {
            if (DBManager.getInstance().getUserPlanDAO().ifExists(pat.getUsername(), Integer.toString(plan.getId())))
            {
                DBManager.getInstance().getUserPlanDAO().removeAssociation(pat.getUsername(), requestBody.get("planId"));
                DBManager.getInstance().getPlanDAO().delete(plan);
                pat.removePlan(plan);
                pat.loadPlansDetails();

                return new ResponseEntity<>("Plan (id[" + requestBody.get("planId") + "]) removed successfully.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Plan (id[" + requestBody.get("planId") + "is already removed.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Could not find plan (id[" + requestBody.get("planId") + ". Reload.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/search/plan")
    public Plan getplan(@RequestBody Map<String, String> requestBody) {
        System.out.println("id" + requestBody.get("id"));
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(requestBody.get("id")));
        try {
            plan.loadMedicinesDetail();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plan;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/plan/addMedicine")
    public ResponseEntity<String> addMedicine(@RequestBody Map<String, String> requestBody) {
        System.out.println("plan(" + requestBody.get("planId" ) + ") requesting to add medicine(" + requestBody.get("medicineId") + ").");
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(requestBody.get("planId")));
        if (plan != null)
        {
            if (!DBManager.getInstance().getPlanMedicineDAO().ifExists(requestBody.get("planId"), requestBody.get("medicineId")))
            {
                DBManager.getInstance().getPlanMedicineDAO().addAssociation(requestBody.get("planId"), requestBody.get("medicineId"));
                plan.addMedicineId(Integer.parseInt(requestBody.get("medicineId")));
                try {
                    plan.loadMedicinesDetail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<>("Medicine added.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("ERROR: Medicine already added.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("ERROR: Can't find plan.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/plan/removeMedicine")
    public ResponseEntity<String> removeMedicine(@RequestBody Map<String, String> requestBody) {
        System.out.println("plan(" + requestBody.get("planId" ) + ") requesting to remove medicine(" + requestBody.get("medicineId") + ").");
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(requestBody.get("planId")));
        if (plan != null)
        {
            if (DBManager.getInstance().getPlanMedicineDAO().ifExists(requestBody.get("planId"), requestBody.get("medicineId")))
            {
                DBManager.getInstance().getPlanMedicineDAO().removeAssociation(requestBody.get("planId"), requestBody.get("medicineId"));
                plan.removeMedicineId(Integer.parseInt(requestBody.get("medicineId")));
                try {
                    plan.loadMedicinesDetail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<>("Medicine added.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("ERROR: Medicine not found.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("ERROR: Can't find plan.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/update/plan/data")
    public ResponseEntity<String> updatePlanData(@RequestBody Map<String, String> requestBody) {
        System.out.println("plan(" + requestBody.get("planId" ) + ") requesting to be updated");
        Plan plan = DBManager.getInstance().getPlanDAO().findById(Integer.parseInt(requestBody.get("planId")));
        if (plan != null)
        {
            plan.setName(requestBody.get("name"));
            plan.setLength(Integer.parseInt(requestBody.get("length")));
            plan.setType(requestBody.get("type"));
            DBManager.getInstance().getPlanDAO().saveOrUpdate(plan);
            return new ResponseEntity<>("Plan updated.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("ERROR: Can't find plan.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/confirmRegistration")
    public  ResponseEntity<String> confirmRegistration(@RequestParam String token){
        Token userToken = DBManager.getInstance().getTokenDAO().findByPrimaryKey(token);
        if (token != null) {
            if (!userToken.isExpired()) {
                userToken.setAuthorized(true);
                DBManager.getInstance().getTokenDAO().saveOrUpdate(userToken);
                return new ResponseEntity<>("Email confirmed", HttpStatus.OK);
            } else {
                DBManager.getInstance().getTokenDAO().delete(userToken);
            }
        }
        return new ResponseEntity<>("ERROR: Confirmation token expired", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
