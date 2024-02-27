package com.example.flagquest

import com.example.flagquest.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v3.1/all?fields=name,flags")
    suspend fun getAllCountries(): Response<List<Country>>
}
