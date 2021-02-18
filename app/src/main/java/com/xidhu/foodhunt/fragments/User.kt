package com.xidhu.foodhunt.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.xidhu.foodhunt.R

class User : Fragment() {
    lateinit var img:ImageView
    lateinit var details:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        details = activity?.getSharedPreferences("details", Context.MODE_PRIVATE) as SharedPreferences
        val view= inflater.inflate(R.layout.fragment_user, container, false)
        img = view.findViewById(R.id.usr_img)
        var img_btn = view.findViewById<Button>(R.id.usr_img_btn)
        var name = view.findViewById<TextView>(R.id.usr_name).setText(details?.getString("name",""))
        var phone = view.findViewById<TextView>(R.id.usr_phone).setText(details?.getString("mobile_number",""))
        var id = view.findViewById<TextView>(R.id.usr_id).setText("id : "+details?.getString("user_id",""))
        var email = view.findViewById<TextView>(R.id.usr_email).setText(details?.getString("email",""))
        var address = view.findViewById<TextView>(R.id.usr_address).setText(details?.getString("address",""))
        img_btn.setOnClickListener{
            img_btn.setText("Change Image")
            Handler().postDelayed(Runnable {
                val dialog = AlertDialog.Builder(context,R.style.AlertDialogCustom)
                dialog.setTitle("Set Profile Picture").setMessage("Set Image from Gallary?").setPositiveButton("Open",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        var i = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
                        i= Intent.createChooser(i,"file")
                        startActivityForResult(i,111)
                    }).setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialogInterface, i ->  }).
                setIcon(R.drawable.icon_main).show()
                img_btn.setText("")
            },200)}
        return  view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK){
            val url = data?.data
            img.setImageURI(url)
            details.edit().putString("profile_pic",url.toString()).apply()
        }
    }

    }