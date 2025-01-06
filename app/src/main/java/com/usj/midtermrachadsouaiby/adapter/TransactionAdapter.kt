package com.usj.midtermrachadsouaiby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.usj.midtermrachadsouaiby.data.Spending
import android.widget.TextView

class TransactionAdapter(private var transactions: List<Spending>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.transactionDescription)
        val amount: TextView = itemView.findViewById(R.id.transactionAmount)
        val date: TextView = itemView.findViewById(R.id.transactionDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.description.text = transaction.description
        holder.amount.text = "$${transaction.amount}"
        holder.date.text = transaction.date
    }

    override fun getItemCount(): Int = transactions.size

    fun updateTransactions(newTransactions: List<Spending>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
