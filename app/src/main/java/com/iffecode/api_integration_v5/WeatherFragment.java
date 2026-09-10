package com.iffecode.api_integration_v5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    private Button searchWeatherBtn;
    private Button weatherToHomeBtn;

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

        searchWeatherBtn = view.findViewById(R.id.searchWeatherBtn);
        weatherToHomeBtn = view.findViewById(R.id.weatherToHomeBtn);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://goweather.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

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

            apiService.getWeather(city).enqueue(new Callback<Weather>() {

                @Override
                public void onResponse(
                        Call<Weather> call,
                        Response<Weather> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        Weather weather = response.body();

                        cityText.setText(city);

                        temperatureText.setText(
                                "Temperature: " + weather.getTemperature()
                        );

                        conditionText.setText(
                                "Condition: " + weather.getDescription()
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
                        Call<Weather> call,
                        Throwable t) {

                    Toast.makeText(
                            requireContext(),
                            "API connection failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });


        weatherToHomeBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });

        return view;
    }
}