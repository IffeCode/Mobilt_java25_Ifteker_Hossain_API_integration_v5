package com.iffecode.api_integration_v5

import android.os.Bundle
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

class WeatherFragment : Fragment() {
    private lateinit var cityInput: EditText

    private lateinit var cityText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var conditionText: TextView
    private lateinit var windText: TextView

    private lateinit var searchWeatherBtn: Button
    private lateinit var weatherToHomeBtn: Button
    private lateinit var countryBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_weather,
            container,
            false
        )

        cityInput = view.findViewById(R.id.cityInput)

        cityText = view.findViewById(R.id.cityText)
        temperatureText = view.findViewById(R.id.temperatureText)
        conditionText = view.findViewById(R.id.conditionText)
        windText = view.findViewById(R.id.windText)

        searchWeatherBtn = view.findViewById(R.id.searchWeatherBtn)
        weatherToHomeBtn = view.findViewById(R.id.weatherToHomeBtn)
        countryBtn = view.findViewById(R.id.countryBtn)


        val geocodingRetrofit = Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val geocodingApi =
            geocodingRetrofit.create<ApiService>(ApiService::class.java)


        val weatherRetrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi =
            weatherRetrofit.create<ApiService>(ApiService::class.java)


        searchWeatherBtn.setOnClickListener {

            val city = cityInput!!.text.toString().trim()

            if (city.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Please enter a city",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            geocodingApi.getLocation(city, 1)
                .enqueue(object : Callback<GeocodingResponse> {

                    override fun onResponse(
                        call: Call<GeocodingResponse>,
                        response: Response<GeocodingResponse>
                    ) {

                        if (
                            response.isSuccessful &&
                            response.body() != null &&
                            !response.body()!!.results.isNullOrEmpty()
                        ) {

                            val location =
                                response.body()!!.results!![0]

                            val latitude =
                                location.latitude

                            val longitude =
                                location.longitude

                            weatherApi.getWeather(
                                latitude,
                                longitude,
                                "temperature_2m,wind_speed_10m,weather_code"
                            ).enqueue(object : Callback<OpenMeteoWeather> {

                                override fun onResponse(
                                    call: Call<OpenMeteoWeather>,
                                    response: Response<OpenMeteoWeather>
                                ) {

                                    if (
                                        response.isSuccessful &&
                                        response.body() != null &&
                                        response.body()!!.current != null
                                    ) {

                                        val weather =
                                            response.body()!!

                                        val current =
                                            weather.current!!

                                        cityText!!.text =
                                            location.name

                                        temperatureText!!.text =
                                            "Temperature: ${current.temperature_2m} °C"

                                        conditionText!!.text =
                                            "Condition: ${
                                                getWeatherDescription(
                                                    current.weather_code
                                                )
                                            }"

                                        windText!!.text =
                                            "Wind: ${current.wind_speed_10m} km/h"

                                    } else {

                                        Toast.makeText(
                                            requireContext(),
                                            "Could not get weather data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<OpenMeteoWeather>,
                                    t: Throwable
                                ) {

                                    Toast.makeText(
                                        requireContext(),
                                        "Weather API connection failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "City not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<GeocodingResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            requireContext(),
                            "Location API connection failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        countryBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val navController =
                findNavController(v!!)
            navController.navigate(R.id.countryFragment)
        })


        weatherToHomeBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val activity =
                requireActivity() as MainActivity2
            activity.showHome()
        })

        return view
    }

    private fun getWeatherDescription(weatherCode: Int): String {
        when (weatherCode) {
            0 -> return "Clear sky"

            1 -> return "Mainly clear"

            2 -> return "Partly cloudy"

            3 -> return "Overcast"

            45, 48 -> return "Fog"

            51, 53, 55 -> return "Drizzle"

            61, 63, 65 -> return "Rain"

            71, 73, 75 -> return "Snow"

            80, 81, 82 -> return "Rain showers"

            95 -> return "Thunderstorm"

            96, 99 -> return "Thunderstorm with hail"

            else -> return "Unknown"
        }
    }
}