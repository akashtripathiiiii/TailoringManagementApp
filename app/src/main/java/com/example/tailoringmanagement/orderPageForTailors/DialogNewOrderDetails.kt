package com.example.tailoringmanagement.orderPageForTailors

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.localDB.OrderDBHelper
import java.util.*
//import java.math.BigDecimal
//import java.math.RoundingMode

class DialogNewOrderDetails : DialogFragment() {
    private lateinit var empIds: List<Int>
    private lateinit var cusIds: List<Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

//        db = EmpDBHelper(requireActivity(), null)
//        cdb = DBHelper(requireActivity(), null)
        empIds = GetData().getEmployeesIds(requireContext())
        cusIds = GetData().getCustomerIds(requireContext())

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_dialog_new_order_details, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelAddingOrder)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnSaveNewOrder)
        val btnPickDate = dialogView.findViewById<Button>(R.id.btnPickDate)
        val tvDate = dialogView.findViewById<TextView>(R.id.tvDeliverDate)

        val tvOCid = dialogView.findViewById<NumberPicker>(R.id.npCustomerID)
        val tvOEid = dialogView.findViewById<NumberPicker>(R.id.npEmployeeID)

        // assign values to number picker
       if(empIds.isNotEmpty()) {
           tvOEid.minValue = 0
           tvOEid.maxValue = empIds.size - 1
           tvOEid.displayedValues = empIds.map { it.toString() }.toTypedArray()
       }
        else {
            Toast.makeText(requireContext(),"Add Employees Please",Toast.LENGTH_SHORT).show()
           dismiss()
        }
        if(cusIds.isNotEmpty()) {
            tvOCid.minValue = 0
            tvOCid.maxValue = cusIds.size - 1
            tvOCid.displayedValues = cusIds.map { it.toString() }.toTypedArray()
        }
        else {
            Toast.makeText(requireContext(),"Add Customers Please",Toast.LENGTH_SHORT).show()
        }

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val selectedDate = "$day/${month + 1}/$year"
                    // Do something with the selected date

                    // Set the selected date to a TextView or any other desired location
                    tvDate.text = selectedDate
                },
                currentYear,
                currentMonth,
                currentDay
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }



        btnCancel.setOnClickListener {
            dismiss()
        }

        btnAdd.setOnClickListener {
            val tvOId = dialogView.findViewById<TextView>(R.id.inputOrderID)
            val tvOPrice = dialogView.findViewById<TextView>(R.id.inputOrderPrice)

            val cid = tvOCid.displayedValues[tvOCid.value].toString()
            val eid = tvOEid.displayedValues[tvOEid.value].toString()

//            tvOCid.setOnValueChangedListener { _, _, newVal ->
//                // Do something with the selected value
//                cid = newVal.toString()
//                // ...
//            }
//            tvOEid.setOnValueChangedListener { _, _, newVal ->
//                // Do something with the selected value
//                eid = newVal.toString()
//                // ...
//            }
//
//            Toast.makeText(requireContext(),cid,Toast.LENGTH_SHORT).show()
//            Toast.makeText(requireContext(),eid,Toast.LENGTH_SHORT).show()


            val oid   =  tvOId.text?.toString() ?: ""
            val price =  tvOPrice.text?.toString() ?: ""

          //  price = truncateDecimalPlaces(price)

            val date = tvDate.text?.toString() ?: ""




            if(oid!="" && cid!="" && eid!="" && price!="" && date!="Pick Order Deliver Date") {
                val db = OrderDBHelper(requireContext())
                if (db.addOrder(oid.toInt(),cid.toInt(),eid.toInt(),price.toFloat(),date))
                    Toast.makeText(requireContext(), "Order Added", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Order Already Exist", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()

                    .replace(R.id.frameLayoutContainer, FragmentRVOrderRecord()).commit()
                dismiss()
            }
            else {
                Toast.makeText(requireActivity(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setView(dialogView).setMessage("Add New Order")
        return builder.create()
    }
//   private fun truncateDecimalPlaces(input: String): String {
//        val decimalSeparatorIndex = input.indexOf('.')
//        return if (decimalSeparatorIndex != -1 && decimalSeparatorIndex + 5 < input.length) {
//            input.substring(0, decimalSeparatorIndex + 5)
//        } else {
//            input
//        }
//    }

//    fun getEmployeesIds(activity : Activity) : List<Int> {
//        db = EmpDBHelper(activity, null)
//        val cursor = db.getAllEmployees()
//        val empIds = mutableListOf<Int>()
//
//        while (cursor!!.moveToNext()) {
//            empIds .add(
//                    cursor.getInt(0),
//
//            )
//        }
//        return empIds
//    }
//    fun getCustomerIds(activity : Activity) : List<Int> {
//        cdb = DBHelper(activity, null)
//        val cursor = cdb.getAllCustomers()
//        val cusIds = mutableListOf<Int>()
//
//        while (cursor!!.moveToNext()) {
//            cusIds  .add(
//                cursor.getInt(0),
//                )
//        }
//        return  cusIds
//    }
}