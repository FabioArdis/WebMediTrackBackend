package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.dao.UserDAO;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDAOPostgres implements UserDAO<Patient> {
    Connection con;

    public PatientDAOPostgres(Connection con) { this.con = con; }

    @Override
    public List<Patient> findAll()
    {
        List<Patient> patients = new ArrayList<>();

        String query = "SELECT * FROM users.doctor";
        try
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                Patient patient = new Patient();
                patient.setUsername(rs.getString("username"));
                patient.setEmail(rs.getString("email"));
                patient.setPassword(rs.getString("password"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                patient.setBirthDate(new Date(secs));
                patient.setCf(rs.getString("cf"));
                patient.setTScode(rs.getString("ts_code"));

                String docQuery = "SELECT doctor_username FROM users.doctor_patients WHERE patient_username = ?";
                PreparedStatement docSt = con.prepareStatement(docQuery);
                docSt.setString(1, patient.getUsername());
                ResultSet docRs = docSt.executeQuery();

                while (docRs.next())
                {
                    patient.addDoctor(DBManager.getInstance().getDoctorDAO().findByPrimaryKey(rs.getString("username")));
                }

                patients.add(patient);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient findByPrimaryKey(String username) {
        Patient patient = null;
        String query = "SELECT * FROM users.patient WHERE username = ?";

        try
        {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                patient = new Patient();

                patient.setUsername(rs.getString("username"));
                patient.setEmail(rs.getString("email"));
                patient.setPassword(rs.getString("password"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                patient.setBirthDate(new Date(secs));
                patient.setCf(rs.getString("cf"));
                patient.setTScode(rs.getString("ts_code"));

                String docQuery = "SELECT doctor_username FROM users.doctor_patients WHERE patient_username = ?";
                PreparedStatement docSt = con.prepareStatement(docQuery);
                docSt.setString(1, patient.getUsername());
                ResultSet patRs = docSt.executeQuery();

                while (patRs.next())
                {
                    patient.addDoctorUsername(patRs.getString("doctor_username"));
                }

                String planQuery = "SELECT plan_id FROM users.therapeutical_plans WHERE username = ?";
                PreparedStatement planSt = con.prepareStatement(planQuery);
                planSt.setString(1, patient.getUsername());
                ResultSet planRs = planSt.executeQuery();
                while (planRs.next())
                {
                    patient.addPlanId(planRs.getInt("plan_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return patient;
    }

    @Override
    public void saveOrUpdate(Patient patient) {
        if (findByPrimaryKey(patient.getUsername()) == null)
        {
            String saveStr = "INSERT INTO users.patient VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, patient.getUsername());
                st.setString(2, patient.getPassword());
                st.setString(3, patient.getEmail());
                st.setString(4, patient.getName());
                st.setString(5, patient.getSurname());

                long secs = patient.getBirthDate().getTime();
                st.setDate(6, new java.sql.Date(secs));
                st.setString(7, patient.getCf());
                st.setString(8, patient.getTScode());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String updateStr = "UPDATE users.patient SET email = ?, "
                    + "password = ?, "
                    + "name = ?, "
                    + "surname = ?, "
                    + "birthDate = ?, "
                    + "cf = ?, "
                    + "ts_code = ? "
                    + "WHERE username = ?";

            PreparedStatement st;

            try {
                st = con.prepareStatement(updateStr);

                st.setString(1, patient.getEmail());
                st.setString(2, patient.getPassword());
                st.setString(3, patient.getName());
                st.setString(4, patient.getSurname());

                long secs = patient.getBirthDate().getTime();
                st.setDate(5, new java.sql.Date(secs));
                st.setString(6, patient.getCf());
                st.setString(7, patient.getTScode());
                st.setString(8, patient.getUsername());

                st.executeUpdate();

                patient.loadDoctorsDetails();
                patient.loadPlansDetails();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Patient patient) {
        String query = "DELETE FROM users.patient WHERE username = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, patient.getUsername());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
