package com.progettoweb.webmeditrackbackend.persistence.model;

import java.util.List;

public class Patient extends User {
    private String cf;
    private String TScode;
    private List<Doctor> doctors;

    public String getCf() { return cf; }
    public String getTScode() { return TScode; }
    public List<Doctor> getDoctors() { return doctors; }

    public void setCf(String cf) { this.cf = cf; }
    public void setTScode(String TScode) { this.TScode = TScode; }
    public void addDoctor(Doctor doctor) { this.doctors.add(doctor); }
    public void removeDoctor(Doctor doctor) { this.doctors.remove(doctor); }
}
