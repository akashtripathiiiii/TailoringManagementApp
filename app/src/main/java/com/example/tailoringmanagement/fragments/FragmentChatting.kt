package com.example.tailoringmanagement.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tailoringmanagement.databinding.FragmentChattingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FragmentChatting : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private lateinit var userList: ArrayList<UserChatModel>
    private lateinit var adapter: RvAdapterChatting
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        userList = ArrayList()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Set adapter on RecyclerView
        adapter = RvAdapterChatting(userList, requireContext())
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        db.child("Users").addValueEventListener(object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (snap in snapshot.children) {
                    val userId = snap.child("key").value.toString()
                    val userName = snap.child("name").value.toString()

                    if (auth.currentUser?.uid != userId) {
                        userList.add(UserChatModel(userId, userName))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })

        return binding.root
    }
}
