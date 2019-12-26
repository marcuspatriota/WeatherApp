package com.example.weatherapp.ui.list

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.common.Constants
import com.example.weatherapp.data.RoomManager
import com.example.weatherapp.entity.City
import com.example.weatherapp.entity.Favorite
import kotlinx.android.synthetic.main.row_weather_layout.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list: List<City>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_weather_layout,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {
            /**
            holder.itemView.btnFavorite.setOnClickListener{btnFan ->
                val db = RoomManager.getInstance(holder.itemView.context)
                val paramFavorite = Favorite(it[position].id,it[position].name)
                var favorite = db?.getCityDao()?.favoriteById(paramFavorite.id)
                if (favorite != null){
                    db?.getCityDao()?.deleteFavorite(paramFavorite)
                    val drawable = ContextCompat.getDrawable(holder.itemView.context,android.R.drawable.btn_star_big_on)
                    holder.itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("InsertFavoriteAsync", "Registro já exite")
                }else{
                    db?.getCityDao()?.insertFavorite(paramFavorite)
                    val drawable = ContextCompat.getDrawable(holder.itemView.context,android.R.drawable.btn_star_big_off)
                    holder.itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("InsertFavoriteAsync", "Registro inserido")
                    notifyDataSetChanged()
                }

                if (InsertFavoriteAsync(holder.itemView.context).execute(it[position]).get()){

                }else{
                    val drawable = ContextCompat.getDrawable(holder.itemView.context,android.R.drawable.btn_star_big_off)
                    holder.itemView.btnFavorite.setImageDrawable(drawable)
                    notifyDataSetChanged()
                }

            }*/
            holder.bind(it[position])
        }

    }
    fun updateData(list: List<City>?){
        this.list= list
        notifyDataSetChanged()
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        private val sp : SharedPreferences by lazy {
            itemView.context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        }
        private val db = RoomManager.getInstance(itemView.context)
        fun bind(city: City){
            itemView.txtCity.text = "${city.name}, ${city.sys.country}"
            itemView.txtTemp.text= city.main.temp.toInt().toString()
            itemView.txtSituacao.text= city.weather[0].description
            itemView.txtDetalhe.text = "wind ${city.wind.speed} | clouds ${city.main.humidity}% | ${city.main.pressure}hpa"
            itemView.txtUnit.text = getSimboloTemp()

            val paramFavorite = Favorite(city.id,city.name)
            var favorite = db?.getCityDao()?.favoriteById(paramFavorite.id)

            if (favorite!=null){
                val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                itemView.btnFavorite.setImageDrawable(drawable)
            }else itemView.btnFavorite.setImageDrawable(ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off))

            itemView.btnFavorite.setOnClickListener{btnFan ->
                favorite = db?.getCityDao()?.favoriteById(paramFavorite.id)
                if (favorite != null){
                    db?.getCityDao()?.deleteFavorite(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                   itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("InsertFavoriteAsync", "Registro já exite")
                }else{
                    db?.getCityDao()?.insertFavorite(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off)
                    itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("InsertFavoriteAsync", "Registro inserido")
                }
            }

            //salvando em cache
            if (city.weather.isNotEmpty()){
                Glide.with(itemView.context)
                    .load("http://openweathermap.org/img/w/${city.weather[0].icon}.png")
                   // .placeholderDrawable(R.drawable.ic_launcher_background) coloca uma imagem antes de carregar
                    .into(itemView.imgTemp)
            }
        }
        private fun getSimboloTemp():String{
             return if (sp.getBoolean(Constants.PREFS_TEMP, true)) "ºC" else "ºF"

        }


    }
}
