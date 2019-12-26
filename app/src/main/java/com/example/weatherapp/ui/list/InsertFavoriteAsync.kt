package com.example.weatherapp.ui.list

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.weatherapp.data.RoomManager
import com.example.weatherapp.entity.City
import com.example.weatherapp.entity.Favorite

//class reponsavel por rodar processo em background
class InsertFavoriteAsync(context: Context)
    : AsyncTask<City, Void, Boolean>(){
    private val db = RoomManager.getInstance(context)

    override fun doInBackground(vararg params: City?): Boolean {
        val paramFavorite = Favorite(params[0]!!.id,params[0]!!.name)
        var favorite = db?.getCityDao()?.favoriteById(paramFavorite.id)
        return if (favorite != null){
            db?.getCityDao()?.deleteFavorite(paramFavorite)
            Log.d("InsertFavoriteAsync", "Registro já exite")
            false
        }else{
            db?.getCityDao()?.insertFavorite(paramFavorite)
            Log.d("InsertFavoriteAsync", "Registro inserido")
            true
        }

    }

}