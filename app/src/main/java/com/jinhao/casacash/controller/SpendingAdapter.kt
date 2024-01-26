import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhao.casacash.DataBaseAPP
import com.jinhao.casacash.R
import com.jinhao.casacash.data.Spending
import java.text.SimpleDateFormat
import java.util.*

class SpendingAdapter(private val context: Context, private val spendingList: ArrayList<Spending>) : RecyclerView.Adapter<SpendingAdapter.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    var onDeleteClick: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClick = listener
    }

    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        this.onDeleteClick = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var spendingId: Int = 0
        var spendingTitle: TextView = itemView.findViewById(R.id.tv_item_spending_title_value)
        var spendingAmount: TextView = itemView.findViewById(R.id.tv_item_spending_amount_value)
        var spendingDate: TextView = itemView.findViewById(R.id.tv_item_spending_date_value)
        var spendingUser: TextView = itemView.findViewById(R.id.tv_item_spending_user_value)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(spendingId)
            }

            itemView.findViewById<ImageButton>(R.id.ib_item_spending_delete).setOnClickListener {
                onDeleteClick?.invoke(spendingId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_spending, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spending = spendingList[position]
        val username = getUserNameById(spending.userId)
        holder.spendingId = spending.id
        holder.spendingTitle.text = spending.title
        holder.spendingAmount.text = spending.amount.toString()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(spending.date)

        holder.spendingDate.text = formattedDate
        holder.spendingUser.text = username

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(holder.spendingId)
        }
    }

    private fun getUserNameById(userId: Int): String {
        val admin = DataBaseAPP(context, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT USER_NAME FROM Users WHERE USER_ID = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor = bd.rawQuery(query, selectionArgs)

        var username = ""

        if (cursor.moveToFirst()) {
            username = cursor.getString(0)
        }

        cursor.close()
        return username
    }

    override fun getItemCount(): Int {
        return spendingList.size
    }

    fun updateData(newList: List<Spending>) {
        spendingList.clear()
        spendingList.addAll(newList)
        notifyDataSetChanged()
    }
}
