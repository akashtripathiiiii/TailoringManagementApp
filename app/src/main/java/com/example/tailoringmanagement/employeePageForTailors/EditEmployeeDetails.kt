package com.example.tailoringmanagement.employeePageForTailors

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.tailoringmanagement.databinding.ActivityEditEmployeeDetailsBinding
import com.example.tailoringmanagement.localDB.EmpDBHelper

class EditEmployeeDetails : AppCompatActivity() {

    private lateinit var binding: ActivityEditEmployeeDetailsBinding
    private var id: String? = null
    private var name: String? = null
    private var nsuits: String? = null
    private var ph: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Edit Employee"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
        nsuits = intent.getStringExtra("nsuits")
        ph = intent.getStringExtra("phone")

        binding.inputEmployeeID.setText(id)
        binding.inputEmployeeID.isEnabled = false
        binding.inputEmployeeName.setText(name)
        binding.inputNumSuits.setText(nsuits)
        binding.inputEmployeePhoneNumber.setText(ph)

        binding.btnCancelEditingEmployee.setOnClickListener {
            if (checkIfThereIsChange())
            {
                showCancelAlert()
            }else
                onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveEmployee.setOnClickListener {
            if (checkIfThereIsChange()){
                val db = EmpDBHelper(this)
                db.updateEmployeeInfo(id!!.toInt(), EmpDBHelper.COLUMN_PHONE, ""+binding.inputEmployeePhoneNumber.text)
                db.updateEmployeeInfo(id!!.toInt(), EmpDBHelper.COLUMN_NAME, ""+binding.inputEmployeeName.text)
                db.updateEmployeeInfo(id!!.toInt(), EmpDBHelper.COLUMN_NO_SUITS, ""+binding.inputNumSuits.text)
            }
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDeleteEmployee.setOnClickListener {
            AlertDialog.Builder(this).setMessage("Sure to Delete Employee?\nAll Records Will be Deleted.")
                .setTitle("Delete Customer")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Customer Deleted!", Toast.LENGTH_SHORT).show()
                    val db = EmpDBHelper(this)
                    db.deleteEmployee(id!!.toInt())
                    onBackPressedDispatcher.onBackPressed()
                }
                .setNegativeButton("No", null)
                .create()
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (checkIfThereIsChange()) {
                    showCancelAlert()
                }else
                    onBackPressedDispatcher.onBackPressed()
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun checkIfThereIsChange(): Boolean {
        return (name != ""+binding.inputEmployeeName.text) ||
                (ph != ""+binding.inputEmployeePhoneNumber.text) ||
                (nsuits != ""+binding.inputNumSuits.text)
    }

    private fun showCancelAlert() {
        AlertDialog.Builder(this).setTitle("Alert")
            .setMessage("Sure To Cancel?\nAll Changes Will Be Lost.")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                onBackPressedDispatcher.onBackPressed()
            }
            .create()
            .show()
    }
}