package com.xidhu.foodhunt.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.database.FavData
import com.xidhu.foodhunt.database.Fav_res
import com.xidhu.foodhunt.fragments.Fav_Fragment
import com.xidhu.foodhunt.fragments.Main_Fragment
import com.xidhu.foodhunt.fragments.Order_Fragment
import com.xidhu.foodhunt.fragments.User
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var toolbar:Toolbar
    lateinit var details: SharedPreferences
    lateinit var navigation_drawer:NavigationView
    lateinit var profile_img:ImageView
    lateinit var prof_change_btn:Button
    lateinit var frame_layout:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frame_layout =findViewById(R.id.main_frame_layout)
        drawerLayout = findViewById(R.id.main_drawer_layout)
        toolbar = findViewById(R.id.main_toolbar_layout)
        navigation_drawer = findViewById(R.id.main_navigation_view)
        prof_change_btn = navigation_drawer.getHeaderView(0).findViewById(R.id.menu_header_change_pic)
        details = getSharedPreferences("details", Context.MODE_PRIVATE)
        profile_img = navigation_drawer.getHeaderView(0).findViewById(R.id.menu_header_img)
        setUpToolBar()
        val drawerAction = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(drawerAction)
        drawerAction.syncState()
        navigation_drawer.getHeaderView(0).findViewById<TextView>(R.id.menu_header_name).setText(details.getString("name",""))
        navigation_drawer.getHeaderView(0).findViewById<TextView>(R.id.menu_header_phone).setText(details.getString("mobile_number",""))
        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,Main_Fragment()).commit()
        navigation_drawer.setCheckedItem(R.id.home_select)
        var prev_item = navigation_drawer.checkedItem
        navigation_drawer.setNavigationItemSelectedListener {
            if(prev_item?.itemId != it.itemId){
                    prev_item?.setChecked(false)
            }
            prev_item = it
            when(it.itemId){
                R.id.home_logout -> {
                    val dialog = AlertDialog.Builder(this,R.style.AlertDialogCustom)
                    dialog.setTitle("Log Out").setMessage("Are you sure you want to Log Out?").setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        if(FavData(this, Fav_res("","",""),"deleteall").execute().get() as Boolean) {
                            details.edit().clear().apply()
                            finish()
                            startActivity(Intent(this, Login_Activity::class.java))
                        }
                    }).setNegativeButton("Cancel",DialogInterface.OnClickListener { dialogInterface, i ->  }).
                        setIcon(R.drawable.icon_main).show()
                    drawerLayout.closeDrawers()
                }
                R.id.home_select ->{
                    supportActionBar?.title = "All Restaurants"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,Main_Fragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_history->{
                    supportActionBar?.title = "Order History"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,Order_Fragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_fav->{
                    supportActionBar?.title = "Favourite Restaurants"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,Fav_Fragment()).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_profile->{
                    supportActionBar?.title = "Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,User()).commit()
                    drawerLayout.closeDrawers()
                }

            }
                return@setNavigationItemSelectedListener true
        }
        prof_change_btn.setOnClickListener{
            prof_change_btn.setText("Change Image")
            Handler().postDelayed(Runnable {
                val dialog = AlertDialog.Builder(this,R.style.AlertDialogCustom)
                dialog.setTitle("Set Profile Picture").setMessage("Set Image from Gallary?").setPositiveButton("Open",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        var i = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")
                        i=Intent.createChooser(i,"file")
                        startActivityForResult(i,111)
                    }).setNegativeButton("Cancel",DialogInterface.OnClickListener { dialogInterface, i ->  }).
                setIcon(R.drawable.icon_main).show()
                prof_change_btn.setText("")
            },200)


        }
        }




    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK){
            val url = data?.data
            profile_img.setImageURI(url)
            details.edit().putString("profile_pic",url.toString()).apply()
        }
    }

    override fun onBackPressed() {
        if(supportActionBar?.title == "All Restaurants"){
            finishAffinity()
        }
        else{
            supportActionBar?.title = "All Restaurants"
            supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,Main_Fragment()).commit()
        }
    }

}


