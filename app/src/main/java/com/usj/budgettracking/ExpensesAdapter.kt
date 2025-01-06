package com.usj.budgettracking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usj.budgettracking.data.Expense
import com.usj.budgettracking.databinding.ItemExpenseBinding

class ExpensesAdapter(
    private val onItemEdit: (Expense) -> Unit,
    private val onItemDelete: (Expense) -> Unit
) : RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

    private val expensesList = mutableListOf<Expense>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding, onItemEdit, onItemDelete)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expensesList[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int = expensesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Expense>) {
        expensesList.clear()
        expensesList.addAll(newList)
        notifyDataSetChanged()
    }

    class ExpenseViewHolder(
        private val binding: ItemExpenseBinding,
        private val onItemEdit: (Expense) -> Unit,
        private val onItemDelete: (Expense) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.tvExpenseName.text = expense.name
            binding.tvExpenseAmount.text = "Amount: $${String.format("%.2f", expense.amount)}"
            binding.tvExpenseDate.text = "Date: ${expense.date}"
            binding.tvExpenseCategory.text = "Category: ${expense.category}"

            binding.btnEdit.setOnClickListener { onItemEdit(expense) }
            binding.btnDelete.setOnClickListener { onItemDelete(expense) }
        }
    }
}
