package com.xidhu.foodhunt.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.adapters.Ord_Recl_Adapter
import com.xidhu.foodhunt.database.CartData
import com.xidhu.foodhunt.database.Cart_Items

class Order_Place : AppCompatActivity() {
    lateinit var recl_view: RecyclerView
    lateinit var place_order_button:Button
    lateinit var ord_res_name:TextView
    lateinit var prgs:RelativeLayout
    lateinit var order_placed:ConstraintLayout
    lateinit var appbar:AppBarLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order__place)
        val details = getSharedPreferences("details", Context.MODE_PRIVATE)
        recl_view = findViewById(R.id.ord_recl_view)
        toolbar = findViewById(R.id.ord_toolbar)
        ord_res_name = findViewById(R.id.ord_res_name)
        prgs = findViewById(R.id.ord_prgs)
        appbar = findViewById(R.id.ord_appBarLayout)
        order_placed = findViewById(R.id.ord_placed)
        place_order_button = findViewById(R.id.ord_place_order)
        setUpToolbar()
        recl_view.layoutManager = LinearLayoutManager(this)
        recl_view.adapter = Ord_Recl_Adapter(this,place_order_button,ord_res_name,prgs,order_placed,appbar,details.getString("user_id","") as String)
    }

    override fun onBackPressed() {
        if (CartData(this, Cart_Items(0, "", "", "", ""), "deleteall").execute().get() as Boolean
            ) {
            order_placed.visibility = View.GONE
            finish()
            startActivity(Intent(this, MainActivity::class.java))

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            CartData(this,Cart_Items(0,"","","",""),"deleteall").execute().get()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_bk_btn)
    }
}

