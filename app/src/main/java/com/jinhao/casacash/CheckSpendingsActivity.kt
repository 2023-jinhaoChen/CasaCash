package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CheckSpendingsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_spendings)
        val btFindSpendings : Button = findViewById(R.id.bt_check_spendings_filter)
        btFindSpendings.setOnClickListener{
            val intent = Intent(this, SpendingListActivity::class.java)
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