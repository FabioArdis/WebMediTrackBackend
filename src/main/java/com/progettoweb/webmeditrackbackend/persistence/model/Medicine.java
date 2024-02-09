package com.progettoweb.webmeditrackbackend.persistence.model;

import java.util.Date;

public class Medicine {
    private int id;
    private String name;
    private int dosage;
    private String manufacturer;
    private int batchNumber;
    private Date expirationTime;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacture(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
