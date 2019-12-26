package com.example.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.entity.Favorite

@Database(entities = [Favorite::class],version = 1)
abstract class  RoomManager:RoomDatabase() {
    abstract fun getCityDao() :CityDao

    // implemente do singunto
    companion object{
        var INSTANCE : RoomManager? = null

        fun getInstance(context: Context):RoomManager?{
            synchronized(RoomManager::class.java) {
                if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            RoomManager::class.java,
                            "weather.db"
                        ).allowMainThreadQueries()
                            .build()
                }
                return INSTANCE
            }
        }
    }

}
