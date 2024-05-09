package com.example.tailoringmanagement.employeePageForTailors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tailoringmanagement.databinding.FragmentRVEmployeeRecordBinding
import com.example.tailoringmanagement.localDB.EmpDBHelper

class FragmentRVEmployeeRecord : Fragment() {
    private lateinit var binding: FragmentRVEmployeeRecordBinding
    private lateinit var db: EmpDBHelper
    private lateinit var employeeAdapter: RVAdapterEmployee
    private var empList: ArrayList<RVEmployeeData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRVEmployeeRecordBinding.inflate(layoutInflater, container, false)
        db = EmpDBHelper(requireActivity())
        setupRecyclerView()
        setupAddButtonListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        displayEmployee()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEmployee.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEmployee.setHasFixedSize(true)
    }

    private fun setupAddButtonListener() {
        binding.btnAddNewEmp.setOnClickListener {
            val dialog = DialogNewEmpDetails()
            dialog.show(requireActivity().supportFragmentManager, "Add New Employee")
        }
    }

    private fun displayEmployee() {
        val cursor = db.getAllEmployees()
        empList.clear()
        while (cursor!!.moveToNext()) {
            empList.add(
                RVEmployeeData(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2).toInt(),
                    cursor.getString(3)
                )
            )
        }
        employeeAdapter = RVAdapterEmployee(empList, requireActivity())
        binding.recyclerViewEmployee.adapter = employeeAdapter
    }
}
