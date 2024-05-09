package com.example.tailoringmanagement.orderPageForTailors

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.databinding.RvOrdersBinding
import com.example.tailoringmanagement.localDB.OrderDBHelper

class RvAdapterOrder(private var orderList: ArrayList<RVOrderData>, private val context: Context) :
    RecyclerView.Adapter<RvAdapterOrder.MyViewHolder>() {

    inner class MyViewHolder(var binding: RvOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvOrdersBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: RvAdapterOrder.MyViewHolder, position: Int) {
        animation(holder.itemView)
        val order = orderList[position]
        holder.binding.tvOrderID.text = context.getString(R.string.order_id, order.oid)
        holder.binding.tvEmployeeId.text =
            if (order.eid == "Deleted Employee") "Deleted Employee" else context.getString(R.string.emp_id, order.eid?.toInt())
        holder.binding.tvCustomerIdOrder.text =
            if (order.cid == "Deleted Customer") "Deleted Customer" else context.getString(R.string.customer_id, order.cid?.toInt())
        holder.binding.tvPayment.text = context.getString(R.string.payment_order, order.payment)
        holder.binding.tvDeliverDate.text = "Order Date: " + order.date

        holder.binding.btnDeleteOrder.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Sure to Delete Order?")
                .setTitle("Delete Order")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(context, "Order Deleted!", Toast.LENGTH_SHORT).show()
                    orderList.removeAt(position)
                    notifyItemRemoved(position)
                    val db = OrderDBHelper(context)
                    db.deleteOrder(order.oid)
                }
                .setNegativeButton("No", null)
                .create()
                .show()
        }

        holder.binding.btnEditOrderDetails.setOnClickListener {
            val intent = Intent(context, EditOrderDetails::class.java).apply {
                putExtra("oid", order.oid.toString())
                putExtra("cid", order.cid.toString())
                putExtra("eid", order.eid.toString())
                putExtra("payment", order.payment.toString())
                putExtra("date", order.date)
            }
            context.startActivity(intent)
        }

        val currentItem = orderList[position]

        holder.binding.btnMarkOrderComplete.setOnClickListener {
            // Case when user clicks it
            // Change the color of the clicked item
            val clickedItemColor = ContextCompat.getColor(context, R.color.light_green)
            saveClickedItemColor(currentItem.oid.toString(), clickedItemColor)

            // Update the item view
            holder.itemView.setBackgroundColor(clickedItemColor)
        }
        // Case when recycler view become visible

        // Retrieve the saved color for the clicked item
        val clickedItemColor = getClickedItemColor(currentItem.oid.toString())
        holder.itemView.setBackgroundColor(clickedItemColor)
    }

    private fun saveClickedItemColor(orderId: String, colorResId: Int) {
        val sharedPreferences = context.getSharedPreferences("ClickedItems", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(orderId, colorResId)
        editor.apply()
    }

    private fun getClickedItemColor(orderId: String): Int {
        val sharedPreferences = context.getSharedPreferences("ClickedItems", Context.MODE_PRIVATE)
        return sharedPreferences.getInt(orderId, android.R.color.transparent)
    }

    private fun animation(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1340
        view.startAnimation(animation)
    }
}
