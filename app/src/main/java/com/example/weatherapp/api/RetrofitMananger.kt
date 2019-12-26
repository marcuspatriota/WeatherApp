package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Para o rotrofit é preciso que exista somente uma instancia (singanto), por isso estamos usando o tipo objeto
// não consigo instancia esse objeto
object RetrofitMananger {


    private val retrofitInstance = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //pode ser assim
    //fun getWeatherService()= retrofitInstance.create(WeatherService::class.java)

    fun getWeatherService(): WeatherService{
        return retrofitInstance.create(WeatherService::class.java)
    }
}