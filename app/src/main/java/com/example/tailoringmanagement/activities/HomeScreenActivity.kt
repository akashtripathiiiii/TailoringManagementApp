package com.example.tailoringmanagement.activities
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.tailoringmanagement.fragments.FragmentProfile
import com.example.tailoringmanagement.fragments.FragmentReportBug
import com.example.tailoringmanagement.fragments.FragmentSettings
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.customerPageForTailors.FragmentRVCustomerRecord
import com.example.tailoringmanagement.databinding.ActivityHomeScreenBinding
import com.example.tailoringmanagement.employeePageForTailors.FragmentRVEmployeeRecord
import com.example.tailoringmanagement.fragments.FragmentChatting
import com.example.tailoringmanagement.orderPageForTailors.FragmentRVOrderRecord

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private var currentFragment: String = "Customers"
    private var name : String? = null
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.drawerToolBar.toolBar)
        binding.drawerToolBar.toolBar.title = "Customers"
        binding.drawerToolBar.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            binding.drawerToolBar.toolBar, R.string.drawer_open, R.string.drawer_close
        )

        toggle.syncState()
        binding.drawerLayout.addDrawerListener(toggle)
        name = intent.getStringExtra("name")
        email= intent.getStringExtra("email")
//        Toast.makeText(this,"$name",Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"$email",Toast.LENGTH_SHORT).show()
        val headerview = binding.drawerNavView.getHeaderView(0)
        val nameTV = headerview.findViewById<TextView>(R.id.drawerHeaderUserName)
        val emailTV = headerview.findViewById<TextView>(R.id.drawerHeaderEmail)
        nameTV.text = name
        emailTV.text = email


        binding.drawerNavView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.drawerItemCustomers -> {
                    launchFragment("Customers", FragmentRVCustomerRecord())
                    Toast.makeText(this, "Customers", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemOrders -> {
                    launchFragment("Orders", FragmentRVOrderRecord())
                    Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemEmployees -> {
                    launchFragment("Employees", FragmentRVEmployeeRecord())
                    Toast.makeText(this, "Employees", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemChatting -> {
                    launchFragment("Chatting Users", FragmentChatting())
                    Toast.makeText(this, "Chatting", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemProfile -> {
                    launchFragment("Profile", FragmentProfile())
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemReportBug -> {
                    launchFragment("Report a Bug", FragmentReportBug())
                    Toast.makeText(this, "Report a Bug", Toast.LENGTH_SHORT).show()
                }
                R.id.drawerItemLogout -> {
                    Toast.makeText(this, "Logout user : $name", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("userType", null)
                    editor.apply()
                    startActivity(Intent(this, StartUpActivity::class.java))
                    finish()
                }
                else -> {
                    launchFragment("Settings", FragmentSettings())
                    Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        if (savedInstanceState != null)
        {
              // to do nothing
        } else {
            launchFragment(currentFragment, FragmentRVCustomerRecord())
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            onBackPressedDispatcher.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

        if (intent.getBooleanExtra("restartFragmentOrder", false)) {
            // Recreate the fragment here
            Log.i("OnResume","Called")
            launchFragment("Orders", FragmentRVOrderRecord())
            intent.removeExtra("restartFragmentOrder")
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fragment", currentFragment)

    }

    private fun launchFragment(toolBarTitle: String, fragment: Fragment) {
        binding.drawerToolBar.toolBar.title = toolBarTitle
        currentFragment = toolBarTitle
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment, "currentFragment").commit()
    }

}