package com.progettoweb.webmeditrackbackend.persistence.model;

import java.util.Date;

public class User {
    private String username;
    private String email;
    private String password;
    private String name;
    private String surname;
    private Date birthDate;

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getFullName() { return name + " " + surname; }
    public Date getBirthDate() { return birthDate; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}
