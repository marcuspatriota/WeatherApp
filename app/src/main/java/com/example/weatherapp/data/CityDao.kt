package com.example.weatherapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.entity.Favorite

@Dao
interface CityDao{

    @Insert
    fun insertFavorite(favorite: Favorite)

    @Delete
    fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM TB_FAVORITE WHERE id= :id")
    fun favoriteById(id:Int):Favorite

    @Query("SELECT * FROM TB_FAVORITE")
    fun allFavorite(): List<Favorite>

}