package com.example.tailoringmanagement.customerPageForTailors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tailoringmanagement.databinding.FragmentRVCustomerRecordBinding
import com.example.tailoringmanagement.localDB.DBHelper
import com.example.tailoringmanagement.localDB.InternetChecker
import com.google.firebase.database.*

class FragmentRVCustomerRecord : Fragment() {
    private lateinit var binding: FragmentRVCustomerRecordBinding
    private lateinit var db: DBHelper
    private lateinit var customerAdapter : RvAdapterCustomer
    private lateinit var customersList : ArrayList<RvCustomersData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRVCustomerRecordBinding.inflate(layoutInflater, container, false)

        binding.btnAddNewCustomer.setOnClickListener {
            val dialog = DialogNewCustDetails()
            dialog.show(requireActivity().supportFragmentManager, "Add New Customer")
            displayCustomers()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        displayCustomers()
    }

    private fun displayCustomers() {
        customersList = ArrayList()
        // Check if internet is available
        if (InternetChecker(requireActivity()).isInternetAvailable()) {
            displayFromFirebase()
            Toast.makeText(requireContext(),"Loaded from Firebase",Toast.LENGTH_SHORT).show()
        }
        else {
            customersList.clear()
            val cursor = db.getAllCustomers()

            while (cursor!!.moveToNext()) {
                customersList.add(
                    RvCustomersData(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            showCustomersList()
            Toast.makeText(requireContext(),"Loaded from Local DB",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCustomersList() {
        if (!isAdded) {
            // Fragment is not attached to an activity, return
            return
        }
        customerAdapter = RvAdapterCustomer(customersList, requireActivity())
        binding.recyclerViewCustomers.adapter = customerAdapter
        binding.recyclerViewCustomers.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun displayFromFirebase() {
        customersList.clear()
        FirebaseDatabase.getInstance().getReference(DBHelper.TABLE_CUSTOMER).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (customerSnapshot in snapshot.children) {
                    val id = customerSnapshot.child(DBHelper.COLUMN_CID).getValue(Int::class.java)?.toInt()
                    val name = customerSnapshot.child(DBHelper.COLUMN_NAME).getValue(String::class.java) ?: ""
                    val phone = customerSnapshot.child(DBHelper.COLUMN_PHONE).getValue(String::class.java) ?: ""
                    Log.i("After Inserting", name)
                    Log.i("After Inserting", id.toString())
                    Log.i("After Inserting", phone)

                    customersList.add(RvCustomersData(id ?: 0, name, phone))
                }
                showCustomersList()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
        Log.i("After Inserting","Here SuccessFully")
    }
}
