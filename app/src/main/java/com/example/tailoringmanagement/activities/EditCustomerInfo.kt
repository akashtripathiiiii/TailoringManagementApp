package com.example.tailoringmanagement.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.tailoringmanagement.databinding.ActivityEditCustomerInfoBinding
import com.example.tailoringmanagement.localDB.DBHelper

class EditCustomerInfo : AppCompatActivity() {

    private lateinit var binding: ActivityEditCustomerInfoBinding
    private var id: String? = null
    private var name: String? = null
    private var ph: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Edit Customer"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
        ph = intent.getStringExtra("ph")

        binding.inputCustomerID.setText(id)
        binding.inputCustomerID.isEnabled = false

        binding.inputCustomerName.setText(name)
        binding.inputCustomerPhoneNumber.setText(ph)

        binding.btnCancelEditingCustomer.setOnClickListener {
            if (checkIfThereIsChange())
            {
                showCancelAlert()
            }else
                onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveCustomer.setOnClickListener {
            if (checkIfThereIsChange()){
                val db = DBHelper(this)
                db.updateCustomerInfo(id!!.toInt(), DBHelper.COLUMN_PHONE, ""+binding.inputCustomerPhoneNumber.text)
                db.updateCustomerInfo(id!!.toInt(), DBHelper.COLUMN_NAME, ""+binding.inputCustomerName.text)
            }
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDeleteCustomer.setOnClickListener {
            AlertDialog.Builder(this).setMessage("Sure to delete customer?\n All sizes will be deleted.")
                .setTitle("Delete Employee")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Employee Deleted!", Toast.LENGTH_SHORT).show()
                    val db = DBHelper(this)
                    db.deleteCustomer(id!!.toInt())
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
        return (name != ""+binding.inputCustomerName.text) ||
                (ph != ""+binding.inputCustomerPhoneNumber.text)
    }

    private fun showCancelAlert(){
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