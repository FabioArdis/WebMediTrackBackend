package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.PlanDAO;
import com.progettoweb.webmeditrackbackend.persistence.model.Plan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanDAOPostgres implements PlanDAO {
    Connection con;
    public PlanDAOPostgres(Connection con) { this.con = con; }
    @Override
    public List<Plan> findByUser(String username) {
        List<Plan> plans = new ArrayList<>();
        String query = "SELECT plan_id FROM users.therapeutical_plans WHERE username = ?";

        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            while (rs.next())
                plans.add(findById(rs.getInt("plan_id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plans;
    }

    @Override
    public Plan findById(int id) {
        Plan plan = null;
        String query = "SELECT * FROM plans.plans WHERE plan_id = ?";

        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                plan = new Plan();

                plan.setId(rs.getInt("plan_id"));
                plan.setName(rs.getString("name"));
                plan.setLength(rs.getInt("length"));
                plan.setType(rs.getString("type"));

                String medQuery = "SELECT medicine_id FROM plans.plansMedicines WHERE plan_id = ?";
                PreparedStatement medSt = con.prepareStatement(medQuery);
                medSt.setInt(1, plan.getId());
                ResultSet medRs = medSt.executeQuery();

                while (medRs.next())
                {
                    plan.addMedicineId(medRs.getInt("medicine_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plan;
    }

    @Override
    public int saveOrUpdate(Plan plan) {
        if (findById(plan.getId()) == null)
        {
            String saveStr = "INSERT INTO plans.plans (name, length, type) VALUES (?, ?, ?)";
            PreparedStatement st;

            try {
                st = con.prepareStatement(saveStr, Statement.RETURN_GENERATED_KEYS);

                st.setString(1, plan.getName());
                st.setInt(2, plan.getLength());
                st.setString(3, plan.getType());

                int affectedRows = st.executeUpdate();

                if (affectedRows > 0)
                {
                    ResultSet generatedKeys = st.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        plan.setId(generatedKeys.getInt(1));
                        return generatedKeys.getInt(1);
                    }
                    else
                        throw new SQLException("Failed to retrieve ID.");
                } else {
                    throw new SQLException("Failed to insert plan. No rows affected.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String updateStr = "UPDATE plans.plans SET name = ?, "
                    + "length = ?, "
                    + "type = ? "
                    + "WHERE plan_id = ?";

            PreparedStatement st;

            try {
                st = con.prepareStatement(updateStr);

                st.setString(1, plan.getName());
                st.setInt(2, plan.getLength());
                st.setString(3, plan.getType());
                st.setInt(4, plan.getId());

                st.executeUpdate();

                return plan.getId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void delete(Plan plan) {
        String query = "DELETE FROM plans.plans WHERE plan_id = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, plan.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
