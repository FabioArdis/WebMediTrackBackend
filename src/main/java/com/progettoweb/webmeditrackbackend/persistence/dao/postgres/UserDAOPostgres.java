package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.UserDAO;
import com.progettoweb.webmeditrackbackend.persistence.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAOPostgres implements UserDAO {
    Connection con;

    public UserDAOPostgres(Connection con) { this.con = con; }

    @Override
    public List<User> findAll()
    {
        List<User> users = new ArrayList<User>();
        String query = "SELECT * FROM users.admins";
        try
        {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                user.setBirthDate(new Date(secs));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findByPrimaryKey(String username)
    {
        User user = null;
        String query = "SELECT * FROM users.admins WHERE username = ?";

        try
        {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                user = new User();

                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));

                long secs = rs.getDate("birthDate").getTime();
                user.setBirthDate(new java.sql.Date(secs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void saveOrUpdate(User user) {
        if (findByPrimaryKey(user.getEmail()) == null)
        {
            String saveStr = "INSERT INTO users.admins VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, user.getUsername());
                st.setString(2, user.getEmail());
                st.setString(3, user.getPassword());
                st.setString(4, user.getName());
                st.setString(5, user.getSurname());

                long secs = user.getBirthDate().getTime();
                st.setDate(6, new java.sql.Date(secs));

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String updateStr = "UPDATE users.admins SET email = ?, "
                    + "password = ?, "
                    + "name = ?, "
                    + "surname = ?, "
                    + "birthDate = ?"
                    + "WHERE username = ?";

            PreparedStatement st;

            try {
                st = con.prepareStatement(updateStr);

                st.setString(1, user.getEmail());
                st.setString(2, user.getPassword());
                st.setString(3, user.getName());
                st.setString(4, user.getSurname());

                long secs = user.getBirthDate().getTime();
                st.setDate(5, new java.sql.Date(secs));
                st.setString(6, user.getUsername());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(User user) {
        String query = "DELETE FROM users.admins WHERE username = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, user.getUsername());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
