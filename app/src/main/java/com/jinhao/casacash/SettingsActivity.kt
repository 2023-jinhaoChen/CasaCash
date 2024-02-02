package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val btChangePassword : Button = findViewById(R.id.bt_settings_change_password)
        btChangePassword.setOnClickListener{
            val intent = Intent(this, NewPasswordActivity::class.java)
            startActivity(intent)
        }
        val btManageFamily : Button = findViewById(R.id.bt_settings_manage_family)
        btManageFamily.setOnClickListener{
            val intent = Intent(this, FamilyListActivity::class.java)
            startActivity(intent)
        }
        val btFamilyRequest : Button = findViewById(R.id.bt_settings_family_request)
        btFamilyRequest.setOnClickListener{
            val intent = Intent(this, RequestsActivity::class.java)
            startActivity(intent)
        }
        val btLogOut : Button = findViewById(R.id.bt_settings_logout)
        btLogOut.setOnClickListener{
            goToMain()
        }
    }
    fun goToMainMenu(){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToMainMenu(view: View){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(view : View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}