package com.example.tailoringmanagement.orderPageForTailors

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.tailoringmanagement.databinding.ActivityEditOrderDetailsBinding
import com.example.tailoringmanagement.localDB.OrderDBHelper
import java.util.*


class EditOrderDetails : AppCompatActivity() {

    private lateinit var binding: ActivityEditOrderDetailsBinding
    private var oid: String? = null
    private var cid: String? = null
    private var eid: String? = null
    private var payment: String? = null
    private var date: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar!!.title = "Edit Orders"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        oid = intent.getStringExtra("oid")
        cid = intent.getStringExtra("cid")
        eid = intent.getStringExtra("eid")
        payment = intent.getStringExtra("payment")
        date = intent.getStringExtra("date")

        // for date
        binding.tvEditDeliverDate.text = date

        binding.btnEditPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val selectedDate = "$day/${month + 1}/$year"
                    // Do something with the selected date

                    // Set the selected date to a TextView or any other desired location
                    binding.tvEditDeliverDate.text = selectedDate
                },
                currentYear,
                currentMonth,
                currentDay
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }


        // for text boxes
        binding.inputEditOrderID.setText(oid)
        binding.inputEditOrderID.isEnabled = false
        binding.inputEditOrderPrice.setText(payment)

        // Now for number picker
        val customerIds =
            GetData().getCustomerIds(this).map { it.toString() }.toMutableList()

//        Log.i("Size",customerIds.size.toString())
//        Log.i("Value 1",customerIds[0])
//        Log.i("Value 2",customerIds[1])
//        Log.i("Value 3",customerIds[2])

        val employeeIds =
            GetData().getEmployeesIds(this).map { it.toString() }.toMutableList()

        if (customerIds.isNotEmpty()) {
            val cidIndex = customerIds.indexOf(cid.toString())

            if (cidIndex >= 0 && cidIndex < customerIds.size) {
                if (cidIndex != 0) {
                    val temp = customerIds[0] // Store the value at index 0
                    customerIds[0] = customerIds[cidIndex] // Replace the value at index 0 with the value of cid
                    customerIds[cidIndex] = temp // Replace the value at cidIndex with the original value at index 0
                }
            }

            binding.npEditCustomerID.minValue = 0
            binding.npEditCustomerID.maxValue = customerIds.size - 1
        }

        if (employeeIds.isNotEmpty()) {
            val eidIndex = employeeIds.indexOf(eid.toString())
            if (eidIndex >= 0 && eidIndex < employeeIds.size) {
                if (eidIndex != 0) {
                    val temp = employeeIds[0] // Store the value at index 0
                    employeeIds[0] =
                        employeeIds[eidIndex] // Replace the value at index 0 with the value of eid
                    employeeIds[eidIndex] =
                        temp // Replace the value at eidIndex with the original value at index 0
                }
            }

            binding.npEditEmployeeID.minValue = 0
            binding.npEditEmployeeID.maxValue = employeeIds.size - 1
        }

        // set value for number pickers from above arrays

        if (customerIds.isNotEmpty()) {
            binding.npEditCustomerID.displayedValues = customerIds.toTypedArray()
        }

        if (employeeIds.isNotEmpty()) {
            binding.npEditEmployeeID.displayedValues = employeeIds.toTypedArray()
        }


        binding.btnCancelEditingOrder.setOnClickListener {
            if (checkIfThereIsChange()) {
                showCancelAlert()
            } else
                onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveOrder.setOnClickListener {
            if (checkIfThereIsChange()) {
                Log.i("Called","Here")
                val db = OrderDBHelper(this)
                db.updateOrdersInfo(
                    oid!!.toInt(),
                    OrderDBHelper.COLUMN_CID,
           binding.npEditCustomerID.displayedValues[binding.npEditCustomerID.value]
                )
                db.updateOrdersInfo(
                    oid!!.toInt(),
                    OrderDBHelper.COLUMN_EID,
                binding.npEditEmployeeID.displayedValues[binding.npEditEmployeeID.value]
                )
                db.updateOrdersInfo(
                    oid!!.toInt(),
                    OrderDBHelper.COLUMN_PRICE,
                binding.inputEditOrderPrice.text.toString()
                )
                db.updateOrdersInfo(
                    oid!!.toInt(),
                    OrderDBHelper.COLUMN_DATE,
                binding.tvEditDeliverDate.text.toString()
                )

            }
            Log.i("Called","Here")

           onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDeleteOrder.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Sure to Delete Order?")
                .setTitle("Delete Order")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Order Deleted!", Toast.LENGTH_SHORT).show()
                    val db = OrderDBHelper(this)
                    db.deleteOrder(oid!!.toInt())
                   // RestartActivity().restartAct(this)
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
                } else
                    onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun checkIfThereIsChange(): Boolean {
        val selectedCid = binding.npEditCustomerID.displayedValues[binding.npEditCustomerID.value].toString()
        val selectedEid = binding.npEditEmployeeID.displayedValues[binding.npEditEmployeeID.value].toString()
        val selectedPayment = binding.inputEditOrderPrice.text.toString()
        val selectedDate = binding.tvEditDeliverDate.text.toString()
        Log.i("SelectedCid",selectedCid)
        Log.i("SelectedEid",selectedEid)
        Log.i("SelectedPayment",selectedPayment)
        Log.i("SelectedDate",selectedDate)
        Log.i("payment",payment.toString())


        return (cid.toString() != selectedCid ||
                eid.toString() != selectedEid ||
                payment.toString() != selectedPayment ||
                date.toString() != selectedDate)
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