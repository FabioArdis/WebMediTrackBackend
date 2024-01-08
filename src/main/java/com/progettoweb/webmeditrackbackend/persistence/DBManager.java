package com.progettoweb.webmeditrackbackend.persistence;

import com.progettoweb.webmeditrackbackend.persistence.dao.AssociationDAO;
import com.progettoweb.webmeditrackbackend.persistence.dao.UserDAO;
import com.progettoweb.webmeditrackbackend.persistence.dao.postgres.DoctorDAOPostgres;
import com.progettoweb.webmeditrackbackend.persistence.dao.postgres.DoctorPatientDAOPostgres;
import com.progettoweb.webmeditrackbackend.persistence.dao.postgres.PatientDAOPostgres;
import com.progettoweb.webmeditrackbackend.persistence.dao.postgres.UserDAOPostgres;
import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance = null;

    private DBManager() {}

    public static DBManager getInstance()
    {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    Connection con = null;

    public Connection getConnection()
    {
        if (con == null)
        {
            try {
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/meditrack", "postgres", "root");
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public UserDAO getUserDAO() { return new UserDAOPostgres(getConnection()); }
    public UserDAO<Doctor> getDoctorDAO() { return new DoctorDAOPostgres(getConnection()); }
    public UserDAO<Patient> getPatientDAO() { return new PatientDAOPostgres(getConnection()); }
    public AssociationDAO getDoctorPatientDAO() { return new DoctorPatientDAOPostgres(getConnection()); }

}
