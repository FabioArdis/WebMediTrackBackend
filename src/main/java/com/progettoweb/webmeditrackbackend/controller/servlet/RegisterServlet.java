package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import com.progettoweb.webmeditrackbackend.persistence.model.Token;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@WebServlet("/doRegister")
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String userType = req.getParameter("userType");

        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        resp.setStatus(HttpServletResponse.SC_OK);

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
                    CompletableFuture.runAsync(() -> sendConfirmationEmail(patient));
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

    private void sendConfirmationEmail(Patient patient) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.mailtrap.io");
        properties.put("mail.smtp.port", "2525");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        String concat = patient.getUsername() + ":" + patient.getPassword();
        String id = Base64.getEncoder().encodeToString(concat.getBytes());
        Token token = new Token();
        token.setId(id);
        token.setAuthorized(false);
        token.setExpiration_time(LocalDateTime.now());
        DBManager.getInstance().getTokenDAO().saveOrUpdate(token);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("a7b5e7824f66f5", "f5d56bc5d2316f");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@webmeditrack.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patient.getEmail()));
            message.setSubject("Confirm registration");
            message.setText("Dear " + patient.getUsername() + ",\nto complete the registration copy and paste the following link: http://localhost:8080/confirmRegistration?token=" + id );


            Transport.send(message);

            System.out.println("Email di conferma inviata a " + patient.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
