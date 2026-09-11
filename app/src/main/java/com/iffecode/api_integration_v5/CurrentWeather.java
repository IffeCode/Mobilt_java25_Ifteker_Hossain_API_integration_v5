package com.iffecode.api_integration_v5;

public class CurrentWeather {

    private double temperature_2m;
    private double wind_speed_10m;
    private int weather_code;

    public CurrentWeather() {
    }

    public double getTemperature_2m() {
        return temperature_2m;
    }

    public double getWind_speed_10m() {
        return wind_speed_10m;
    }

    public int getWeather_code() {
        return weather_code;
    }
}
