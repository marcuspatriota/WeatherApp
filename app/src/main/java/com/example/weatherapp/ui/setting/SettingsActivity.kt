package com.example.weatherapp.ui.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.example.weatherapp.R
import com.example.weatherapp.common.Constants
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val sp :SharedPreferences by lazy {
        getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        updateUi()

        button_salvar.setOnClickListener {
                save()
        }

    }

    private fun save() {
        //Aqui por padrão kotlin ele já chama o metodo apply().
        sp.edit {
            putBoolean(Constants.PREFS_TEMP, rbC.isChecked)
            putBoolean(Constants.PREFS_LANG, rbEn.isChecked)
        }
        Toast.makeText(this, "Settings saved with sucess", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun updateUi(){
        val isCelsius = sp.getBoolean(Constants.PREFS_TEMP, true)
        val isEnglish = sp.getBoolean(Constants.PREFS_LANG, true)

        radiogroup_temp.check(if (isCelsius) R.id.rbC else R.id.rbf)
        radiogroup_lang.check(if (isEnglish) R.id.rbEn else R.id.rbPT)

        //rbC.isChecked = temp
        //rbf.isChecked = !temp
       // rbEn.isChecked = lag
        //rbEn.isChecked = !lag

    }

}
