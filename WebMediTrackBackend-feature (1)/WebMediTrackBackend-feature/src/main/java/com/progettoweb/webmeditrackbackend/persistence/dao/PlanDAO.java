package com.progettoweb.webmeditrackbackend.persistence.dao;

import com.progettoweb.webmeditrackbackend.persistence.model.Plan;

import java.util.List;

public interface PlanDAO {
    public List<Plan> findByUser(String username);
    public Plan findById(int id);
    public int saveOrUpdate(Plan plan);
    public void delete(Plan plan);
}
