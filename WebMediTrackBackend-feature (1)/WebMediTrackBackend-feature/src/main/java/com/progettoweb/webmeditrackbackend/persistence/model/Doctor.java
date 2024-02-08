package com.progettoweb.webmeditrackbackend.persistence.model;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor extends User {
    private String cf;
    private String doctorId;
    private String spec;
    private String docAvailTime;
    private List<String> patientUsernames;
    private List<Patient> patients;

    public String getCf() { return cf; }
    public String getDoctorId() { return doctorId; }
    public String getSpec() { return spec; }
    public String getDocAvailTime() { return docAvailTime; }
    public List<String> getPatientUsernames() { return patientUsernames; }
    public List<Patient> getPatients() { return patients; }

    public void setCf(String cf) { this.cf = cf; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setSpec(String spec) { this.spec = spec; }
    public void setDocAvailTime(String docAvailTime) { this.docAvailTime = docAvailTime; }
    public void addPatientUsername(String username) {
        if (patientUsernames == null) {
            patientUsernames = new ArrayList<>();
        }
        this.patientUsernames.add(username);
    }
    public void removePatientUsername(String username) {
        if (patientUsernames == null) {
            patientUsernames = new ArrayList<>();
        }
        this.patientUsernames.remove(username);
    }

    public void loadPatientsDetail()
    {
        if (patients == null) {
            patients = new ArrayList<>();
        }
        if (patientUsernames == null) {
            patientUsernames = new ArrayList<>();
        }
        patients.clear();
        for (String patientUsername : patientUsernames)
        {
            Patient pat = DBManager.getInstance().getPatientDAO().findByPrimaryKey(patientUsername);
            if (!patients.contains(pat))
                patients.add(pat);
        }
    }
    public void addPatient(Patient patient) { patients.add(patient); }
    public void removePatient(Patient patient) { patients.remove(patient); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Doctor doctor = (Doctor) obj;
        return Objects.equals(this.getUsername(), doctor.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUsername());
    }
}
