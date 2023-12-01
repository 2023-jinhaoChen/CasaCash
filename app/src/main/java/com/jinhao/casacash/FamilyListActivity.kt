package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FamilyListActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_list)
        val btCreateFamily : Button = findViewById(R.id.bt_family_list_create)
        btCreateFamily.setOnClickListener{
            val intent = Intent(this, FamilyActivity::class.java)
            startActivity(intent)
        }
    }

    fun goToMainMenu(view : View){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(view : View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}