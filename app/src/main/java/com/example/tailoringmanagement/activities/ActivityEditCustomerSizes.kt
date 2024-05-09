package com.example.tailoringmanagement.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.databinding.ActivityEditCustomerSizesBinding
import com.example.tailoringmanagement.lowerbody.lowerbodyfragments.FragmentPant
import com.example.tailoringmanagement.lowerbody.lowerbodyfragments.FragmentShalwar
import com.example.tailoringmanagement.lowerbody.lowerbodyfragments.FragmentTrouser
import com.example.tailoringmanagement.upperbody.upperbodyfragments.*

class ActivityEditCustomerSizes : AppCompatActivity()
{
    private lateinit var binding: ActivityEditCustomerSizesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerSizesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        when (intent.getStringExtra("apparel")) {
            "Shalwar" -> replaceFragment("Shalwar Sizes", FragmentShalwar())
            "Pant" -> replaceFragment("Pant Sizes", FragmentPant())
            "Trouser" -> replaceFragment("Trouser Sizes", FragmentTrouser())
            "Qameez" -> replaceFragment("Qameez Sizes", FragmentLongTunic())
            "Full Sleeve Shirt" -> replaceFragment("Full Sleeve Shirt Sizes", FragmentFullSleeveShirt())
            "Half Sleeve Shirt" -> replaceFragment("Half Sleeve Shirt Sizes", FragmentHalfSleeveShirt())
            "Coat" -> replaceFragment("Coat Sizes", FragmentCoat())
            "Waist Coat" ->replaceFragment("Waist Coat", FragmentWaistCoat())
        }

        binding.btnSaveSizeRecord.setOnClickListener {
            Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }
        binding.btnDeleteSizeRecord.setOnClickListener {
            showDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            } else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun replaceFragment(title: String, fragment: Fragment)
    {
        supportActionBar!!.title = title
        supportFragmentManager.beginTransaction().replace(R.id.frameApparelViewer, fragment).commit()
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Sure To Delete Record")
            .setTitle("Warning!")
            .setPositiveButton("Sure") { _, _ ->
                Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        val dialog = builder.create()
        dialog.show()
    }

}