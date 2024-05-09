package com.example.tailoringmanagement.employeePageForTailors

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.localDB.EmpDBHelper

class DialogNewEmpDetails : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_dialog_new_emp_details, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelAddingEmployee)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnSaveNewEmployee)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnAdd.setOnClickListener {
            val tvId = dialogView.findViewById<TextView>(R.id.inputNewEmployeeID)
            val tvName = dialogView.findViewById<TextView>(R.id.inputNewEmployeeName)
            val tvPhone = dialogView.findViewById<TextView>(R.id.inputNewEmployeePhoneNumber)
            val id = tvId.text?.toString() ?: ""
            val name = tvName.text?.toString() ?: ""
            val number = tvPhone.text?.toString() ?: ""
            if(id!="" && name!="" && number!="") {
                val db = EmpDBHelper(requireContext())
                if (db.addEmployee(id.toInt(), name, "0", number))
                    Toast.makeText(requireContext(), "Employee Added", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Employee Already Exist", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainer, FragmentRVEmployeeRecord()).commit()
                dismiss()
            }
            else {
                Toast.makeText(requireActivity(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setView(dialogView).setMessage("Add New Customer")
        return builder.create()
    }
}