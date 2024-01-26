package com.jinhao.casacash

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jinhao.casacash.data.Family
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FamilyActivity : AppCompatActivity() {
    private lateinit var etFamily: EditText
    private lateinit var etBudget: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family)
        etFamily = findViewById(R.id.et_family_name)
        etBudget = findViewById(R.id.et_family_budget)
        val btAddMember : Button = findViewById(R.id.bt_family_add_member)

        val familyId = intent.getIntExtra("familyId",-1)
        if(familyId != -1){
            val family = getFamilyDetails(familyId)

            etFamily.setText(family.name)
            etBudget.setText(family.budget.toString())
        }

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
            if (etFamily.text.length == 0 || etBudget.text.length == 0){
                Toast.makeText(this, "Falta campo para rellenar", Toast.LENGTH_LONG).show()
            }else{
                saveFamily(familyId)
                finish()

            }
        }
    }

    fun saveFamily(familyId: Int){
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase
        val query : String
        if (familyId != -1){
            query = "UPDATE Families SET FAMILY_NAME = '${etFamily.text}', " +
                    "FAMILY_BUDGET = ${etBudget.text} " +
                    "WHERE FAMILY_ID = $familyId"
        }else{
            query = "INSERT INTO Families(FAMILY_NAME, FAMILY_BUDGET, FAMILY_ADMIN_ID) "
                    "VALUES ('${etFamily.text}', ${etBudget.text}, 1) "
        }
        bd?.execSQL(query)
    }

    private fun getFamilyDetails(familyId: Int): Family {
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT FAMILY_NAME, FAMILY_BUDGET, FAMILY_ADMIN_ID " +
                "FROM Families WHERE FAMILY_ID = $familyId"


        val reg = bd.rawQuery(query, null)

        var name: String = ""
        var budget: Double = 0.0
        var adminId: Int = 0

        if (reg.moveToFirst()) {
            name = reg.getString(0)
            budget = reg.getString(1).toDouble()
            adminId = reg.getString(2).toInt()
        }

        reg.close()
        return Family(familyId, name, budget, adminId)
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