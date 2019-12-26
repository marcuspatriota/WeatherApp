package com.example.weatherapp.api

import com.example.weatherapp.entity.FindResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("find")
    fun find(
        @Query ("q")
        city:String,
        @Query ("appId")
        appId:String,
        @Query("lang")
        lang:String,
        @Query ("units")
        unit: String
    ):Call<FindResult>

    @GET("group")
    fun findByFavorite(
        @Query ("id")
        idCity:String,
        @Query ("appId")
        appId: String,
        @Query("lang")
        lang:String,
        @Query ("units")
        unit: String
    ):Call<FindResult>
}