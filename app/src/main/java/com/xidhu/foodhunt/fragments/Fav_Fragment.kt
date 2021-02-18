package com.xidhu.foodhunt.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.activities.Login_Activity
import com.xidhu.foodhunt.activities.MainActivity
import com.xidhu.foodhunt.adapters.Fav_Recl_Adapter
import com.xidhu.foodhunt.database.CartData
import com.xidhu.foodhunt.database.FavData
import com.xidhu.foodhunt.database.Fav_res

class Fav_Fragment : Fragment() {
    lateinit var recl_view:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_fav_, container, false)
        if(FavData(activity as Context, Fav_res("","",""),"getall").execute().get().toString() != "[]") {
            recl_view = view.findViewById(R.id.fav_recl)
            recl_view.layoutManager = GridLayoutManager(context, 2)
            recl_view.adapter = Fav_Recl_Adapter(activity as Context)
        }
        else{
            val dialog = AlertDialog.Builder(activity as Context,R.style.AlertDialogCustom)
            dialog.setTitle("No Favourites").setMessage("You have No Favourite Restaurants").setPositiveButton("OK",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivity(Intent(activity as Context,MainActivity::class.java))

                }).show()
        }
        return view
    }
}