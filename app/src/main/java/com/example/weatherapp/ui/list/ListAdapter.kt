package com.example.weatherapp.ui.list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.RoomManager
import com.example.weatherapp.entity.City
import com.example.weatherapp.entity.Favorite
import kotlinx.android.synthetic.main.activity_settings.view.*
import kotlinx.android.synthetic.main.row_weather_layout.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list: List<City>? = null

    private var db : RoomManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_weather_layout,
            parent, false)
        db = RoomManager.getInstance(parent.context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {
            holder.itemView.btnFavorite.setOnClickListener{btnFan ->
                if (InsertFavoriteAsync(holder.itemView.context).execute(it[position]).get()){
                    val drawable = ContextCompat.getDrawable(holder.itemView.context,android.R.drawable.btn_star_big_on)
                    holder.itemView.btnFavorite.setImageDrawable(drawable)
                }else{
                    val drawable = ContextCompat.getDrawable(holder.itemView.context,android.R.drawable.btn_star_big_off)
                    holder.itemView.btnFavorite.setImageDrawable(drawable)
                }
            }
            holder.bind(it[position])
        }

    }
    fun updateData(list: List<City>?){
        this.list= list
        notifyDataSetChanged()
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){

        fun bind(city: City){
            itemView.txtCity.text = "${city.name}, ${city.sys.country}"
            itemView.txtTemp.text= city.main.temp.toInt().toString()
            itemView.txtSituacao.text= city.weather[0].description
            itemView.txtDetalhe.text = "wind ${city.wind.speed} | clouds ${city.main.humidity}% | ${city.main.pressure}hpa"
            //salvando em cache
            if (city.weather.isNotEmpty()){
                Glide.with(itemView.context)
                    .load("http://openweathermap.org/img/w/${city.weather[0].icon}.png")
                   // .placeholderDrawable(R.drawable.ic_launcher_background) coloca uma imagem antes de carregar
                    .into(itemView.imgTemp)
            }
        }

    }
}
