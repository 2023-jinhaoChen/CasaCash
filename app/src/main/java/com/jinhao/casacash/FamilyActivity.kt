package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FamilyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family)
        val btAddMember : Button = findViewById(R.id.bt_family_add_member)
        btAddMember.setOnClickListener{
            val intent = Intent(this, AddMemberActivity::class.java)
            startActivity(intent)
        }
        val btDeleteMember : Button = findViewById(R.id.bt_family_delete_member)
        btDeleteMember.setOnClickListener{
            val intent = Intent(this, DeleteMemberActivity::class.java)
            startActivity(intent)
        }
        val btDefaultFamily : Button = findViewById(R.id.bt_family_select_default_family)
        btDefaultFamily.setOnClickListener{

        }
        val btSave : Button = findViewById(R.id.bt_family_save)
        btSave.setOnClickListener{
            finish()
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