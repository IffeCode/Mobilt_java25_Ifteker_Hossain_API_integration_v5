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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CountryFragment extends Fragment {


    private EditText countryInput;

    private TextView countryNameText;
    private TextView capitalText;
    private TextView populationText;
    private TextView regionText;

    private Button searchCountryBtn;
    private Button countryToWeatherBtn;

    public CountryFragment() {
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(
                R.layout.fragment_country,
                container,
                false
        );

        countryInput = view.findViewById(R.id.countryInput);

        countryNameText = view.findViewById(R.id.countryNameText);
        capitalText = view.findViewById(R.id.capitalText);
        populationText = view.findViewById(R.id.populationText);
        regionText = view.findViewById(R.id.regionText);

        searchCountryBtn = view.findViewById(R.id.searchCountryBtn);
        countryToWeatherBtn = view.findViewById(R.id.countryToWeatherBtn);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        searchCountryBtn.setOnClickListener(v -> {

            String country =
                    countryInput.getText().toString().trim();

            if (country.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Please enter a country",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            apiService.getCountry(country).enqueue(new Callback<List<Country>>() {

                @Override
                public void onResponse(
                        Call<List<Country>> call,
                        Response<List<Country>> response) {

                    if (response.isSuccessful()
                            && response.body() != null
                            && !response.body().isEmpty()) {

                        Country countryData = response.body().get(0);

                        countryNameText.setText(
                                "Country: " +
                                        countryData.getName().getCommon()
                        );

                        if (countryData.getCapital() != null
                                && !countryData.getCapital().isEmpty()) {

                            capitalText.setText(
                                    "Capital: " +
                                            countryData.getCapital().get(0)
                            );

                        } else {

                            capitalText.setText(
                                    "Capital: No information"
                            );
                        }

                        populationText.setText(
                                "Population: " +
                                        countryData.getPopulation()
                        );

                        regionText.setText(
                                "Region: " +
                                        countryData.getRegion()
                        );

                    } else {

                        Toast.makeText(
                                requireContext(),
                                "Country not found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<List<Country>> call,
                        Throwable t) {

                    Toast.makeText(
                            requireContext(),
                            "API connection failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });

        countryToWeatherBtn.setOnClickListener(v -> {

            NavController navController =
                    Navigation.findNavController(v);

            navController.popBackStack();
        });

        return view;
    }
}