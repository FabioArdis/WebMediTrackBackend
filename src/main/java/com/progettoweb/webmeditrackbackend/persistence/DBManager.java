package com.progettoweb.webmeditrackbackend.persistence;

import com.progettoweb.webmeditrackbackend.persistence.dao.UserDAO;
import com.progettoweb.webmeditrackbackend.persistence.dao.postgres.UserDAOPostgres;

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

}
