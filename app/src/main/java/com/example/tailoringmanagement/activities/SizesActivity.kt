package com.example.tailoringmanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tailoringmanagement.databinding.ActivitySizesBinding

class SizesActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySizesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySizesBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}