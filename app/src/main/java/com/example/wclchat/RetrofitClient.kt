package com.example.wclchat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Создаем базовый URL для Overpass API
    private const val OVERPASS_API_BASE_URL = "https://overpass-api.de/api/"

    // Создаем базовый URL для OpenWeatherMap API
    private const val OPENWEATHERMAP_API_BASE_URL = "https://api.openweathermap.org/"

    // Создаем экземпляр Retrofit для Overpass API
    private val retrofitOverpass = Retrofit.Builder()
        .baseUrl(OVERPASS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Создаем экземпляр Retrofit для OpenWeatherMap API
    private val retrofitOpenWeatherMap = Retrofit.Builder()
        .baseUrl(OPENWEATHERMAP_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Создаем сервисы для каждого API
    val overpassService: OverpassService = retrofitOverpass.create(OverpassService::class.java)
    val weatherService: WeatherService = retrofitOpenWeatherMap.create(WeatherService::class.java)
}
