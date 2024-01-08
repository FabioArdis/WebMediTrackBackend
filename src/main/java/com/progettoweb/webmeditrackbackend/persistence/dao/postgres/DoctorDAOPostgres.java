package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.dao.UserDAO;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorDAOPostgres implements UserDAO<Doctor> {
    Connection con;

    public DoctorDAOPostgres(Connection con) { this.con = con; }

    @Override
    public List<Doctor> findAll()
    {
        List<Doctor> doctors = new ArrayList<Doctor>();

        String query = "SELECT * FROM users.doctor";
        try
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                Doctor doctor = new Doctor();
                doctor.setUsername(rs.getString("username"));
                doctor.setEmail(rs.getString("email"));
                doctor.setPassword(rs.getString("password"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                doctor.setBirthDate(new Date(secs));
                doctor.setCf(rs.getString("cf"));
                doctor.setDoctorId(rs.getString("doctorid"));
                doctor.setSpec(rs.getString("spec"));
                doctor.setDocAvailTime(rs.getString("docavailtime"));

                String patQuery = "SELECT patient_username FROM users.doctor_patients WHERE doctor_username = ?";
                PreparedStatement patSt = con.prepareStatement(patQuery);
                patSt.setString(1, doctor.getUsername());
                ResultSet patRs = patSt.executeQuery();

                while (patRs.next())
                {
                    doctor.addPatient(DBManager.getInstance().getPatientDAO().findByPrimaryKey(rs.getString("username")));
                }

                doctors.add(doctor);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return doctors;
    }

    @Override
    public Doctor findByPrimaryKey(String username) {
        Doctor doctor = null;
        String query = "SELECT * FROM users.doctor WHERE username = ?";

        try
        {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                doctor = new Doctor();

                doctor.setUsername(rs.getString("username"));
                doctor.setEmail(rs.getString("email"));
                doctor.setPassword(rs.getString("password"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                doctor.setBirthDate(new Date(secs));
                doctor.setCf(rs.getString("cf"));
                doctor.setDoctorId(rs.getString("doctorid"));
                doctor.setSpec(rs.getString("spec"));
                doctor.setDocAvailTime(rs.getString("docavailtime"));

                String patQuery = "SELECT patient_username FROM users.doctor_patients WHERE doctor_username = ?";
                PreparedStatement patSt = con.prepareStatement(patQuery);
                patSt.setString(1, doctor.getUsername());
                ResultSet patRs = patSt.executeQuery();

                while (patRs.next())
                {
                    doctor.addPatient(DBManager.getInstance().getPatientDAO().findByPrimaryKey(rs.getString("username")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return doctor;
    }

    @Override
    public void saveOrUpdate(Doctor doctor) {
        if (findByPrimaryKey(doctor.getUsername()) == null)
        {
            String saveStr = "INSERT INTO users.doctor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, doctor.getUsername());
                st.setString(2, doctor.getEmail());
                st.setString(3, doctor.getPassword());
                st.setString(4, doctor.getName());
                st.setString(5, doctor.getSurname());

                long secs = doctor.getBirthDate().getTime();
                st.setDate(6, new java.sql.Date(secs));
                st.setString(7, doctor.getCf());
                st.setString(8, doctor.getDoctorId());
                st.setString(9, doctor.getSpec());
                st.setString(10, doctor.getDocAvailTime());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String updateStr = "UPDATE users.doctor SET email = ?, "
                    + "password = ?, "
                    + "name = ?, "
                    + "surname = ?, "
                    + "birthDate = ?"
                    + "cf = ?"
                    + "doctorid = ?"
                    + "spec = ?"
                    + "docavailtime = ?"
                    + "WHERE username = ?";

            PreparedStatement st;

            try {
                st = con.prepareStatement(updateStr);

                st.setString(1, doctor.getEmail());
                st.setString(2, doctor.getPassword());
                st.setString(3, doctor.getName());
                st.setString(4, doctor.getSurname());

                long secs = doctor.getBirthDate().getTime();
                st.setDate(5, new java.sql.Date(secs));
                st.setString(6, doctor.getCf());
                st.setString(7, doctor.getDoctorId());
                st.setString(8, doctor.getSpec());
                st.setString(9, doctor.getDocAvailTime());
                st.setString(10, doctor.getUsername());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Doctor doctor) {
        String query = "DELETE FROM users.doctor WHERE username = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, doctor.getUsername());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
