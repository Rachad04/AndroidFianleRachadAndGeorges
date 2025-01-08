package com.usj.budgettracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.usj.budgettracking.data.Expense

class SpendingAdapter : RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder>() {

    private var expenses: List<Expense> = emptyList()

    /**
     * Updates the list of expenses and refreshes the RecyclerView.
     */
    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spending_item, parent, false)
        return SpendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        val expense = expenses[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int = expenses.size

    class SpendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.itemEventName)
        private val amountTextView: TextView = itemView.findViewById(R.id.itemPrice)
        private val dateTextView: TextView = itemView.findViewById(R.id.itemDate)

        /**
         * Binds the expense object data to the corresponding views.
         */
        fun bind(expense: Expense) {
            descriptionTextView.text = expense.name
            amountTextView.text = "$%.2f".format(expense.amount) // Format amount to 2 decimal places
            dateTextView.text = expense.date
        }
    }
}
