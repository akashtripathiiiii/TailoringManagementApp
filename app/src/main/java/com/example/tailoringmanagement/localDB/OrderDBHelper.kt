package com.example.tailoringmanagement.localDB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val orderReference: DatabaseReference = database.getReference(TABLE_ORDER)

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_ORDER (" +
                "$COLUMN_OID INTEGER PRIMARY KEY, " +
                "$COLUMN_CID INTEGER , " +
                "$COLUMN_EID INTEGER, " +
                "$COLUMN_PRICE REAL NOT NULL, " +
                "$COLUMN_DATE TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
        onCreate(db)
    }

    fun addOrder(oid: Int, cid: Int, eid: Int, payment: Float, date: String): Boolean {
        val values = ContentValues()

        values.put(COLUMN_OID, oid)
        values.put(COLUMN_CID, cid)
        values.put(COLUMN_EID, eid)
        values.put(COLUMN_PRICE, payment)
        values.put(COLUMN_DATE, date)

        val db = this.writableDatabase

        val result = db.insert(TABLE_ORDER, null, values)
        db.close()
        if(result != -1L)
            syncOrderToFirebase(oid,cid,eid,payment,date)

        return result != -1L
    }

    fun deleteOrder(oid: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_ORDER, "$COLUMN_OID = ?", arrayOf(oid.toString()))
        db.close()
        syncDeleteOrderToFirebase(oid)
    }
//    fun deleteOrderByColumn(id: Int,col : String) {
//        val db = this.writableDatabase
//        db.delete(TABLE_ORDER, "$col = ?", arrayOf(id.toString()))
//        db.close()
//    }


    fun getAllOrders(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_ORDER", null)
    }

    fun updateOrdersInfo(id: Int, columnToChange: String, updatedValue: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(columnToChange, updatedValue)
        db.update(TABLE_ORDER, values, "$COLUMN_OID = ?", arrayOf(id.toString()))
        db.close()
        syncUpdateOrderToFirebase(id,columnToChange,updatedValue)
    }
    @SuppressLint("Range")
    fun updateOrdersInfo(id: Int, columnToChange: String, updatedValue: String?, columnToUpdate: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(columnToChange, updatedValue)
        db.update(TABLE_ORDER, values, "$columnToUpdate = ?", arrayOf(id.toString()))
        db.close()

        val read = this.readableDatabase
        // accessing oid using any random column like eid,cid
        val cursor = read.rawQuery(
            "SELECT $COLUMN_OID FROM $TABLE_ORDER WHERE $columnToUpdate=?",
            arrayOf(id.toString())
        )

        if (cursor != null && cursor.moveToFirst() && cursor.count>0) {
            val firebaseOId: Int = cursor.getInt(cursor.getColumnIndex(COLUMN_OID))
            syncUpdateOrderToFirebase(firebaseOId, columnToChange, updatedValue)
        }

        cursor?.close()
        read.close()
    }
    private fun syncOrderToFirebase(oid: Int, cid: Int, eid: Int, payment: Float, date: String) {
        val orderData = HashMap<String, Any?>()
        orderData[COLUMN_OID] = oid
        orderData[COLUMN_CID] = cid
        orderData[COLUMN_EID] = eid
        orderData[COLUMN_PRICE] = payment
        orderData[COLUMN_DATE] = date

        orderReference.child(oid.toString()).setValue(orderData)
    }

    private fun syncDeleteOrderToFirebase(oid: Int) {
        orderReference.child(oid.toString()).removeValue()
    }

    private fun syncUpdateOrderToFirebase(oid: Int, columnToChange: String, updatedValue: String?) {
        val updatedData = HashMap<String, Any?>()
        updatedData[columnToChange] = updatedValue

        orderReference.child(oid.toString()).updateChildren(updatedData)
    }



    companion object {
        // database name and version
        private const val DATABASE_NAME = "OrdersDirectory"
        private const val DATABASE_VERSION = 1

        // table name

        const val TABLE_ORDER = "Orders"

        // column names
        const val COLUMN_OID= "OrderID"
        const val COLUMN_CID = "CustomerID"
        const val COLUMN_EID = "EmployeeID"
        const val COLUMN_PRICE = "OrderPrice"
        const val COLUMN_DATE = "Date"
    }
}