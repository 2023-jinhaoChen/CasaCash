import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jinhao.casacash.DataBaseAPP
import com.jinhao.casacash.R
import com.jinhao.casacash.data.Family

class FamilyAdapter(private val context: Context, private val familyList: ArrayList<Family>) : RecyclerView.Adapter<FamilyAdapter.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    var onDeleteClick: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClick = listener
    }

    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        this.onDeleteClick = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var familyId: Int = 0
        var familyName: TextView = itemView.findViewById(R.id.tv_item_family_name_value)
        var familyBudget: TextView = itemView.findViewById(R.id.tv_item_family_budget_value)
        var familyAdminId: TextView = itemView.findViewById(R.id.tv_item_family_admin_value)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(familyId)
            }

            itemView.findViewById<ImageButton>(R.id.ib_item_family_delete).setOnClickListener {
                onDeleteClick?.invoke(familyId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_family, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val family = familyList[position]
        val adminUsername = getUserNameById(family.adminId)
        holder.familyId = family.id
        holder.familyName.text = family.name
        holder.familyBudget.text = family.budget.toString()
        holder.familyAdminId.text = adminUsername

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(holder.familyId)
        }
    }

    private fun getUserNameById(adminId: Int): String {
        val admin = DataBaseAPP(context, "bd", null, 1)
        val bd = admin.writableDatabase

        val query = "SELECT USER_NAME FROM Users WHERE USER_ID = ?"
        val selectionArgs = arrayOf(adminId.toString())
        val cursor = bd.rawQuery(query, selectionArgs)

        var adminUsername = ""

        if (cursor.moveToFirst()) {
            adminUsername = cursor.getString(0)
        }

        cursor.close()
        return adminUsername
    }
    override fun getItemCount(): Int {
        return familyList.size
    }

    fun updateData(newList: List<Family>) {
        familyList.clear()
        familyList.addAll(newList)
        notifyDataSetChanged()
    }
}
