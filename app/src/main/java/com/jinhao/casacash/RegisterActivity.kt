package com.jinhao.casacash

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class RegisterActivity: AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var passwordValidator: PasswordValidator

    lateinit var etUser: EditText
    lateinit var etPass1: EditText
    lateinit var etPass2: EditText
    lateinit var etEmail: EditText
    val btRegister: Button by lazy { findViewById<Button>(R.id.bt_register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUser = findViewById(R.id.et_register_user)
        etPass1 = findViewById(R.id.et_register_password)
        etPass2 = findViewById(R.id.et_register_confirm)
        etEmail = findViewById(R.id.et_register_email)

        passwordValidator = PasswordValidator()

        btRegister.setOnClickListener{
            registerNewUser()
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

    private fun registerNewUser() {
        if (etUser.text.length == 0 || etPass1.text.length == 0 ||
            etPass2.text.length == 0 || etEmail.text.length == 0
        ) {
            Toast.makeText(this, "Falta campo para rellenar", Toast.LENGTH_LONG).show()
        } else if (etPass2.text.toString() != etPass1.text.toString()) {
            Toast.makeText(this, "No coinciden las 2 contraseñas", Toast.LENGTH_LONG).show()
        } else if (isValidPassword(etPass1.text.toString())) {
            val admin = DataBaseAPP(this, "bd", null, 1)
            val bd = admin.writableDatabase
            val userId = insertNewUser(bd)
            if (insertNewUser(bd) != -1) {
                val sharedPref = this.getSharedPreferences(getString(R.string.userId), MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt(getString(R.string.userId), userId)
                    commit()
                }

                val familyId = createDefaultFamily(bd, userId)

                if (familyId != -1){
                    registerDefaultFamilyToUser(bd, userId, familyId)
                }else{
                    Toast.makeText(
                        this,
                        "Error al crear default family",
                        Toast.LENGTH_LONG
                    ).show()
                }

                Toast.makeText(
                    this,
                    "Registrado correctamente, volviendo al menú principal",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(
                    this,
                    "Error al crear nuevo user",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(this, "Formato de contraseña incorrecta", Toast.LENGTH_LONG).show()
        }
    }

    fun insertNewUser(bd : SQLiteDatabase) : Int{
        val reg = ContentValues()

        val username: String = etUser.text.toString()
        val password: String = etPass1.text.toString()
        val email: String = etEmail.text.toString()

        reg.put("USER_NAME", username)
        reg.put("USER_PASSWORD", password)
        reg.put("USER_EMAIL", email)

        val userId = bd.insert("Users", null, reg)
        return userId.toInt()
    }

    fun createDefaultFamily(bd : SQLiteDatabase, userId : Int) : Int{
        val reg = ContentValues()
        val username: String = etUser.text.toString()
        reg.put("FAMILY_NAME", "Familia de $username")
        reg.put("FAMILY_BUDGET", 0)
        reg.put("FAMILY_ADMIN_ID", userId)
        val familyId = bd.insert("Families", null, reg)
        return familyId.toInt()
    }

    fun registerDefaultFamilyToUser(bd : SQLiteDatabase, userId : Int, familyId: Int){
        val query = "INSERT INTO Default_Family(USER_ID, FAMILY_ID) VALUES ($userId, $familyId)"
        bd.execSQL(query)
    }


    fun isValidPassword(password: String): Boolean{
        return passwordValidator.isValidPassword(password)
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