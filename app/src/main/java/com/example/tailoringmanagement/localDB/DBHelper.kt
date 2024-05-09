package com.example.tailoringmanagement.localDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.database.*


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val contextValue = context
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val customerReference: DatabaseReference = database.getReference(TABLE_CUSTOMER)

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_CUSTOMER (" +
                "$COLUMN_CID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PHONE TEXT);"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        onCreate(db)
    }

    fun addCustomer(id: Int, name: String, phone: String): Boolean {
        val values = ContentValues()

        values.put(COLUMN_CID, id)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)

        val db = this.writableDatabase

        val result = db.insert(TABLE_CUSTOMER, null, values)
        db.close()
        // Sync data with Firebase
        syncToFirebase(id, name, phone)

        return result != -1L
    }

    fun deleteCustomer(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CUSTOMER, "$COLUMN_CID = ?", arrayOf(id.toString()))
        val orderDB = OrderDBHelper(contextValue)
        // 2nd parameter column to change and last parameter for condition based column
        // first parameter is condition like order id, customer id and employee id only
        orderDB.updateOrdersInfo(id, COLUMN_CID, null, COLUMN_CID)

        // Sync data with Firebase
        customerReference.child(id.toString()).removeValue()
    }

    fun getAllCustomers(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_CUSTOMER", null)
    }


    fun updateCustomerInfo(id: Int, columnToChange: String, updatedValue: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(columnToChange, updatedValue)
        db.update(TABLE_CUSTOMER, values, "$COLUMN_CID = ?", arrayOf(id.toString()))
        db.close()
        // Sync data with Firebase
        updateFirebaseData(id, columnToChange, updatedValue)
    }

    private fun syncToFirebase(id: Int, name: String?, phone: String?) {
        val customerData = HashMap<String, Any?>()
        customerData[COLUMN_CID] = id
        customerData[COLUMN_NAME] = name
        customerData[COLUMN_PHONE] = phone

        customerReference.child(id.toString()).setValue(customerData)
    }
    private fun updateFirebaseData(id: Int, columnToChange: String, updatedValue: String?) {
        val customerData = HashMap<String, Any?>()
        customerData[columnToChange] = updatedValue

        customerReference.child(id.toString()).updateChildren(customerData)
    }


    companion object {
        private const val DATABASE_NAME = "CustomerDirectory"
        private const val DATABASE_VERSION = 1

        const val TABLE_CUSTOMER = "Customer"
        const val COLUMN_CID = "CustomerID"
        const val COLUMN_NAME = "CustomerName"
        const val COLUMN_PHONE = "CustomerPhone"
    }
}