package com.iffecode.api_integration_v5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v1/search")
    Call<GeocodingResponse> getLocation(
            @Query("name") String city,
            @Query("count") int count
    );

    @GET("v1/forecast")
    Call<OpenMeteoWeather> getWeather(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current") String current
    );

    @GET("v3.1/name/{country}")
    Call<List<Country>> getCountry(
            @Path("country") String country
    );


}
