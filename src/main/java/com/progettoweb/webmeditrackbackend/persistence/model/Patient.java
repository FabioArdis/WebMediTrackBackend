package com.progettoweb.webmeditrackbackend.persistence.model;

import com.progettoweb.webmeditrackbackend.persistence.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient extends User {
    private String cf;
    private String TScode;
    private List<String> doctorUsernames;
    private List<Doctor> doctors;

    public String getCf() { return cf; }
    public String getTScode() { return TScode; }
    public List<String> getDoctorUsername() { return doctorUsernames; }
    public List<Doctor> getDoctors() { return doctors; }

    public void setCf(String cf) { this.cf = cf; }
    public void setTScode(String TScode) { this.TScode = TScode; }
    public void addDoctorUsername(String username) {
        if (doctorUsernames == null) {
            doctorUsernames = new ArrayList<>();
        }
        if (!doctorUsernames.contains(username))
            this.doctorUsernames.add(username);
    }
    public void loadDoctorsDetails()
    {
        if (doctors == null) {
            doctors = new ArrayList<>();
        }
        if (doctorUsernames == null) {
            doctorUsernames = new ArrayList<>();
        }
        doctors.clear();
        for (String doctorUsername : doctorUsernames)
        {
            Doctor doc = DBManager.getInstance().getDoctorDAO().findByPrimaryKey(doctorUsername);
            if (!doctors.contains(doc))
                doctors.add(doc);
        }
    }
    public void addDoctor(Doctor doctor) { this.doctors.add(doctor); }
    public void removeDoctor(Doctor doctor) { this.doctors.remove(doctor); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient patient = (Patient) obj;
        return Objects.equals(this.getUsername(), patient.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUsername());
    }
}
