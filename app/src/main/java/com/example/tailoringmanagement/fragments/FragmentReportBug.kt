package com.example.tailoringmanagement.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tailoringmanagement.databinding.FragmentReportBugBinding

class FragmentReportBug : Fragment()
{
    private lateinit var binding: FragmentReportBugBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBugBinding.inflate(layoutInflater)
        return binding.root
    }

}