package com.example.tailoringmanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.tailoringmanagement.databinding.ActivityStartUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.content.SharedPreferences
import com.example.tailoringmanagement.R


class StartUpActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityStartUpBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userName : String = ""
    private var userEmail : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("Login",Context.MODE_PRIVATE)
        // Login is file name
        val checkUserLoggedIn = sharedPreferences.getString("userType",null)

        if (checkUserLoggedIn != null) {
            userName = sharedPreferences.getString("name",null).toString()
            userEmail = sharedPreferences.getString("email",null).toString()
            // User is already logged in, navigate to the respective home screen
            if (checkUserLoggedIn == "Tailor") {
                moveToHome()
           }
//            else if (checkUserLoggedIn== "Customer") {
//                Toast.makeText(this,"Customer Logged In",Toast.LENGTH_SHORT).show()
//            }
            finish() // Finish the current activity to prevent going back to the login screen
        }
        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.btnLoginUser.setOnClickListener {
            val validation = validateCredentials()
            if(validation=="Validations Successful")
              loginUser()
           else
                Toast.makeText(this, validation, Toast.LENGTH_SHORT).show()
        }
        binding.textViewRecoverAccount.setOnClickListener {
            Toast.makeText(this, "Recovery Not Yet Implemented", Toast.LENGTH_SHORT).show()
        }
        binding.textViewUseAsGuest.setOnClickListener {
            userName ="guest"
            userEmail = "guest@gmail.com"
            moveToHome()
        }
    }
    private fun moveToHome(){
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.putExtra("name",userName)
        intent.putExtra("email",userEmail)
        startActivity(intent)
    }

    private fun signUpUser()
    {
        startActivity(Intent(this, SIgnUpActivity::class.java))
    }
    private fun validateCredentials() : String {
        val email = binding.loginUser.text.toString()
        val password = binding.loginPassword.text.toString()
        if (email.isBlank() && password.isBlank()) {
            return "Please enter your email and password."
        }

        if (email.isBlank()) {
            return "Please enter your email."
        }

        if (password.isBlank()) {
            return "Please enter your password."
        }

        if (password.length < 6) {
            return "Password is too short. Please enter 6 or more characters."
        }

        // All validations passed, return empty string to indicate success
        return "Validations Successful"
    }

    private fun loginUser(){
        val email = binding.loginUser.text.toString()
        val password = binding.loginPassword.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
             val database = FirebaseDatabase.getInstance().reference.child("Users")
             val key = task.result.user!!.uid
             database.child(key).addListenerForSingleValueEvent(object : ValueEventListener{
                 // functions to retrieve data
             override fun onDataChange(snapshot: DataSnapshot) {
                       val userType = snapshot.child("userType").value.toString()
                       val name = snapshot.child("name").value.toString()
                     Toast.makeText(this@StartUpActivity,name,Toast.LENGTH_SHORT).show()
                       val editor: SharedPreferences.Editor = sharedPreferences.edit()
                     editor.putString("userType", userType)
                     editor.putString("email", email)
                     editor.putString("name",name)
                     editor.apply()

                        if(userType=="Tailor"){
                            userName = name
                            userEmail = email
                            Toast.makeText(this@StartUpActivity,"Tailor Logged In",Toast.LENGTH_SHORT).show()
                            moveToHome()
                        }
                        else {
                            Toast.makeText(this@StartUpActivity,"Customer Logged In",Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    }

             override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@StartUpActivity,"Got error :$error",Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                when (task.exception) {
                   is FirebaseAuthInvalidUserException -> {
                        // User does not exist or email is incorrect
                        Toast.makeText(this@StartUpActivity, "Invalid email", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Incorrect password
                        Toast.makeText(this@StartUpActivity, "Invalid password", Toast.LENGTH_SHORT)
                            .show()
                    } else -> {
                        // Other login errors
                        Toast.makeText(
                            this@StartUpActivity,
                            "Login denied. Invalid credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                //Toast.makeText(this@StartUpActivity,"Login denied. Invalid Credentials",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_setting -> {
                Toast.makeText(this, "Settings Not Yet Implemented", Toast.LENGTH_SHORT).show()
                true
            } else -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
    }
}