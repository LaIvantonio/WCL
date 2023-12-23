package com.example.wclchat

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassService {
    @GET("interpreter")
    fun getAttractions(
        @Query("data", encoded = true) data: String
    ): Call<ResponseBody>
}
