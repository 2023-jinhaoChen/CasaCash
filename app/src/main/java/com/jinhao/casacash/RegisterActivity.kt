package com.jinhao.casacash

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btRegister : Button = findViewById(R.id.bt_register)
        val etUser : EditText = findViewById(R.id.et_register_user)
        val etPass1 : EditText = findViewById(R.id.et_register_password)
        val etPass2 : EditText = findViewById(R.id.et_register_confirm)
        val etEmail : EditText = findViewById(R.id.et_register_email)
        btRegister.setOnClickListener{
            if (etUser.text.length == 0 || etPass1.text.length == 0 ||
                etPass2.text.length == 0 || etEmail.text.length == 0){
                Toast.makeText(this, "Falta campo para rellenar",Toast.LENGTH_LONG).show()
            } else if (etPass2.text.toString() == etPass1.text.toString()){
                val admin = DataBaseAPP(this, "bd", null, 1)
                val bd = admin.writableDatabase
                val reg = ContentValues()
                reg.put("USER_NAME", etUser.text.toString())
                reg.put("USER_PASSWORD", etPass1.text.toString())
                reg.put("USER_EMAIL", etEmail.text.toString())
                bd.insert("Users",null,reg)
                bd.close()
                Toast.makeText(this, "Registrado correctamente, volviendo al menú principal",Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "No coinciden las 2 contraseñas",Toast.LENGTH_LONG).show()
            }
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