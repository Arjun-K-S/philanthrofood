package com.xidhu.foodhunt.activities

import android.animation.ObjectAnimator
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.util.Connection
import com.xidhu.foodhunt.R
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class Login_Activity : AppCompatActivity() {

    lateinit var login_button: Button
    lateinit var sign_up: TextView
    lateinit var forgot: TextView
    lateinit var password: TextView
    lateinit var mobile: TextView
    lateinit var progress_bar:ConstraintLayout
    lateinit var details:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)
        details = getSharedPreferences("details", Context.MODE_PRIVATE)
        details.edit().putBoolean("loggined",false).apply()
        mobile = findViewById(R.id.login_mobile_no)
        password = findViewById(R.id.login_password)
        login_button = findViewById(R.id.login_btn)
        sign_up = findViewById(R.id.login_sign_up)
        progress_bar = findViewById(R.id.login_prgs)
        sign_up.setOnClickListener {
            sign_up.setBackgroundResource(R.color.yellow)
            sign_up.setTextColor(resources.getColor(R.color.black))
            Handler().postDelayed(Runnable {
                sign_up.setBackgroundResource(R.drawable.nothing)
                sign_up.setTextColor(resources.getColor(R.color.white))
                startActivity(Intent(this, Registration::class.java))
            },200)

        }
        forgot = findViewById(R.id.login_forgot)
        forgot.setOnClickListener {
            forgot.setBackgroundResource(R.color.yellow)
            forgot.setTextColor(resources.getColor(R.color.black))
            Handler().postDelayed(Runnable {
                forgot.setBackgroundResource(R.drawable.nothing)
                forgot.setTextColor(resources.getColor(R.color.white))
                startActivity(Intent(this, ForgotPassword::class.java))
            },200)

        }
        KeyboardVisibilityEvent.setEventListener(this,object : KeyboardVisibilityEventListener{
            override fun onVisibilityChanged(isOpen: Boolean) {
                if(isOpen){
                    ObjectAnimator.ofFloat(login_button, "translationY", -130f).apply {
                        duration = 300
                        start()
                    }}else {
                    ObjectAnimator.ofFloat(login_button, "translationY", -10f).apply {
                        duration = 300
                        start()
                    }
                }

            }
        })
        login_button.setOnClickListener {
            login_button.setBackgroundResource(R.drawable.btn_clkd)
            if(mobile.text.isNotBlank()&&password.text.isNotBlank()){
                if(Connection().checkConnectivity(this)) {
                    val q = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/login/fetch_result/"
                    val jsonobj = JSONObject()
                    jsonobj.put("mobile_number", mobile.text.toString())
                    jsonobj.put("password", password.text.toString())
                    try{
                        progress_bar.visibility = View.VISIBLE
                        val jsonreq = object : JsonObjectRequest(
                            Request.Method.POST,url,jsonobj,
                            Response.Listener {
                            if(it.getJSONObject("data").getBoolean("success")){
                                Toast.makeText(this,"Logged in", Toast.LENGTH_SHORT).show()
                                progress_bar.visibility = View.GONE
                                val fetched_data = it.getJSONObject("data").getJSONObject("data")
                                details.edit().putBoolean("loggined",true).putString("user_id",fetched_data.getString("user_id"))
                                    .putString("name",fetched_data.getString("name"))
                                    .putString("email",fetched_data.getString("email"))
                                    .putString("mobile_number",fetched_data.getString("mobile_number"))
                                    .putString("address",fetched_data.getString("address")).apply()
                                startActivity(Intent(this,MainActivity::class.java))

                            }else{
                                progress_bar.visibility = View.GONE
                                Toast.makeText(this,"Mobile number or Password is incorrect.!",
                                    Toast.LENGTH_SHORT).show()
                            }

                        },
                            Response.ErrorListener {
                            progress_bar.visibility = View.GONE
                            Toast.makeText(this,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                        }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c3acf1e14c21f9"
                                return headers
                            }
                        }
                        q.add(jsonreq)
                    }catch (e: Exception){
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Enter the details Correctly..!", Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed(Runnable {
                login_button.setBackgroundResource(R.drawable.btn)
            },200)

        }
    }

    override fun onBackPressed() {
            finishAffinity()
        }

    }