package com.example.tailoringmanagement.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tailoringmanagement.fragments.FragmentInteractionListener
import com.example.tailoringmanagement.fragments.FragmentSignUpForUser
import com.example.tailoringmanagement.R
import com.example.tailoringmanagement.UserModel
import com.example.tailoringmanagement.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class SIgnUpActivity : AppCompatActivity(), FragmentInteractionListener
{
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebase: FirebaseAuth

    private lateinit var email : String
    private lateinit var password : String
    private lateinit var age : String
    private lateinit var phone : String
    private lateinit var name : String
    private lateinit var confirmPass : String
    private lateinit var userType : String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseAuth.getInstance()

        replaceFragment(FragmentSignUpForUser())

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.textViewSignPage.text = "Sign Up"
        binding.btnSignUpUser.setOnClickListener {

                // In your Activity
            val fragment = supportFragmentManager.findFragmentById(R.id.frameLayoutSignUp) as? FragmentSignUpForUser
            fragment?.sendDataToActivity()
            val id = fragment?.getSelectedRadioButtonId()
            // by default type is tailor
            if(id== R.id.rBCustomer) {
                userType = "Customer"
            }
            val validation = validation()
            if(validation=="Validation successful.") {
                signUp(email, password)
            }
             //   else
            Toast.makeText(this, validation, Toast.LENGTH_SHORT).show()
                // fragment?.clearData()
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutSignUp, fragment).commit()
    }
//    override fun clearData(){
//        // implement any thing
//    }
    override fun sendDataToActivity(
        email: String,
        password: String,
        confirm: String,
        name: String,
        age: String,
        phone: String,
        userType : String
    ) {
       this.email = email
       this.password = password
       this.confirmPass = confirm
       this.name= name
       this.userType = userType
       this.age = age
       this.phone = phone
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_setting -> {
                Toast.makeText(this, "Settings Not Yet Implemented", Toast.LENGTH_SHORT).show()
                return true
            } else -> {
                super.onBackPressed()
                return true
            }
        }
    }
    private fun validation(): String {
        val name = name.trim()
        val email = email.trim()
        val password = password.trim()
        val confirmPass = confirmPass.trim()
        val age = age.trim()
        val phone = phone.trim()

        if (name.isBlank()) {
            return "Name field is blank."
        }

        if (age.isBlank()) {
            return "Age field is blank."
        }

        if (phone.isBlank()) {
            return "Phone field is blank."
        }

        if (email.isBlank()) {
            return "Email field is blank."
        }

        if (password.isBlank()) {
            return "Password field is blank."
        }

        if (confirmPass.isBlank()) {
            return "Confirm Password field is blank."
        }



        if (password != confirmPass) {
            return "Password and Confirm Password do not match."
        }
        if (password.length < 6) {
            return "Password length should be at least 6 characters. Current Length is ${password.length}"
        }
        var hasCapitalLetter = false
        var hasSmallLetter = false
        var hasSpecialCharacter = false
        var hasNumber = false

        for (char in password) {
            when {
                char.isUpperCase() -> hasCapitalLetter = true
                char.isLowerCase() -> hasSmallLetter = true
                char.isDigit() -> hasNumber = true
                char.isLetterOrDigit().not() -> hasSpecialCharacter = true
            }
        }

        if (!(hasCapitalLetter && hasSmallLetter && hasSpecialCharacter && hasNumber)) {
            return "Password must include at least one capital letter, one small letter, one special character, and one number."
        }

        // Add additional validation rules if needed

        // Return a success message if all conditions are met
        return "Validation successful."
    }
    private fun signUp(email: String, password: String) {
        firebase.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Signup successful, you can retrieve the user from `task.result.user`

                    // Save user details to Realtime Database

                    val database = FirebaseDatabase.getInstance().getReference("Users")

                    val key = task.result.user!!.uid

                    val details = UserModel(key,email,password,name,age.toInt(),phone,userType)

                    database.child(key).setValue(details)
                        .addOnSuccessListener {
                            // Tailor details saved successfully

                            Toast.makeText(
                                this,
                                " Successfully Signed Up",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            // Failed to save tailor details
                            Toast.makeText(
                                this,
                                " Unsuccessful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                else {
                    // Signup failed
                    //  val exception = task.exception
                    Toast.makeText(this,"Error Occurred!!",Toast.LENGTH_SHORT).show()
                }
            }
    }
}