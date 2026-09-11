package com.iffecode.api_integration_v5;

import java.util.List;

public class Country {

    private CountryName name;
    private List<String> capital;
    private long population;
    private String region;

    public Country() {
    }

    public CountryName getName() {
        return name;
    }

    public List<String> getCapital() {
        return capital;
    }

    public long getPopulation() {
        return population;
    }

    public String getRegion() {
        return region;
    }
}
