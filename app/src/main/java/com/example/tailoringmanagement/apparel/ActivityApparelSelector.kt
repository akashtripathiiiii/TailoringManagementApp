package com.example.tailoringmanagement.apparel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.tailoringmanagement.activities.ActivityEditCustomerSizes
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.databinding.ActivityApparelSelectorBinding

class ActivityApparelSelector : AppCompatActivity() {
    private lateinit var binding: ActivityApparelSelectorBinding
    private lateinit var apparelArray: ArrayList<Apparel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApparelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Manage Sizes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val imageIds = intArrayOf(
            R.drawable.shalwar, R.drawable.pants, R.drawable.trouser, R.drawable.tunic,
            R.drawable.full_sleeve_shirt, R.drawable.half_sleeve_shirt, R.drawable.coat,
            R.drawable.waistcoat
        )
        val apparelName = arrayOf("Shalwar", "Pants", "Trouser", "Qameez", "Full Sleeve Shirt",
            "Half Sleeve Shirt", "Coat", "Waist Coat")

        apparelArray = ArrayList()

        for (i in apparelName.indices) {
            val apparel = Apparel(apparelName[i], imageIds[i])
            apparelArray.add(apparel)
        }

        val intent = Intent(this, ActivityEditCustomerSizes::class.java)

        binding.apparelListView.isClickable = true
        binding.apparelListView.adapter = ApparelAdapter(this, apparelArray)
        binding.apparelListView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    intent.putExtra("apparel", "Shalwar")
                    startActivity(intent)
                }
                1 -> {
                    intent.putExtra("apparel", "Pant")
                    startActivity(intent)
                }
                2 -> {
                    intent.putExtra("apparel", "Trouser")
                    startActivity(intent)
                }
                3 -> {
                    intent.putExtra("apparel", "Qameez")
                    startActivity(intent)
                }
                4 -> {
                    intent.putExtra("apparel", "Full Sleeve Shirt")
                    startActivity(intent)
                }
                5 -> {
                    intent.putExtra("apparel", "Half Sleeve Shirt")
                    startActivity(intent)
                }
                6 -> {
                    intent.putExtra("apparel", "Coat")
                    startActivity(intent)
                }
                else -> {
                    intent.putExtra("apparel", "Waist Coat")
                    startActivity(intent)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
