package com.cdanner.b2wsw.service;

import java.util.List;

import com.cdanner.b2wsw.model.Planet;

public interface IPlanetService {
	
    Planet getById(String id);
       
    List<Planet> getAll(String name, int page, int per_page);
    
    void deleteById(String id);
    
    Planet getByName(String name);
    
    Planet insert(Planet planet);
    
    Planet update(Planet planet);
}

