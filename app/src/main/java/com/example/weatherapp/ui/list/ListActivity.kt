package com.example.weatherapp.ui.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.api.RetrofitMananger
import com.example.weatherapp.common.Constants
import com.example.weatherapp.data.RoomManager
import com.example.weatherapp.entity.FindResult
import com.example.weatherapp.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListActivity : AppCompatActivity(), Callback<FindResult> {

    private val adapter: ListAdapter by lazy {
        ListAdapter()
    }
    private val sp : SharedPreferences by lazy {
        getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
    }

    private val db : RoomManager? by lazy{
       RoomManager.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        if (isDeviceConnected()) getAllFavorete()

        btn_buscar.setOnClickListener { if (isDeviceConnected()) getCities()}

    }

    private fun initRecyclerView(){
        rvWeather.adapter = adapter
    }
    //Retorna a cidades da pesquisa na API
    private fun getCities(){
        if (input_text_main.text.isNullOrBlank()){
            getAllFavorete()
        }else{
            val call = RetrofitMananger.getWeatherService()
                .find(input_text_main.text.toString(),Constants.API_KEY,getLang(), getUnit())
            progressBar.visibility = View.VISIBLE
            call.enqueue(this)
        }

    }
    private fun getAllFavorete(){
        var list = db?.getCityDao()?.allFavorite()
        var aux:String = ""
        if (!list.isNullOrEmpty()){
            for (i in 0..list.size-1){
                Log.d("Patriota2", "${list[i].id}")
                if (!aux.isNullOrBlank()){
                    aux = "$aux,${list[i].id}"
                }else{
                    aux = "${list[i].id}"
                }

            }
            Log.d("Patriota2", "Aux: $aux")
            val call = RetrofitMananger.getWeatherService()
                .findByFavorite(aux,Constants.API_KEY,getLang(), getUnit())
            progressBar.visibility = View.VISIBLE
            call.enqueue(this)
        }
    }

    private fun getUnit():String{
        return if (sp.getBoolean(Constants.PREFS_TEMP, true)) "metric" else "imperial"
    }
    private fun getLang():String{
        return if (sp.getBoolean(Constants.PREFS_LANG, true)) "en" else "pt"
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

