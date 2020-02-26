package com.cdanner.b2wsw.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiSearch {

    private List<SwapiPlanet> results;

    public List<SwapiPlanet>getResults() {
        return results;
    }

    public void setResults(List<SwapiPlanet>results) {
        this.results = results;
    }
}