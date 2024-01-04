package com.progettoweb.webmeditrackbackend.persistence.dao;

import com.progettoweb.webmeditrackbackend.persistence.model.User;

import java.util.List;

public interface UserDAO {
    public List<User> findAll();
    public User findByPrimaryKey(String username);
    public void saveOrUpdate(User user);
    public void delete(User user);
}
