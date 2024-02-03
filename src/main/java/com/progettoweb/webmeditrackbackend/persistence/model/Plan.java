package com.progettoweb.webmeditrackbackend.persistence.model;

public class Plan {
    private int id;
    private String name;
    private int length;
    private String type;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getLength() {
        return length;
    }
    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setType(String type) {
        this.type = type;
    }
}
