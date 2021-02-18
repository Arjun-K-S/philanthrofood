package com.example.myapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connection {
    fun checkConnectivity(context:Context):Boolean{
        val connection = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNet:NetworkInfo? = connection.activeNetworkInfo
        if(activeNet?.isConnected != null) {
            return true
        }
        else{
            return false
    }
}
}