package com.example.wclchat

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AttractionsRepository {

    fun getAttractions(query: String, callback: (String?) -> Unit) {
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        RetrofitClient.overpassService.getAttractions(encodedQuery)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = response.body()?.string()
                        callback(json)
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(null)
                }
            })
    }

    fun parseAttractions(json: String?): List<Attraction>? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<OverpassResponse>() {}.type
            val response = gson.fromJson<OverpassResponse>(json, type)
            response.elements.mapNotNull { element ->
                val tags = element.tags ?: return@mapNotNull null
                val center = element.center ?: return@mapNotNull null
                val name = tags["name"] ?: return@mapNotNull null
                val type = when {
                    "leisure" in tags && tags["leisure"] == "park" -> AttractionType.PARK
                    "historic" in tags && tags["historic"] == "monument" -> AttractionType.MONUMENT
                    "tourism" in tags && tags["tourism"] == "museum" -> AttractionType.MUSEUM
                    "amenity" in tags && tags["amenity"] == "theatre" -> AttractionType.THEATER
                    else -> return@mapNotNull null
                }
                Attraction(name, type, center.lat, center.lon)
            }
        } catch (e: Exception) {
            null // В случае ошибки парсинга возвращаем null
        }
    }
}
