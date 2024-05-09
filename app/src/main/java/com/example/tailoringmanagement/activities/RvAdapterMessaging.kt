package com.example.tailoringmanagement.activities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.tailoringmanagement.databinding.ReceiverMessageBinding
import com.example.tailoringmanagement.databinding.SenderMessageBinding
import com.google.firebase.auth.FirebaseAuth

class RvAdapterMessaging(private var messages: ArrayList<Message>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SendViewHolder(var binding: SenderMessageBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ReceiveViewHolder(var binding: ReceiverMessageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val senderBinding =
                SenderMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SendViewHolder(senderBinding)
        }
        val receiverBinding =
            ReceiverMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiveViewHolder(receiverBinding)

    }

    override fun getItemViewType(position: Int): Int {
        val user = messages[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid == user.senderId)
            0 // 0 for sender
        else
            1 // for receiver
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        animation(holder.itemView)
        val message = messages[position]
        if (holder is SendViewHolder) {
            holder.binding.tvSenderMessage.text = message.message.toString()
        } else if (holder is ReceiveViewHolder) {
            holder.binding.tvReceiverMessage.text = message.message.toString()
        }
    }

    private fun animation(view: View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1340
        view.startAnimation(animation)
    }
}
