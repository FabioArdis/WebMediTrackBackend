package com.progettoweb.webmeditrackbackend.persistence.model;

import java.util.List;

public class Doctor extends User {
    private String cf;
    private String doctorId;
    private String spec;
    private String docAvailTime;
    private List<Patient> patients;

    public String getCf() { return cf; }
    public String getDoctorId() { return doctorId; }
    public String getSpec() { return spec; }
    public String getDocAvailTime() { return docAvailTime; }
    public List<Patient> getPatients() { return patients; }

    public void setCf(String cf) { this.cf = cf; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setSpec(String spec) { this.spec = spec; }
    public void setDocAvailTime(String docAvailTime) { this.docAvailTime = docAvailTime; }
    public void addPatient(Patient patient) { patients.add(patient); }
    public void removePatient(Patient patient) { patients.remove(patient); }
}
