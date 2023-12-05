package com.example.wclchat

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {

    fun getCurrentWeather(city: String, apiKey: String, callback: (WeatherResponse?) -> Unit) {
        RetrofitClient.weatherService.getCurrentWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
