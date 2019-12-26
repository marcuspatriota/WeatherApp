package com.example.weatherapp.ui.list

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.api.RetrofitMananger
import com.example.weatherapp.common.Constants
import com.example.weatherapp.data.RoomManager
import com.example.weatherapp.entity.City
import com.example.weatherapp.entity.Favorite
import com.example.weatherapp.entity.FindResult
import com.example.weatherapp.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_weather_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListActivity : AppCompatActivity(), Callback<FindResult> {

    private val adapter: ListAdapter by lazy {
        ListAdapter()
    }
   private val db : RoomManager? by lazy{
       RoomManager.getInstance(this)
   }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        btn_buscar.setOnClickListener { getCities()}

    }

    private fun initRecyclerView(){
        rvWeather.adapter = adapter
    }
    //Retorna a cidades da pesquisa na API
    private fun getCities(){
        if (input_text_main.text.isNullOrBlank()){
            var list = db?.getCityDao()?.allFavorite()
            var aux:String = ""
            if (!list.isNullOrEmpty()){
                for (i in 0..list.size-1){
                    if (!aux.isNullOrBlank()){
                        aux = "$aux,${list[i].id}"
                    }else{
                        aux = "${list[i].id}"
                    }
                }
                val call = RetrofitMananger.getWeatherService()
                    .findByFavorite(aux,Constants.API_KEY, "metric")
                progressBar.visibility = View.VISIBLE
                call.enqueue(this)
            }
        }else{
            val call = RetrofitMananger.getWeatherService()
                .find(input_text_main.text.toString(),Constants.API_KEY)
            progressBar.visibility = View.VISIBLE
            call.enqueue(this)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = item.itemId
        if (menuId == R.id.setting_item){
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun isDeviceConnected():Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo =cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    override fun onFailure(call: Call<FindResult>, t: Throwable) {
        Log.e("WELL", "Error", t)
    }

    override fun onResponse(call: Call<FindResult>, response: Response<FindResult>) {
        if (response.isSuccessful){
            adapter.updateData(response.body()?.list)
            //response.body()?.list?.forEach {
              //  Log.d("WELL", it.toString())
            //}
            progressBar.visibility = View.INVISIBLE

        }
    }
}
//class reponsavel por rodar processo em background
class InsertFavoriteAsync(context: Context)
    :AsyncTask<City, Void,Boolean>(){
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
