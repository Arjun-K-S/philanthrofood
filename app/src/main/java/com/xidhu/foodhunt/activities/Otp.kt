package com.xidhu.foodhunt.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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

class Otp : AppCompatActivity() {
    lateinit var change_btn:Button
    lateinit var otp_entered:EditText
    lateinit var new_pass:EditText
    lateinit var new_pass_con:EditText
    lateinit var progress_bar: ConstraintLayout
    lateinit var resnt:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        change_btn = findViewById(R.id.otp_change_password_button)
        otp_entered = findViewById(R.id.otp_otp)
        new_pass = findViewById(R.id.otp_password)
        new_pass_con = findViewById(R.id.otp_confirm_password)
        progress_bar = findViewById(R.id.otp_prgs)
        resnt = findViewById(R.id.otp_resent)
        val timer = object :CountDownTimer(9000,1000){
            override fun onFinish() {
                resnt.setTextColor(resources.getColor(R.color.white))
                resnt.setText("Resent OTP")
            }

            override fun onTick(p0: Long) {
                resnt.setTextColor(resources.getColor(R.color.yellow))
                resnt.setText("Resent OTP in :00:0${p0/1000}")
            }
        }
        timer.start()
        KeyboardVisibilityEvent.setEventListener(this,object: KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if(isOpen){
                    ObjectAnimator.ofFloat(change_btn, "translationY", -170f).apply {
                        duration = 300
                        start()
                    }}else{
                    ObjectAnimator.ofFloat(change_btn, "translationY", 0f).apply {
                        duration = 300
                        start()}
                }
            }
        })
        change_btn.setOnClickListener {
            change_btn.setBackgroundResource(R.drawable.btn_clkd)
            if(new_pass.text.toString()==new_pass_con.text.toString()){
                if(Connection().checkConnectivity(this)) {
                    val q = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
                    val jsonobj = JSONObject()
                    jsonobj.put("mobile_number",intent.getStringExtra("phone"))
                    jsonobj.put("password", new_pass.text.toString())
                    jsonobj.put("otp", otp_entered.text.toString())
                    try{
                        progress_bar.visibility = View.VISIBLE
                        val jsonreq = object : JsonObjectRequest(
                            Request.Method.POST,url,jsonobj,
                            Response.Listener {
                                if(it.getJSONObject("data").getBoolean("success")){
                                    progress_bar.visibility = View.GONE
                                    Toast.makeText(this,"Password Successfully Changed.", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this,Login_Activity::class.java))
                                    finish()

                                }else{
                                    progress_bar.visibility = View.GONE
                                    Toast.makeText(this,"Entered OTP is incorrect or Password is invalid.",
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
                Toast.makeText(this,"Passwords don't match.!", Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed(Runnable {
                change_btn.setBackgroundResource(R.drawable.btn)
            },200)
        }

        resnt.setOnClickListener {
            resnt.setBackgroundResource(R.color.yellow)
            resnt.setTextColor(resources.getColor(R.color.black))
            if(resnt.text.toString()=="Resent OTP"){
                if(Connection().checkConnectivity(this)) {
                    val q = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
                    val jsonobj = JSONObject()
                    jsonobj.put("mobile_number",intent.getStringExtra("phone"))
                    jsonobj.put("email", intent.getStringExtra("email"))
                    try{
                        progress_bar.visibility = View.VISIBLE
                        val jsonreq = object : JsonObjectRequest(
                            Request.Method.POST,url,jsonobj,
                            Response.Listener {
                            if(it.getJSONObject("data").getBoolean("success")){
                                progress_bar.visibility = View.GONE
                                Toast.makeText(this,"OTP sent", Toast.LENGTH_SHORT).show()
                            }else{
                                progress_bar.visibility = View.GONE
                                Toast.makeText(this,"Unknown Error.!",
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
            Handler().postDelayed(Runnable {
                resnt.setBackgroundResource(R.drawable.nothing)
                resnt.setTextColor(resources.getColor(R.color.white))
                timer.start()
            },200)
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this,ForgotPassword::class.java))
        finish()
    }
}