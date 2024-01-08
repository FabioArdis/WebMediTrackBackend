package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.AssociationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorPatientDAOPostgres implements AssociationDAO {
    Connection con;
    public DoctorPatientDAOPostgres(Connection con) { this.con = con; }
    @Override
    public boolean ifExists(String doctor, String patient) {
        String query = "SELECT COUNT(*) FROM users.doctor_patients WHERE doctor_username = ? AND patient_username = ?";

        try (PreparedStatement st = con.prepareStatement(query))
        {
            st.setString(1, doctor);
            st.setString(2, patient);

            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void addAssociation(String doctor, String patient) {
        if (!ifExists(doctor, patient))
        {
            String saveStr = "INSERT INTO users.doctor_patients VALUES (?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, doctor);
                st.setString(2, patient);

                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeAssociation(String doctor, String patient) {
        if (ifExists(doctor, patient))
        {
            String query = "DELETE FROM users.doctor_patients WHERE doctor_username = ? AND patient_username = ?";
            try {
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, doctor);
                st.setString(2, patient);
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
