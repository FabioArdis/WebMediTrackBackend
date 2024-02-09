package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.AssociationDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanMedicineDAOPostgres implements AssociationDAO {
    Connection con;
    public PlanMedicineDAOPostgres(Connection con) { this.con = con; }
    @Override
    public boolean ifExists(String planId, String medicineId) {
        int cFirst = Integer.parseInt(planId);
        int cSecond = Integer.parseInt(medicineId);

        String query = "SELECT COUNT(*) FROM plans.plansMedicines WHERE plan_id = ? AND medicine_id = ?";

        try (PreparedStatement st = con.prepareStatement(query))
        {
            st.setInt(1, cFirst);
            st.setInt(2, cSecond);

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
    public void addAssociation(String planId, String medicineId) {
        int cFirst = Integer.parseInt(planId);
        int cSecond = Integer.parseInt(medicineId);

        if (!ifExists(planId, medicineId))
        {
            String saveStr = "INSERT INTO plans.plansMedicines VALUES (?, ?)";
            PreparedStatement st;

            try {
                st = con.prepareStatement(saveStr);
                st.setInt(1, cFirst);
                st.setInt(2, cSecond);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeAssociation(String planId, String medicineId) {
        int cFirst = Integer.parseInt(planId);
        int cSecond = Integer.parseInt(medicineId);

        if (ifExists(planId, medicineId))
        {
            String query = "DELETE FROM plans.plansMedicines WHERE plan_id = ? AND medicine_id = ?";

            try {
                PreparedStatement st = con.prepareStatement(query);
                st.setInt(1, cFirst);
                st.setInt(2, cSecond);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
