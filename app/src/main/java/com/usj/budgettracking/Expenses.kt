package com.usj.budgettracking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.usj.budgettracking.data.Expense
import com.usj.budgettracking.databinding.DialogAddExpenseBinding
import com.usj.budgettracking.databinding.FragmentExpensesBinding
import com.usj.budgettracking.viewmodel.BudgetViewModel
import com.usj.budgettracking.viewmodel.ExpensesViewModel
import java.text.SimpleDateFormat
import java.util.*

class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by activityViewModels()
    private val budgetViewModel: BudgetViewModel by activityViewModels()

    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnAddExpense.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun setupRecyclerView() {
        expensesAdapter = ExpensesAdapter(
            onItemEdit = { expense -> showEditExpenseDialog(expense) },
            onItemDelete = { expense -> expensesViewModel.deleteExpense(expense) }
        )
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.expensesRecyclerView.adapter = expensesAdapter
    }

    private fun setupObservers() {
        expensesViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            if (expenses.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.expensesRecyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.expensesRecyclerView.visibility = View.VISIBLE
            }
            expensesAdapter.submitList(expenses)
        }

        budgetViewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories.isNotEmpty()) {
                setupCategoryDropdown(categories.map { it.name })
            }
        }
    }

    private fun setupCategoryDropdown(categoryNames: List<String>) {
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun showAddExpenseDialog() {
        val dialogBinding = DialogAddExpenseBinding.inflate(layoutInflater)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Populate Spinner with categories
        budgetViewModel.categories.value?.let { categories ->
            val categoryNames = categories.map { it.name }
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.categorySpinner.adapter = spinnerAdapter
        }

        dialogBinding.dateInput.setText(dateFormat.format(calendar.time))

        dialogBinding.dateInput.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dialogBinding.dateInput.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("Add Expense")
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.expenseNameInput.text.toString()
                val amount = dialogBinding.expenseAmountInput.text.toString().toDoubleOrNull()
                val category = dialogBinding.categorySpinner.selectedItem?.toString()
                val date = dialogBinding.dateInput.text.toString()

                if (name.isNotBlank() && amount != null && category != null && date.isNotBlank()) {
                    expensesViewModel.addExpense(name, amount, category, date)
                } else {
                    Toast.makeText(requireContext(), "Invalid inputs", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showEditExpenseDialog(expense: Expense) {
        val dialogBinding = DialogAddExpenseBinding.inflate(layoutInflater)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        dialogBinding.expenseNameInput.setText(expense.name)
        dialogBinding.expenseAmountInput.setText(expense.amount.toString())
        dialogBinding.dateInput.setText(expense.date)

        budgetViewModel.categories.value?.let { categories ->
            val categoryNames = categories.map { it.name }
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.categorySpinner.adapter = spinnerAdapter

            val categoryIndex = categoryNames.indexOf(expense.category)
            if (categoryIndex >= 0) {
                dialogBinding.categorySpinner.setSelection(categoryIndex)
            }
        }

        dialogBinding.dateInput.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dialogBinding.dateInput.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("Edit Expense")
            .setPositiveButton("Update") { _, _ ->
                val name = dialogBinding.expenseNameInput.text.toString()
                val amount = dialogBinding.expenseAmountInput.text.toString().toDoubleOrNull()
                val category = dialogBinding.categorySpinner.selectedItem?.toString()
                val date = dialogBinding.dateInput.text.toString()

                if (name.isNotBlank() && amount != null && category != null && date.isNotBlank()) {
                    val updatedExpense = expense.copy(
                        name = name,
                        amount = amount,
                        category = category,
                        date = date
                    )
                    expensesViewModel.updateExpense(updatedExpense)
                } else {
                    Toast.makeText(requireContext(), "Invalid inputs", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        budgetViewModel.refreshCategories()
        expensesViewModel.loadExpenses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
