package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
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
    fun goToMainMenu(){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_change_password -> goToSettings()
            R.id.item_manage_family -> goToMainMenu()
            R.id.item_family_request -> Toast.makeText(this, "family_requests", Toast.LENGTH_SHORT).show()
            R.id.item_logout -> Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}