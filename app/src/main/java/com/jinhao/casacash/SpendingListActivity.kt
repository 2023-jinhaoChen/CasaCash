package com.jinhao.casacash

import SpendingAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.*


class SpendingListActivity : AppCompatActivity() {

    lateinit var spendingAdapter: SpendingAdapter
    private lateinit var drawerLayout: DrawerLayout
    lateinit var dateParser: DateParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_list)

        val spendingListView = findViewById<RecyclerView>(R.id.rv_spending_list)
        spendingListView.layoutManager = LinearLayoutManager(this)

        dateParser = DateParser()

        spendingAdapter = SpendingAdapter(this, ArrayList())
        spendingListView.adapter = spendingAdapter

        val sharedPref = getSharedPreferences(getString(R.string.userId), Context.MODE_PRIVATE)
        val userId = sharedPref.getInt(getString(R.string.userId), 0)
        spendingAdapter.updateData(getSpendingListForUser(userId))

        spendingAdapter.setOnItemClickListener { spendingId ->
            val intent = Intent(this@SpendingListActivity, SpendingActivity::class.java)
            intent.putExtra("spendingId", spendingId)
            startActivity(intent)
        }

        spendingAdapter.setOnDeleteClickListener { spendingId ->
            deleteSpending(spendingId)
            spendingAdapter.updateData(getSpendingListForUser(userId))
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
        spendingAdapter.updateData(getSpendingListForUser(userId))
    }

    fun getSpendingList(): ArrayList<Spending> {
        val spendingList: ArrayList<Spending> = ArrayList()
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT SPENDING_ID, SPENDING_TITLE, SPENDING_AMOUNT, " +
                "SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID, FAMILY_ID " +
                "FROM Spendings"

        val reg = bd.rawQuery(query, null)

        var id: Int
        var title: String
        var amount: Double
        var description: String
        var date: Date
        var imageUri: String
        var userId: Int
        var familyId: Int

        if (reg.moveToFirst()) {
            do {
                id = reg.getString(0).toInt()
                title = reg.getString(1)
                amount = reg.getString(2).toDouble()
                description = reg.getString(3)
                val dateString = reg.getString(4)
                date = parseData(dateString)
                imageUri = reg.getString(5)?: ""
                userId = reg.getString(6)?.toInt() ?: -1 // Handle nullable userId
                familyId = reg.getString(7).toInt() ?: -1

                spendingList.add(Spending(id, title, amount, description, date, imageUri, userId, familyId))
            } while (reg.moveToNext())
        }

        reg.close()
        return spendingList
    }

    fun getSpendingListForUser(userId: Int): ArrayList<Spending> {
        val spendingList: ArrayList<Spending> = ArrayList()
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        // Get the default familyId for the given userId
        val familyIdQuery = "SELECT FAMILY_ID FROM Default_Family WHERE USER_ID = $userId"
        val familyIdCursor = bd.rawQuery(familyIdQuery, null)
        var defaultFamilyId: Int? = null

        if (familyIdCursor.moveToFirst()) {
            defaultFamilyId = familyIdCursor.getInt(0)
        }

        familyIdCursor.close()

        // If a default familyId is found, retrieve spendings for that familyId in the current month
        defaultFamilyId?.let {
            val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

            val query = """
            SELECT SPENDING_ID, SPENDING_TITLE, SPENDING_AMOUNT, 
                SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID, FAMILY_ID 
            FROM Spendings 
            WHERE FAMILY_ID = $defaultFamilyId AND strftime('%Y-%m', SPENDING_DATE) = '$currentMonth'
        """.trimIndent()

            val reg = bd.rawQuery(query, null)

            var id: Int
            var title: String
            var amount: Double
            var description: String
            var date: Date
            var imageUri: String
            var spendingUserId: Int
            var spendingFamilyId: Int

            if (reg.moveToFirst()) {
                do {
                    id = reg.getString(0).toInt()
                    title = reg.getString(1)
                    amount = reg.getString(2).toDouble()
                    description = reg.getString(3)
                    val dateString = reg.getString(4)
                    date = parseData(dateString)
                    imageUri = reg.getString(5) ?: ""
                    spendingUserId = reg.getString(6)?.toInt() ?: -1 // Handle nullable userId
                    spendingFamilyId = reg.getString(7).toInt() ?: -1

                    spendingList.add(Spending(id, title, amount, description, date, imageUri, spendingUserId, spendingFamilyId))
                } while (reg.moveToNext())
            }

            reg.close()
        }

        return spendingList
    }

    private fun deleteSpending(spendingId : Int){
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val deleteQuery = "DELETE FROM Spendings WHERE SPENDING_ID = $spendingId"
        bd.execSQL(deleteQuery)
    }

    fun parseData(dateString: String): Date{
        return dateParser.parseDate(dateString)
    }

    fun goToMainMenu(view: View){
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun goToMainMenu(){
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
