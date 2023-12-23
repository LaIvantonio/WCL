package com.example.wclchat

import java.io.Serializable

data class Attraction(
    val name: String,
    val type: AttractionType,
    val latitude: Double,
    val longitude: Double
) : Serializable

enum class AttractionType {
    MONUMENT,
    MUSEUM,
    PARK,
    THEATER
}
