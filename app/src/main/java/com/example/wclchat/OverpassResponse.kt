package com.example.wclchat

data class OverpassResponse(
    val elements: List<OverpassElement>
)

data class OverpassElement(
    val type: String,
    val id: Long,
    val lat: Double?,
    val lon: Double?,
    val tags: Map<String, String>?,
    val center: OverpassCenter?
)

data class OverpassCenter(
    val lat: Double,
    val lon: Double
)
