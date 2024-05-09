package com.example.tailoringmanagement.localDB
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities



class InternetChecker(context: Context) {
       private val contextValue = context
       fun isInternetAvailable(): Boolean {
        val connectivityManager = contextValue.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}