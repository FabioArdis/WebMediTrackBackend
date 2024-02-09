package com.progettoweb.webmeditrackbackend.persistence.dao.postgres;

import com.progettoweb.webmeditrackbackend.persistence.dao.TokenDAO;
import com.progettoweb.webmeditrackbackend.persistence.model.Token;
import com.progettoweb.webmeditrackbackend.persistence.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

public class TokenDAOPostgres implements TokenDAO {
    Connection con;

    public TokenDAOPostgres(Connection con) { this.con = con; }

    @Override
    public Token findByPrimaryKey(String id) {
        Token token = null;
        String query = "SELECT * FROM users.token_patients WHERE id = ?";
        try
        {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                token = new Token();

                token.setId(rs.getString("id"));
                token.setAuthorized(rs.getBoolean("auth"));
                token.setExpiration_time(rs.getDate("expiration_time").toLocalDate().atStartOfDay());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return token;
    }

    @Override
    public void saveOrUpdate(Token token) {
        if (findByPrimaryKey(token.getId()) == null)
        {
            String saveStr = "INSERT INTO users.token_patients VALUES (?, ?, ?)";
            PreparedStatement st;

            try
            {
                st = con.prepareStatement(saveStr);
                st.setString(1, token.getId());
                st.setBoolean(2, token.isAuthorized());
                st.setDate(3, token.getExpirationTime());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String updateStr = "UPDATE users.token_patients SET id = ?, "
                    + "auth = ?, "
                    + "expiration_time = ?";

            PreparedStatement st;

            try {
                st = con.prepareStatement(updateStr);

                st.setString(1, token.getId());
                st.setBoolean(2, token.isAuthorized());
                st.setDate(3, token.getExpirationTime());
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Token token) {
        String query = "DELETE FROM users.token_patients WHERE id = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, token.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
