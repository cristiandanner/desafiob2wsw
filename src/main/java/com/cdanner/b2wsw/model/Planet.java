package com.cdanner.b2wsw.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "planets")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Planet {
	
	@Id private String id;
    @NotNull @Indexed(unique = true) private String name;
    @NotNull private String climate;
    @NotNull private String terrain;
    @Transient private int filmsCount;

    public Planet() {}

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public int getFilmsCount() {
        return filmsCount;
    }

    public void setFilmsCount(int filmsCount) {
        this.filmsCount = filmsCount;
    }
}
