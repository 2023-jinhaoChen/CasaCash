package com.jinhao.casacash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUserName : EditText = findViewById(R.id.et_main_user)
        val etPassword : EditText = findViewById(R.id.et_main_password)

        val btLogin : Button = findViewById(R.id.bt_main_login)
        btLogin.setOnClickListener{
            var userName : String = etUserName.text.toString()
            var userPassword : String = etPassword.text.toString()

            if (userName.length == 0){
                Toast.makeText(this, "Usuario no puede ser vacío", Toast.LENGTH_LONG)
                    .show()
            }else if(userPassword.length == 0){
                Toast.makeText(this, "Constraseña no puede ser vacío", Toast.LENGTH_LONG)
                    .show()
            }else {
                val admin = DataBaseAPP(this, "bd", null, 1)
                val bd = admin.writableDatabase
                val loginQuery = "SELECT USER_ID " +
                        "FROM Users WHERE USER_NAME = '$userName' AND USER_PASSWORD = '$userPassword'"
                val reg = bd.rawQuery(loginQuery, null)

                var userId: Int = 0

                if (reg.moveToFirst()) {
                    userId = reg.getString(0).toInt()
                    Toast.makeText(this, "Bienvenido $userName", Toast.LENGTH_LONG).show()

                    val sharedPref =
                        this?.getSharedPreferences(getString(R.string.userId), Context.MODE_PRIVATE)
                            ?: return@setOnClickListener
                    with(sharedPref.edit()) {
                        putInt(getString(R.string.userId), userId)
                        commit()
                    }
                    val retrievedUserId = sharedPref.getInt(getString(R.string.userId), 0)
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrecto", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        val tvRegister : TextView = findViewById(R.id.tv_main_register)
        tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}