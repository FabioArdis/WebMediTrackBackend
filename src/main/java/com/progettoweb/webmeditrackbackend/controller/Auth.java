package com.progettoweb.webmeditrackbackend.controller;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@CrossOrigin(value = "https://localhost:4200", allowCredentials = "true")
public class Auth {
    private class AuthToken
    {
        String token;
        User user;

        public User getUser() { return user; }
        public String getToken() { return token; }

        public void setUser(User user) { this.user = user; }
        public void setToken(String token) { this.token = token; }
    }

    @PostMapping("/login")
    public AuthToken login(@RequestBody User user, HttpServletRequest req) throws Exception
    {
        String username = user.getUsername();
        String password = user.getPassword();
        String concat = username + ":" + password;
        String token = code64(concat);

        user = getUserByToken(token);

        if (user != null)
        {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            AuthToken auth = new AuthToken();
            auth.setToken(token);
            auth.setUser(user);

            return auth;
        }

        return null;
    }

    private User getUserByToken(String token)
    {
        if (token != null)
        {
            String decoded = decode64(token);
            String username = decoded.split(":")[0];
            String password = decoded.split(":")[1];
            /*User user = DBManager.getInstance().getUserDAO().findByPrimaryKey(username);

            if (user != null)
                if (user.getPassword().equals(password))
                    return user;*/
        }

        return null;
    }

    private String code64(String value) { return Base64.getEncoder().encodeToString(value.getBytes()); }
    private String decode64(String value) { return new String(Base64.getDecoder().decode(value.getBytes())); }
}
