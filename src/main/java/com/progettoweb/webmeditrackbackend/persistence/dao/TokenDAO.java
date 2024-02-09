package com.progettoweb.webmeditrackbackend.persistence.dao;

import com.progettoweb.webmeditrackbackend.persistence.model.Token;

public interface TokenDAO {
    public Token findByPrimaryKey(String id);
    public void saveOrUpdate(Token token);
    public void delete(Token token);
}
