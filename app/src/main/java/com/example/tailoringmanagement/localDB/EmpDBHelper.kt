package com.example.tailoringmanagement.localDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmpDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val contextValue = context
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val employeeReference: DatabaseReference = database.getReference(TABLE_EMPLOYEE)

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_EMPLOYEE (" +
                "$COLUMN_EID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_NO_SUITS TEXT, " +
                "$COLUMN_PHONE TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EMPLOYEE")
        onCreate(db)
    }

    fun addEmployee(id: Int, name: String, suits: String, phone: String): Boolean {
        val values = ContentValues()

        values.put(COLUMN_EID, id)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_NO_SUITS, suits)
        values.put(COLUMN_PHONE, phone)

        val db = this.writableDatabase

        val result = db.insert(TABLE_EMPLOYEE, null, values)
        db.close()
        syncToFirebase(id,name,suits,phone)

        return result != -1L
    }

    fun deleteEmployee(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_EMPLOYEE, "$COLUMN_EID = ?", arrayOf(id.toString()))
        val orderDB = OrderDBHelper(contextValue)
        orderDB.updateOrdersInfo(id, COLUMN_EID, null, COLUMN_EID)
        db.close()
        employeeReference.child(id.toString()).removeValue()
    }

    fun getAllEmployees(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_EMPLOYEE", null)
    }

    fun updateEmployeeInfo(id: Int, columnToChange: String, updatedValue: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(columnToChange, updatedValue)
        db.update(TABLE_EMPLOYEE, values, "$COLUMN_EID = ?", arrayOf(id.toString()))
        db.close()
        updateFirebase(id,columnToChange,updatedValue)
    }
    private fun updateFirebase(id: Int, columnToChange: String, updatedValue: String?) {
        val employeeData = HashMap<String, Any?>()
        employeeData[columnToChange] = updatedValue

        employeeReference.child(id.toString()).updateChildren(employeeData)
    }

    private fun syncToFirebase(id: Int, name: String, noOfSuits: String, phone: String) {
        val employeeData = HashMap<String, Any?>()
        employeeData[COLUMN_EID] = id
        employeeData[COLUMN_NAME] = name
        employeeData[COLUMN_NO_SUITS] = noOfSuits
        employeeData[COLUMN_PHONE] = phone

        employeeReference.child(id.toString()).setValue(employeeData)
    }

    companion object {
        private const val DATABASE_NAME = "EmployeeDirectory"
        private const val DATABASE_VERSION = 1

        const val TABLE_EMPLOYEE = "Employee"
        const val COLUMN_EID = "EmployeeID"
        const val COLUMN_NAME = "EmployeeName"
        const val COLUMN_NO_SUITS = "NumSuits"
        const val COLUMN_PHONE = "EmployeePhone"
    }
}