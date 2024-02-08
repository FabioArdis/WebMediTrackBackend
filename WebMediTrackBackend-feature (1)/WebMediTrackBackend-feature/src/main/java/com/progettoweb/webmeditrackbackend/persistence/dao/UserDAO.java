package com.progettoweb.webmeditrackbackend.persistence.dao;

import com.progettoweb.webmeditrackbackend.persistence.model.User;

import java.util.List;

public interface UserDAO<T> {
    public List<T> findAll();
    public T findByPrimaryKey(String username);
    public void saveOrUpdate(T user);
    public void delete(T user);
}
