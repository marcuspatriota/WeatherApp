package com.example.weatherapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.FileDescriptor

//Classe referente a persistencia, tem mais funções, toString, Equals ...

data class FindResult(
    val list : List<City>

)
data class City(
    val id:Int,
    val name:String,
    val main: Main,
    val weather: List<Weather>,
    val sys: Sys,
    val wind: Wind
)

data class Main(
    val temp :Float,
    val humidity:Int,
    val pressure:Int
)

data class Weather(
    val icon:String,
    val description: String
)

data class Sys(
    val country:String
)

data class Wind(
    val speed:Float
)

@Entity(tableName = "TB_FAVORITE")
data class Favorite(
    //salvar id da API
    @PrimaryKey
    val id: Int,
    val name: String
)