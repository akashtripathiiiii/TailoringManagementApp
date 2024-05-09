package com.example.tailoringmanagement.customerPageForTailors

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tailoringmanagement.activities.EditCustomerInfo
import com.example.tailoringmanagement.apparel.ActivityApparelSelector
import com.example.tailoringmanagement.databinding.RvCustomersBinding
import com.example.tailoringmanagement.localDB.DBHelper

class RvAdapterCustomer(private  var customerList : ArrayList<RvCustomersData>, var context: Context) : RecyclerView.Adapter<RvAdapterCustomer.MyViewHolder> () {
    inner class MyViewHolder(var binding : RvCustomersBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvCustomersBinding.inflate(LayoutInflater.from(context), parent,false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        animation(holder.itemView)
        holder.binding.tvCustomerId.text = customerList[position].id.toString()
        holder.binding.tvCustomerName.text = customerList[position].name
        holder.binding.tvCustomerPhoneNumber.text = customerList[position].phoneNumber

        holder.binding.btnDelete.setOnClickListener {
            val removedPosition = holder.bindingAdapterPosition

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Sure to delete customer?\n All sizes will be deleted.")
                .setTitle("Delete Customer")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(context, "Customer Deleted!", Toast.LENGTH_SHORT).show()
                    val customer = customerList[removedPosition]
                    customerList.removeAt(removedPosition)
                    notifyItemRemoved(removedPosition)
                    val db = DBHelper(context)
                    db.deleteCustomer(customer.id)
                }
                .setNegativeButton("No", null)
            val dialog = builder.create()
            dialog.show()
        }

        holder.binding.btnEdit.setOnClickListener {
            val editPosition = holder.bindingAdapterPosition
            val customer = customerList[editPosition]
            val intent = Intent(context, EditCustomerInfo::class.java)

            intent.putExtra("id", "" + customer.id)
            intent.putExtra("name", "" + customer.name)
            intent.putExtra("ph", "" + customer.phoneNumber)

            context.startActivity(intent)
        }

        holder.binding.btnEditCustomers.setOnClickListener {
            val intent = Intent(context, ActivityApparelSelector::class.java)
            context.startActivity(intent)
        }
    }

    private fun animation (view : View){
        val animation = AlphaAnimation(0.0f,1.0f)
        animation.duration = 1340
        view.startAnimation(animation)
    }
}