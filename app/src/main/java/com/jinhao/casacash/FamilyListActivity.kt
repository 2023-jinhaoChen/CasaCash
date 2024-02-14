package com.jinhao.casacash

import FamilyAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.jinhao.casacash.data.Family

class FamilyListActivity: AppCompatActivity()  {
    private lateinit var drawerLayout: DrawerLayout
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
            deleteFamily(familyId)
            familyAdapter.updateData(getFamilyList())
        }

        val btCreateFamily : Button = findViewById(R.id.bt_family_list_create)
        btCreateFamily.setOnClickListener{
            val intent = Intent(this, FamilyActivity::class.java)
            startActivity(intent)
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

    private fun deleteFamily(familyId : Int){
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val deleteQuery = "DELETE FROM Families WHERE FAMILY_ID = $familyId"
        bd.execSQL(deleteQuery)
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