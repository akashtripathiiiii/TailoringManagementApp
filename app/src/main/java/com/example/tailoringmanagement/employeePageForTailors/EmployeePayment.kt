package com.example.tailoringmanagement.employeePageForTailors

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import com.example.tailoringmanagement.databinding.ActivityEmployeePaymentBinding
import com.example.tailoringmanagement.localDB.EmpDBHelper

class EmployeePayment : AppCompatActivity()
{
    private lateinit var binding: ActivityEmployeePaymentBinding
    private var nsuits: String? = null
    private var id: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Employee Wage"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nsuits = intent.getStringExtra("nsuits")
        id = intent.getStringExtra("id")
        binding.inputNumSuits.setText(nsuits)
        binding.inputNumSuits.isEnabled = false

        binding.inputWagePerSuit.setText("0")
        binding.inputWagePerSuit.addTextChangedListener {
            if (it.toString() == "")
                binding.tvAmount.text = "0"
            else
                binding.tvAmount.text = ""+(it.toString().toInt()*nsuits!!.toInt())
        }

        binding.btnPayEmployee.setOnClickListener {
            val db = EmpDBHelper(this)
            db.updateEmployeeInfo(id!!.toInt(), EmpDBHelper.COLUMN_NO_SUITS, "0")
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}