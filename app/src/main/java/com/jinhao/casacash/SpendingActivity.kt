package com.jinhao.casacash

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
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SpendingActivity : AppCompatActivity() {
    private lateinit var etSpendingName: EditText
    private lateinit var etSpendingAmount: EditText
    private lateinit var etSpendingDescription: EditText

    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending)

        etSpendingName = findViewById(R.id.et_spending_name)
        etSpendingAmount = findViewById(R.id.et_spending_amount)
        etSpendingDescription = findViewById(R.id.et_spending_description)

        val spendingId = intent.getIntExtra("spendingId",-1)
        if(spendingId != -1){
            val spending = getSpendingDetails(spendingId)

            etSpendingName.setText(spending.title)
            etSpendingAmount.setText(spending.amount.toString())
            etSpendingDescription.setText(spending.description)
        }

        val btSave: Button = findViewById(R.id.bt_spending_save)
        btSave.setOnClickListener {
            saveSpending(spendingId)
            finish()
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


    private fun getSpendingDetails(spendingId: Int): Spending {
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE " +
                "FROM Spendings WHERE SPENDING_ID = $spendingId"

        val reg = bd.rawQuery(query, null)

        var title: String = ""
        var amount: Double = 0.0
        var date: Date = Date()
        var description: String = ""

        if (reg.moveToFirst()) {
            title = reg.getString(0)
            amount = reg.getString(1).toDouble()
            description = reg.getString(2) ?: ""
            val dateString = reg.getString(3)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date = dateFormat.parse(dateString) ?: Date()
        }

        reg.close()
        return Spending(spendingId, title, amount, description, date, "", 1)
    }

    fun saveSpending(spendingId: Int){
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase
        val query : String
        if (spendingId != -1) {
            query = "UPDATE Spendings SET SPENDING_TITLE = '${etSpendingName.text}', " +
                    "SPENDING_AMOUNT = ${etSpendingAmount.text}, " +
                    "SPENDING_DESCRIPTION = '${etSpendingDescription.text}' " +
                    "WHERE SPENDING_ID = $spendingId"
        } else {
            query = "INSERT INTO Spendings(SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID) " +
                    "VALUES ('${etSpendingName.text}', ${etSpendingAmount.text}, " +
                    "'${etSpendingDescription.text}', CURRENT_DATE, null, 1)"
        }
        bd?.execSQL(query)
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