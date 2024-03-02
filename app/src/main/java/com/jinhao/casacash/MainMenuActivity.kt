package com.jinhao.casacash

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainMenuActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvFamilyName: TextView
    private lateinit var tvRemainBudget: TextView

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

        val sharedPref = getSharedPreferences(getString(R.string.userId), Context.MODE_PRIVATE)
        val userId = sharedPref.getInt(getString(R.string.userId), 0)

        tvRemainBudget = findViewById(R.id.tv_main_menu_remain)
        tvRemainBudget.setText(getRemainBudget(userId).toString())

        tvFamilyName = findViewById(R.id.tv_main_menu_family)
        tvFamilyName.setText(getFamilyNameByUserId(userId))
    }

    fun getRemainBudget(userId: Int) : Double{
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = """
                SELECT
                    f.FAMILY_BUDGET - COALESCE(SUM(s.SPENDING_AMOUNT), 0) AS REMAINING_BUDGET
                FROM
                    Families f
                    LEFT JOIN Spendings s ON f.FAMILY_ID = s.FAMILY_ID
                    LEFT JOIN Default_Family df ON f.FAMILY_ID = df.FAMILY_ID
                WHERE
                    df.USER_ID = ?
                    AND strftime('%Y-%m', s.SPENDING_DATE) = strftime('%Y-%m', 'now')
                GROUP BY
                    f.FAMILY_ID, f.FAMILY_BUDGET;   
        """.trimIndent()
        val cursor: Cursor = bd.rawQuery(
            query,
            arrayOf(userId.toString())
        )

        var remainingBudget = 0.0

        if (cursor.moveToFirst()) {
            remainingBudget = cursor.getDouble(0)
        }

        cursor.close()
        bd.close()

        return remainingBudget
    }

    fun getFamilyNameByUserId(userId: Int): String{
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase
        val query = "SELECT f.FAMILY_NAME FROM Families f LEFT JOIN Default_Family df " +
                "ON f.FAMILY_ID = df.FAMILY_ID WHERE df.USER_ID = ?"

        val selectionArgs = arrayOf(userId.toString())
        val cursor = bd.rawQuery(query, selectionArgs)

        var familyName = ""

        if (cursor.moveToFirst()) {
            familyName = cursor.getString(0)
        }
        cursor.close()
        return familyName

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
    fun goToMainMenu(view: View){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
    fun goToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}