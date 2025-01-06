package com.usj.midtermrachadsouaiby.ui.spendinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.usj.midtermrachadsouaiby.R
import com.usj.midtermrachadsouaiby.data.Spending

class SpendingAdapter : RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder>() {

    private var spendings = listOf<Spending>()

    // Set spendings to update the RecyclerView
    fun setSpendings(spendings: List<Spending>) {
        this.spendings = spendings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spending_item, parent, false)
        return SpendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        val spending = spendings[position]
        holder.bind(spending)
    }

    override fun getItemCount() = spendings.size

    class SpendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.itemEventName)
        private val amountTextView: TextView = itemView.findViewById(R.id.itemPrice)
        private val dateTextView: TextView = itemView.findViewById(R.id.itemDate)

        fun bind(spending: Spending) {
            // Set the data from the spending object to the corresponding TextViews
            descriptionTextView.text = spending.description
            amountTextView.text = "$${spending.amount}"
            dateTextView.text = spending.date
        }
    }
}
