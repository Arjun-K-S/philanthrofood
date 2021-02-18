package com.xidhu.foodhunt.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import kotlin.collections.ArrayList

class ForgotPassword : AppCompatActivity() {
    lateinit var confirm:Button
    lateinit var phone:EditText
    lateinit var email:EditText
    lateinit var progress_bar:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        confirm = findViewById(R.id.forgot_next)
        phone = findViewById(R.id.forgot_mobile)
        email = findViewById(R.id.forgot_email)
        progress_bar = findViewById(R.id.forgot_prgs)
        confirm.setOnClickListener {
            confirm.setBackgroundResource(R.drawable.btn_clkd)
            if(phone.text.isNotBlank()&&email.text.isNotBlank()&&email.text.toString()
                    .contains('@') && email.text.toString().contains('.')){
                if(Connection().checkConnectivity(this)) {
                    val q = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
                    val jsonobj = JSONObject()
                    jsonobj.put("mobile_number", phone.text.toString())
                    jsonobj.put("email", email.text.toString())
                    try{
                        progress_bar.visibility = View.VISIBLE
                        val jsonreq = object : JsonObjectRequest(Request.Method.POST,url,jsonobj,Response.Listener {
                            if(it.getJSONObject("data").getBoolean("success")){
                                Toast.makeText(this,"OTP sent",Toast.LENGTH_SHORT).show()
                                progress_bar.visibility = View.GONE
                                var intent = Intent(this,Otp::class.java)
                                intent.putExtra("phone",phone.text.toString())
                                intent.putExtra("email",email.text.toString())
                                startActivity(intent)

                            }else{
                                progress_bar.visibility = View.GONE
                                Toast.makeText(this,"Registered mobile number/email is not found.!",Toast.LENGTH_SHORT).show()
                            }

                        },Response.ErrorListener {
                            progress_bar.visibility = View.GONE
                            Toast.makeText(this,"Please Try Again Later.",Toast.LENGTH_SHORT).show()
                        }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c3acf1e14c21f9"
                                return headers
                            }
                        }
                        q.add(jsonreq)
                    }catch (e:Exception){
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this,"Please Try Again Later.",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Please Check your Internet Connection",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Enter the details Correctly..!",Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed(Runnable {
                confirm.setBackgroundResource(R.drawable.btn)
            },200)

        }
        KeyboardVisibilityEvent.setEventListener(this,object: KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if(isOpen){
                    ObjectAnimator.ofFloat(confirm, "translationY", -170f).apply {
                        duration = 300
                        start()
                    }}else{
                    ObjectAnimator.ofFloat(confirm, "translationY", 0f).apply {
                        duration = 300
                        start()}
                }
            }
        }

        )
    }

    override fun onBackPressed() {
        startActivity(Intent(this,Login_Activity::class.java))
        finish()
    }


}