package com.xidhu.foodhunt.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.activities.Details
import com.xidhu.foodhunt.data.data_list
import com.xidhu.foodhunt.database.CartData
import com.xidhu.foodhunt.database.Cart_Items
import com.xidhu.foodhunt.database.FavData
import com.xidhu.foodhunt.database.Fav_res
import org.json.JSONObject
import java.util.ArrayList

class Main_Recl_Adapter(val context:Context,val list:ArrayList<data_list>):RecyclerView.Adapter<Main_Recl_Adapter.View_Holder>(){

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val img = view.findViewById<ImageView>(R.id.item_img)
        val name = view.findViewById<TextView>(R.id.item_name)
        val price = view.findViewById<TextView>(R.id.item_price)
        val rating = view.findViewById<TextView>(R.id.item_rating)
        val fav = view.findViewById<ImageView>(R.id.item_fav_icon)
        val item = view.findViewById<ConstraintLayout>(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_single_item,parent,false)
        return View_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.name
        holder.price.text = "Available"
        holder.rating.text = data.rating+"★"
        Picasso.get().load(data.image_url).error(R.drawable.icon_main).into(holder.img)
        val ett = Fav_res(data.name,data.image_url,data.id)
        if(FavData(context,ett,"getid").execute().get()!=null){
            holder.fav.tooltipText = "Remove From Favourites"
            holder.fav.setImageResource(R.drawable.floating_fav_clkd)
        }else{
            holder.fav.tooltipText = "Add to Favourites"
            holder.fav.setImageResource(R.drawable.floating_fav)
        }
        holder.item.setOnClickListener {
            startAnimation(holder.item)
            val i =Intent(context,Details::class.java)
            i.putExtra("id",data.id)
            i.putExtra("name",data.name)
            i.putExtra("url",data.image_url)
            Handler().postDelayed(Runnable {
                CartData(context, Cart_Items(0,"","","",""),"deleteall").execute().get()
                context.startActivity(i)
            },200)
        }
        holder.fav.setOnClickListener {
            if(holder.fav.tooltipText == "Add to Favourites"){
                if(FavData(context,ett,"insert").execute().get() as Boolean) {
                    holder.fav.tooltipText = "Remove From Favourites"
                    holder.fav.setImageResource(R.drawable.floating_fav_clkd)

                }
            }
            else{
                if(FavData(context,ett,"delete").execute().get() as Boolean) {
                holder.fav.tooltipText = "Add to Favourites"
                holder.fav.setImageResource(R.drawable.floating_fav)
            }
        }
    } }
    fun startAnimation(item:ConstraintLayout){
        item.setBackgroundResource(R.drawable.item_main0)
        Handler().postDelayed(Runnable {
            item.setBackgroundResource(R.drawable.item_main1)
        },40)
        Handler().postDelayed(Runnable {
            item.setBackgroundResource(R.drawable.item_main2)
        },80)
        Handler().postDelayed(Runnable {
            item.setBackgroundResource(R.drawable.item_main3)
        },120)
        Handler().postDelayed(Runnable {
            item.setBackgroundResource(R.drawable.item_main_clkd)
        },160)
        Handler().postDelayed(Runnable {
            item.setBackgroundResource(R.drawable.item_main)
        },180)
    }
}
