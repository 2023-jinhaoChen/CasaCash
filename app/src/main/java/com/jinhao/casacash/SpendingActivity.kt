package com.jinhao.casacash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SpendingActivity : AppCompatActivity() {
    private lateinit var etSpendingName: EditText
    private lateinit var etSpendingAmount: EditText
    private lateinit var etSpendingDescription: EditText
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
            query = "INSERT INTO Spendings(SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI) " +
                    "VALUES ('${etSpendingName.text}', ${etSpendingAmount.text}, " +
                    "'${etSpendingDescription.text}', CURRENT_DATE, null, 1)"
        }
        bd?.execSQL(query)
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