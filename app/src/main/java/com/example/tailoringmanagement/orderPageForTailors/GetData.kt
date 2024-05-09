package com.example.tailoringmanagement.orderPageForTailors

import android.content.Context
import com.example.tailoringmanagement.localDB.DBHelper
import com.example.tailoringmanagement.localDB.EmpDBHelper

class GetData {
    private lateinit var cdb: DBHelper
    private lateinit var db: EmpDBHelper

    fun getEmployeesIds(context : Context) : List<Int> {
        db = EmpDBHelper(context)
        val cursor = db.getAllEmployees()
        val empIds = mutableListOf<Int>()

        while (cursor!!.moveToNext()) {
            empIds .add(
                cursor.getInt(0),

                )
        }
        return empIds
    }
    fun getCustomerIds(context : Context) : List<Int> {
        cdb = DBHelper(context)
        val cursor = cdb.getAllCustomers()
        val cusIds = mutableListOf<Int>()

        while (cursor!!.moveToNext()) {
            cusIds  .add(
                cursor.getInt(0),
            )
        }
        return  cusIds
    }

}