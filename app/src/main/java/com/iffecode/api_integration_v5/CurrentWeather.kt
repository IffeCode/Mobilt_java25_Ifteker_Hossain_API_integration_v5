package com.iffecode.api_integration_v5

data class CurrentWeather(
    val temperature_2m: Double,
    val wind_speed_10m: Double,
    val weather_code: Int
)
