package com.iffecode.api_integration_v5

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryFragment : Fragment() {
    private lateinit var countryInput: EditText

    private lateinit var countryNameText: TextView
    private lateinit var capitalText: TextView
    private lateinit var populationText: TextView
    private lateinit var regionText: TextView

    private lateinit var searchCountryBtn: Button
    private lateinit var countryToWeatherBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_country,
            container,
            false
        )

        countryInput = view.findViewById(R.id.countryInput)

        countryNameText = view.findViewById(R.id.countryNameText)
        capitalText = view.findViewById(R.id.capitalText)
        populationText = view.findViewById(R.id.populationText)
        regionText = view.findViewById(R.id.regionText)

        searchCountryBtn = view.findViewById(R.id.searchCountryBtn)
        countryToWeatherBtn = view.findViewById(R.id.countryToWeatherBtn)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://countries.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<ApiService>(
            ApiService::class.java
        )

        searchCountryBtn.setOnClickListener {

            val country = countryInput.text.toString().trim()

            if (country.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Please enter a country",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            apiService.getCountry(country)
                .enqueue(object : Callback<MutableList<Country>> {

                    override fun onResponse(
                        call: Call<MutableList<Country>>,
                        response: Response<MutableList<Country>>
                    ) {

                        if (
                            response.isSuccessful &&
                            response.body() != null &&
                            response.body()!!.isNotEmpty()
                        ) {

                            val countryData =
                                response.body()!![0]

                            countryNameText.text =
                                "Country: ${countryData.name}"

                            if (
                                !countryData.capital.isNullOrEmpty()
                            ) {
                                capitalText.text =
                                    "Capital: ${countryData.capital}"
                            } else {
                                capitalText.text =
                                    "Capital: No information"
                            }

                            populationText.text =
                                "Population: ${countryData.population}"

                            regionText.text =
                                "Region: ${countryData.region}"

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Country not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<MutableList<Country>>,
                        t: Throwable
                    ) {

                        Log.e(
                            "COUNTRY_API",
                            "API connection failed",
                            t
                        )

                        Toast.makeText(
                            requireContext(),
                            "API error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

        countryToWeatherBtn.setOnClickListener {

            val navController =
                findNavController(it)

            navController.popBackStack()
        }

        return view
    }
}