package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.AssociationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPlanDAOPostgres implements AssociationDAO {
    Connection con;
    public UserPlanDAOPostgres(Connection con) { this.con = con; }

    @Override
    public boolean ifExists(String username, String planId) {
        int convertedId = Integer.parseInt(planId);
        String query = "SELECT COUNT(*) FROM users.therapeutical_plans WHERE username = ? AND plan_id = ?";

        try (PreparedStatement st = con.prepareStatement(query))
        {
            st.setString(1, username);
            st.setInt(2, convertedId);

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
    public void addAssociation(String username, String planId) {
        int convertedId = Integer.parseInt(planId);
        if (!ifExists(username, planId))
        {
            String saveStr = "INSERT INTO users.therapeutical_plans VALUES (?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, username);
                st.setInt(2, convertedId);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeAssociation(String username, String planId) {
        int convertedId = Integer.parseInt(planId);

        if (ifExists(username, planId))
        {
            String query = "DELETE FROM users.therapeutical_plans WHERE username = ? AND plan_id = ?";

            try {
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, username);
                st.setInt(2, convertedId);
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
