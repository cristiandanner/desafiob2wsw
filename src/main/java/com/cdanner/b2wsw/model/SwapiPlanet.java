package com.cdanner.b2wsw.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPlanet {
    
    private String name;
    private String[] films;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFilms() {
        return films;
    }

    public void setFilms(String[] films) {
        this.films = films;
    }

    public int getFilmsCount() {
        return films == null ? 0 : films.length;
    }
}