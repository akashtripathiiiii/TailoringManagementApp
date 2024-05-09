package com.example.tailoringmanagement.employeePageForTailors

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.databinding.RvEmployeeBinding
import com.example.tailoringmanagement.localDB.EmpDBHelper

class RVAdapterEmployee(private val employeeList: ArrayList<RVEmployeeData>, private val context: Context) :
    RecyclerView.Adapter<RVAdapterEmployee.MyViewHolder>() {

    inner class MyViewHolder(val binding: RvEmployeeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvEmployeeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        animation(holder.itemView)
        val employee = employeeList[position]
        val binding = holder.binding

        binding.tvEmpID.text = context.getString(R.string.emp_id, employee.id)
        binding.tvEmpName.text = employee.name
        binding.tvEmpNumSuits.text = context.getString(R.string.emp_num_suits, employee.numSuit)
        binding.tvEmpPhone.text = context.getString(R.string.emp_phone, employee.phone)

        binding.btnDeleteEmp.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Sure to Delete Employee?\nAll Records Will be Deleted.")
                .setTitle("Delete Employee")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(context, "Employee Deleted!", Toast.LENGTH_SHORT).show()
                    removeEmployee(position)
                }
                .setNegativeButton("No", null)
                .create()
                .show()
        }

        binding.btnEditEmpDetails.setOnClickListener {
            val intent = Intent(context, EditEmployeeDetails::class.java)
            intent.putExtra("id", employee.id.toString())
            intent.putExtra("name", employee.name)
            intent.putExtra("nsuits", employee.numSuit.toString())
            intent.putExtra("phone", employee.phone)
            context.startActivity(intent)
        }

        binding.btnEmpPayment.setOnClickListener {
            val intent = Intent(context, EmployeePayment::class.java)
            intent.putExtra("id", employee.id.toString())
            intent.putExtra("nsuits", employee.numSuit.toString())
            context.startActivity(intent)
        }

        binding.btnIncrementSuit.setOnClickListener {
            employee.numSuit++
            notifyItemChanged(position)
            val db = EmpDBHelper(context)
            db.updateEmployeeInfo(employee.id, EmpDBHelper.COLUMN_NO_SUITS, employee.numSuit.toString())
        }
    }

    private fun removeEmployee(position: Int) {
        val db = EmpDBHelper(context)
        db.deleteEmployee(employeeList[position].id)
        employeeList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun animation(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1340
        view.startAnimation(animation)
    }
}
