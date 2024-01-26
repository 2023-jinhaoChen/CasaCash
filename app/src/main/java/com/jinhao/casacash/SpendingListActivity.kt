package com.jinhao.casacash

import SpendingAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.*


class SpendingListActivity : AppCompatActivity() {

    lateinit var spendingAdapter: SpendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_list)

        val spendingListView = findViewById<RecyclerView>(R.id.rv_spending_list)
        spendingListView.layoutManager = LinearLayoutManager(this)

        spendingAdapter = SpendingAdapter(this, ArrayList())
        spendingListView.adapter = spendingAdapter

        spendingAdapter.updateData(getSpendingList())

        spendingAdapter.setOnItemClickListener { spendingId ->
            val intent = Intent(this@SpendingListActivity, SpendingActivity::class.java)
            intent.putExtra("spendingId", spendingId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        spendingAdapter.updateData(getSpendingList())
    }

    fun getSpendingList(): ArrayList<Spending> {
        val spendingList: ArrayList<Spending> = ArrayList()
        val admin = DataBaseAPP(this, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT s.SPENDING_ID, s.SPENDING_TITLE, s.SPENDING_AMOUNT, " +
                "s.SPENDING_DESCRIPTION, s.SPENDING_DATE, s.SPENDING_IMAGE_URI, us.USER_ID " +
                "FROM Spendings s " +
                "LEFT JOIN UserSpending us ON s.SPENDING_ID = us.SPENDING_ID"

        val reg = bd.rawQuery(query, null)

        var id: Int
        var title: String
        var amount: Double
        var description: String
        var date: Date
        var imageUri: String
        var userId: Int


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

                spendingList.add(Spending(id, title, amount, description, date, imageUri, userId))
            } while (reg.moveToNext())
        }

        reg.close()
        return spendingList
    }

    private fun parseData(dateString: String): Date{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return inputFormat.parse(dateString) ?: Date()
    }

    fun goToMainMenu(view: View) {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    fun goToSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
