package com.example.tailoringmanagement.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.tailoringmanagement.databinding.FragmentSignUpForUserBinding

interface FragmentInteractionListener {
    fun sendDataToActivity(
        email: String,
        password: String,
        confirm: String,
        name: String,
        age: String,
        phone: String,
        userType : String
    )
}

class FragmentSignUpForUser : Fragment() {
    private lateinit var binding: FragmentSignUpForUserBinding
    private var listener: FragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = FragmentSignUpForUserBinding.inflate(layoutInflater)

        return binding.root
    }


    // In TailorFragment.kt

    // In your Fragment
//    fun clearData () {
//        listener?.clearData()
//        binding.editTextTailorName.text.clear()
//        binding.editTextAge.text.clear()
//        binding.editTextTailorEmailAddress.text.clear()
//        binding.editTextTailorPassword.text.clear()
//        binding.editTextConfirmTailorPassword.text.clear()
//        binding.editTextTailorShopName.text.clear()
//    }
    fun sendDataToActivity() {
        listener?.sendDataToActivity(
            binding.signUpEmail.text.toString(),
            binding.signUpPassword.text.toString(),
            binding.signUpConfirmPassword.text.toString(),
            binding.signUpName.text.toString(),
            binding.signUpAge.text.toString(),
            binding.signUpPhone.text.toString(),
            "Tailor"
        )
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the activity implements the FragmentInteractionListener interface
        if (context is FragmentInteractionListener) {
            listener = context
        } else {
            // context is object of Activity that is holding this fragment
            throw RuntimeException("$context must implement FragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // to check about radio buttons
    fun getSelectedRadioButtonId(): Int {
        return binding.radioGroup.checkedRadioButtonId
    }


}