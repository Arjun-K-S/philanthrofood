package com.xidhu.foodhunt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.xidhu.foodhunt.activities.Login_Activity
import com.xidhu.foodhunt.activities.MainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var details: SharedPreferences
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        details = getSharedPreferences("details", Context.MODE_PRIVATE)
        if(details.getBoolean("loggined",false)){
            Handler().postDelayed(Runnable { startActivity(Intent(this,
                MainActivity::class.java)) },1000)
        }
        else{
            Handler().postDelayed(Runnable { startActivity(Intent(this,
                Login_Activity::class.java)) },1000)
        }

    }
}
