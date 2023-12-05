package com.example.wclchat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
}
