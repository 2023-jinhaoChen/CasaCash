package com.jinhao.casacash

import FamilyAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinhao.casacash.data.Family

class FamilyListActivity: AppCompatActivity()  {
    lateinit var familyAdapter: FamilyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_list)

        val familyListView = findViewById<RecyclerView>(R.id.rv_family_list)
        familyListView.layoutManager = LinearLayoutManager(this)
        familyAdapter = FamilyAdapter(this, ArrayList())
        familyListView.adapter = familyAdapter

        familyAdapter.updateData(getFamilyList())

        familyAdapter.setOnItemClickListener { familyId ->
            val intent = Intent(this@FamilyListActivity, FamilyActivity::class.java)
            intent.putExtra("familyId", familyId)
            startActivity(intent)
        }

        familyAdapter.setOnDeleteClickListener { familyId ->
            // Handle delete click (delete family from the list)
            // Add your logic to delete the family with the specified ID
        }

        val btCreateFamily : Button = findViewById(R.id.bt_family_list_create)
        btCreateFamily.setOnClickListener{
            val intent = Intent(this, FamilyActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        familyAdapter.updateData(getFamilyList())
    }

    fun getFamilyList(): ArrayList<Family>{
        val familyList : ArrayList<Family> = ArrayList()
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val reg = bd.rawQuery("SELECT FAMILY_ID, FAMILY_NAME, FAMILY_BUDGET, FAMILY_ADMIN_ID FROM Families", null)
        var id : Int
        var name : String
        var budget : Double
        var adminId : Int

        if (reg.moveToFirst()){
            do{
                id = reg.getString(0).toInt()
                name = reg.getString(1)
                budget = reg.getString(2).toDouble()
                adminId = reg.getString(3).toInt()
                familyList.add(Family(id, name, budget, adminId))
            } while (reg.moveToNext())
        }
        reg.close()
        return familyList
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