package com.iffecode.api_integration_v5;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("weather/{city}")
    Call<Weather> getWeather(
            @Path("city") String city
    );


}
