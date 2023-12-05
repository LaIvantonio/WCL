package com.example.wclchat

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main
)

data class Weather(
    val main: String,
    val description: String
)

data class Main(
    val temp: Double
)
