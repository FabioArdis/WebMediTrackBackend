package com.progettoweb.webmeditrackbackend.persistence.dao;

public interface AssociationDAO {
    public boolean ifExists(String first, String second);
    public void addAssociation(String first, String second);
    public void removeAssociation(String first, String second);
}
