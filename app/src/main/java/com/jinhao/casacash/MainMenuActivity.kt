package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val btFindSpendings : Button = findViewById(R.id.bt_main_menu_find_spendings)
        btFindSpendings.setOnClickListener{
            val intent = Intent(this, CheckSpendingsActivity::class.java)
            startActivity(intent)
        }
        val btReport : Button = findViewById(R.id.bt_main_menu_report)
        btReport.setOnClickListener{
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        val btAddSpendings : Button = findViewById(R.id.bt_main_menu_add_spendings)
        btAddSpendings.setOnClickListener{
            val intent = Intent(this, SpendingActivity::class.java)
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