package com.jinhao.casacash

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.jinhao.casacash.data.Family
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FamilyActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
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
            val sharedPref = getSharedPreferences(getString(R.string.userId), Context.MODE_PRIVATE)
            val userId = sharedPref.getInt(getString(R.string.userId), 0)
            setDefaultFamilyForUser(userId, familyId)
            Toast.makeText(this, "Se ha cambiado la família correctamente", Toast.LENGTH_SHORT).show()
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
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.navigation_view)

// Configurar el icono del hamburguesa para abrir el menú
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.textAppName, R.string.textAppName
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

// Manejar clics en los elementos del menú de navegación
        navView.setNavigationItemSelectedListener { menuItem ->
            // Manejar las acciones del menú aquí
            when (menuItem.itemId) {
                R.id.item_change_password -> {
                    goToChangePassword()
                    true
                }
                R.id.item_manage_family -> {
                    goToManageFamily()
                    true
                }
                R.id.item_family_request -> {
                    goToFamilyRequest()
                    true
                }
                R.id.item_logout -> {
                    goToLogin()
                    true
                }

                else -> false
            }
        }

        val btnOpenMenu: ImageButton = findViewById(R.id.tb_menu)
        btnOpenMenu.setOnClickListener { openMenu(it) }
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
            query = "INSERT INTO Families(FAMILY_NAME, FAMILY_BUDGET, FAMILY_ADMIN_ID) " +
                    "VALUES ('${etFamily.text}', ${etBudget.text}, 1) "
        }
        bd?.execSQL(query)
    }

    fun setDefaultFamilyForUser(userId: Int, familyId: Int){
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase
        val query = "UPDATE Default_Family SET FAMILY_ID = $familyId WHERE USER_ID = $userId"
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
    fun goToMainMenu(){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToMainMenu(view: View){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
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

    fun goToChangePassword(){
        val intent = Intent(this, NewPasswordActivity::class.java)
        startActivity(intent)
    }

    fun goToManageFamily(){
        val intent = Intent(this, FamilyListActivity::class.java)
        startActivity(intent)
    }

    fun goToFamilyRequest(){
        val intent = Intent(this, RequestsActivity::class.java)
        startActivity(intent)
    }

    fun goToLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openMenu(view: View) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}