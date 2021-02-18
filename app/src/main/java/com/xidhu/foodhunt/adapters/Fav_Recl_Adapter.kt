package com.xidhu.foodhunt.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.activities.Details
import com.xidhu.foodhunt.activities.MainActivity
import com.xidhu.foodhunt.data.food_items
import com.xidhu.foodhunt.data.order_list
import com.xidhu.foodhunt.database.CartData
import com.xidhu.foodhunt.database.Cart_Items
import com.xidhu.foodhunt.database.FavData
import com.xidhu.foodhunt.database.Fav_res
import com.xidhu.foodhunt.fragments.Fav_Fragment
import java.util.ArrayList
import java.util.logging.Handler

class Fav_Recl_Adapter(val context: Context):RecyclerView.Adapter<Fav_Recl_Adapter.View_Holder>() {
    var list = (FavData(context, Fav_res("","",""),"getall").execute().get() as List<Fav_res>).toMutableList()

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.fav_res_name)
        val img = view.findViewById<ImageView>(R.id.fav_img)
        val btn = view.findViewById<Button>(R.id.fav_btn)
        val item = view.findViewById<ConstraintLayout>(R.id.fav_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_single_item, parent, false)
        return View_Holder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        var data = list[position]
        holder.name.text = data.res_name
        Picasso.get().load(data.img).error(R.drawable.icon_main).into(holder.img)
        holder.btn.setBackgroundResource(R.drawable.floating_fav_clkd)
        holder.item.setOnClickListener {
            val i =Intent(context, Details::class.java)
            i.putExtra("id",data.id)
            i.putExtra("name",data.res_name)
            i.putExtra("url",data.img)
            android.os.Handler().postDelayed(Runnable {
                CartData(context, Cart_Items(0,"","","",""),"deleteall").execute().get()
                context.startActivity(i)
            },200)
        }
        holder.btn.setOnClickListener {
            if(FavData(context,Fav_res(data.res_name,data.img,data.id),"delete").execute().get() as Boolean){
                android.os.Handler().postDelayed(Runnable {
                    holder.btn.setBackgroundResource(R.drawable.floating_fav)
                    list.removeAt(position)
                    this.notifyItemRemoved(position)
                    android.os.Handler().postDelayed(Runnable { this.notifyDataSetChanged()
                    if(list.isEmpty()){
                        context.startActivity(Intent(context,MainActivity::class.java))
                    }},500)
                },200)
            }
        }
    }
}