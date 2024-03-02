package com.jinhao.casacash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.jinhao.casacash.data.Spending
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SpendingActivity : AppCompatActivity() {
    private lateinit var etSpendingName: EditText
    private lateinit var etSpendingAmount: EditText
    private lateinit var etSpendingDescription: EditText

    private lateinit var ibSpendingImageDelete: ImageButton
    private val defaultImagePath: String = "/data/data/com.jinhao.casacash/files/recibo.jpeg"

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var ivSpendingImage: ImageView
    private lateinit var imagePath: String

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending)

        etSpendingName = findViewById(R.id.et_spending_name)
        etSpendingAmount = findViewById(R.id.et_spending_amount)
        etSpendingDescription = findViewById(R.id.et_spending_description)

        val defaultImageFile = File(defaultImagePath)

        if (!defaultImageFile.exists()) {
            // Copy the image from drawable resources to application data directory
            val defaultImageResourceId = R.drawable.recibo
            val defaultImageBitmap = BitmapFactory.decodeResource(resources, defaultImageResourceId)
            saveBitmapToFile(defaultImageBitmap, defaultImageFile)
        }

        ivSpendingImage = findViewById(R.id.iv_spending_image)
        ivSpendingImage.setOnClickListener {
            openImageChooser()
        }

        val spendingId = intent.getIntExtra("spendingId",-1)
        if(spendingId != -1){
            val spending = getSpendingDetails(spendingId)

            etSpendingName.setText(spending.title)
            etSpendingAmount.setText(spending.amount.toString())
            etSpendingDescription.setText(spending.description)
            imagePath = spending.image_uri
            if (imagePath == "") {
                imagePath = defaultImagePath
            }
            var imageUri = Uri.parse(imagePath)
            ivSpendingImage.setImageURI(imageUri)
        }else{
            imagePath = defaultImagePath
        }

        val btSave: Button = findViewById(R.id.bt_spending_save)
        btSave.setOnClickListener {
            saveSpending(spendingId)
            finish()
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.navigation_view)

        val ibDeleteImage: ImageButton = findViewById(R.id.ib_spending_remove_image)
        ibDeleteImage.setOnClickListener {
            setDefaultImage()
        }

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

        val query = "SELECT SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID, FAMILY_ID " +
                        "FROM Spendings WHERE SPENDING_ID = $spendingId"

        val reg = bd.rawQuery(query, null)

        var title: String = ""
        var amount: Double = 0.0
        var date: Date = Date()
        var description: String = ""
        var uri: String = ""
        var userId: Int = 0
        var familyId: Int = 0

        if (reg.moveToFirst()) {
            title = reg.getString(0)
            amount = reg.getString(1).toDouble()
            description = reg.getString(2) ?: ""
            val dateString = reg.getString(3)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date = dateFormat.parse(dateString) ?: Date()
            uri = reg.getString(4)?: ""
            userId = reg.getInt(5)
            familyId = reg.getInt(6)
        }

        reg.close()
        return Spending(spendingId, title, amount, description, date, uri, userId, familyId)
    }

    fun saveSpending(spendingId: Int) {
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase
        val query: String
        if (spendingId != -1) {
            query = "UPDATE Spendings SET SPENDING_TITLE = '${etSpendingName.text}', " +
                    "SPENDING_AMOUNT = ${etSpendingAmount.text}, " +
                    "SPENDING_DESCRIPTION = '${etSpendingDescription.text}', " +
                    "SPENDING_IMAGE_URI = '${imagePath}' " +
                    "WHERE SPENDING_ID = $spendingId"
        } else {
            val sharedPref = getSharedPreferences(getString(R.string.userId), Context.MODE_PRIVATE)
            val userId = sharedPref.getInt(getString(R.string.userId), 0)
            query = "INSERT INTO Spendings(SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID, FAMILY_ID) " +
                        "SELECT '${etSpendingName.text}', ${etSpendingAmount.text}, " +
                        "'${etSpendingDescription.text}', CURRENT_DATE, '${imagePath}', $userId, FAMILY_ID " +
                        "FROM Default_Family WHERE USER_ID = $userId"
        }
        bd?.execSQL(query)
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!

            val copiedImagePath = copyImageToAppData(selectedImageUri)

            // Update the imagePath variable to point to the copied image
            if (copiedImagePath != null) {
                imagePath = copiedImagePath
                ivSpendingImage.setImageURI(Uri.parse(copiedImagePath))
            } else {
                // Handle error if image copy failed
                Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setDefaultImage() {
        val file = File(imagePath)
        if (file.exists()) {
            file.delete()
        }

        imagePath = defaultImagePath
        ivSpendingImage.setImageURI(Uri.parse(defaultImagePath))
    }

    private fun copyImageToAppData(imageUri: Uri): String? {
        val inputStream = contentResolver.openInputStream(imageUri)
        val outputFile = File(filesDir, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(outputFile)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return if (outputFile.exists()) outputFile.absolutePath else null
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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