package com.progettoweb.webmeditrackbackend.persistence.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Token {
    String id;
    boolean authorized;
    LocalDateTime expiration_time;

    public String getId() {
        return id;
    }

    public boolean isAuthorized() {
        return authorized;
    }
    public boolean isExpired() {return LocalDateTime.now().isAfter(expiration_time);}

    public java.sql.Date getExpirationTime() { return Date.valueOf(expiration_time.toLocalDate());}

    public void setId(String id) {
        this.id = id;
    }
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
    public void setExpiration_time(LocalDateTime expiration_time) {this.expiration_time = expiration_time.plusHours(24);}
}
