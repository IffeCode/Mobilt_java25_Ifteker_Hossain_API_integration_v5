package com.iffecode.api_integration_v5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherFragment extends Fragment {

    private EditText cityInput;

    private TextView cityText;
    private TextView temperatureText;
    private TextView conditionText;
    private TextView windText;

    private Button searchWeatherBtn;
    private Button weatherToHomeBtn;
    private Button countryBtn;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_weather,
                container,
                false
        );

        cityInput = view.findViewById(R.id.cityInput);

        cityText = view.findViewById(R.id.cityText);
        temperatureText = view.findViewById(R.id.temperatureText);
        conditionText = view.findViewById(R.id.conditionText);
        windText = view.findViewById(R.id.windText);

        searchWeatherBtn = view.findViewById(R.id.searchWeatherBtn);
        weatherToHomeBtn = view.findViewById(R.id.weatherToHomeBtn);
        countryBtn = view.findViewById(R.id.countryBtn);


        Retrofit geocodingRetrofit = new Retrofit.Builder()
                .baseUrl("https://geocoding-api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService geocodingApi =
                geocodingRetrofit.create(ApiService.class);


        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService weatherApi =
                weatherRetrofit.create(ApiService.class);


        searchWeatherBtn.setOnClickListener(v -> {

            String city =
                    cityInput.getText().toString().trim();

            if (city.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Please enter a city",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            geocodingApi.getLocation(city, 1)
                    .enqueue(new Callback<GeocodingResponse>() {

                        @Override
                        public void onResponse(
                                Call<GeocodingResponse> call,
                                Response<GeocodingResponse> response) {

                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body().getResults() != null
                                    && !response.body().getResults().isEmpty()) {

                                Geocoding location =
                                        response.body()
                                                .getResults()
                                                .get(0);

                                double latitude =
                                        location.getLatitude();

                                double longitude =
                                        location.getLongitude();

                                weatherApi.getWeather(
                                                latitude,
                                                longitude,
                                                "temperature_2m,wind_speed_10m,weather_code"
                                        )
                                        .enqueue(new Callback<OpenMeteoWeather>() {

                                            @Override
                                            public void onResponse(
                                                    Call<OpenMeteoWeather> call,
                                                    Response<OpenMeteoWeather> response) {

                                                if (response.isSuccessful()
                                                        && response.body() != null
                                                        && response.body().getCurrent() != null) {

                                                    OpenMeteoWeather weather =
                                                            response.body();

                                                    CurrentWeather current =
                                                            weather.getCurrent();

                                                    cityText.setText(
                                                            location.getName()
                                                    );

                                                    temperatureText.setText(
                                                            "Temperature: "
                                                                    + current.getTemperature_2m()
                                                                    + " °C"
                                                    );

                                                    conditionText.setText(
                                                            "Condition: "
                                                                    + getWeatherDescription(
                                                                    current.getWeather_code()
                                                            )
                                                    );

                                                    windText.setText(
                                                            "Wind: "
                                                                    + current.getWind_speed_10m()
                                                                    + " km/h"
                                                    );

                                                } else {

                                                    Toast.makeText(
                                                            requireContext(),
                                                            "Could not get weather data",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(
                                                    Call<OpenMeteoWeather> call,
                                                    Throwable t) {

                                                Toast.makeText(
                                                        requireContext(),
                                                        "Weather API connection failed",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        });

                            } else {

                                Toast.makeText(
                                        requireContext(),
                                        "City not found",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<GeocodingResponse> call,
                                Throwable t) {

                            Toast.makeText(
                                    requireContext(),
                                    "Location API connection failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });

        countryBtn.setOnClickListener(v -> {

            NavController navController =
                    Navigation.findNavController(v);

            navController.navigate(R.id.countryFragment);
        });


        weatherToHomeBtn.setOnClickListener(v -> {

            MainActivity2 activity =
                    (MainActivity2) requireActivity();

            activity.showHome();
        });

        return view;
    }

    private String getWeatherDescription(int weatherCode) {

        switch (weatherCode) {

            case 0:
                return "Clear sky";

            case 1:
                return "Mainly clear";

            case 2:
                return "Partly cloudy";

            case 3:
                return "Overcast";

            case 45:
            case 48:
                return "Fog";

            case 51:
            case 53:
            case 55:
                return "Drizzle";

            case 61:
            case 63:
            case 65:
                return "Rain";

            case 71:
            case 73:
            case 75:
                return "Snow";

            case 80:
            case 81:
            case 82:
                return "Rain showers";

            case 95:
                return "Thunderstorm";

            case 96:
            case 99:
                return "Thunderstorm with hail";

            default:
                return "Unknown";
        }
    }
}