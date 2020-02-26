package com.cdanner.b2wsw.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cdanner.b2wsw.model.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String> {
    
	Planet getByName(String name);
}
