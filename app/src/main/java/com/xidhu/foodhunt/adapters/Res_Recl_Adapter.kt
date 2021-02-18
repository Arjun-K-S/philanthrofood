package com.xidhu.foodhunt.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.xidhu.foodhunt.R

import com.xidhu.foodhunt.data.res_item_list
import com.xidhu.foodhunt.database.CartData

import com.xidhu.foodhunt.database.Cart_Items
import java.lang.Exception
import java.util.ArrayList

class Res_Recl_Adapter(val context:Context,val list:ArrayList<res_item_list>,val res_name:String,val btn:Button):RecyclerView.Adapter<Res_Recl_Adapter.View_Holder>() {


    class View_Holder(view: View) : RecyclerView.ViewHolder(view) {
        val count = view.findViewById<TextView>(R.id.res_item_count)
        val name = view.findViewById<TextView>(R.id.res_item_name)
        val price = view.findViewById<TextView>(R.id.res_item_price)
        val add_btn = view.findViewById<Button>(R.id.res_item_add_btn)
        val item = view.findViewById<ConstraintLayout>(R.id.res_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.res_single_item, parent, false)
            btn.setText("No items Selected")
        return View_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.count.text = (position + 1).toString()
        holder.name.text = data.name
        holder.price.text = "₹ " + (data.cost_for_one.toInt() /10).toString()+ "/-"
        val dummy_ett = Cart_Items(data.id.toInt(),"","","","")
        if(CartData(context,dummy_ett,"getid").execute().get() != null){
            holder.add_btn.text = "Remove"
            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
        }
        else{
            holder.add_btn.setBackgroundResource(R.color.colorPrimary)
            holder.add_btn.text = "Add"
        }
        holder.add_btn.setOnClickListener {
            if (holder.add_btn.text == "Add") {
                //remove
                Handler().postDelayed(Runnable {
                    try {
                        val ett = Cart_Items(
                            data.id.toInt(),
                            data.name.toString(),
                            data.cost_for_one.toString(),
                            res_name,data.restaurant_id
                        )
                        val ready = CartData(context, ett, "insert").execute().get()
                        if (ready as Boolean) {
                            if(CartData(context,dummy_ett,"getall").execute().get().toString() != "[]"){
                                btn.setText("Proceed To Payment")
                                btn.setBackgroundResource(R.color.colorPrimary)
                            }else{btn.setBackgroundResource(R.color.grey)
                                btn.setText("No items Selected")}
                            holder.add_btn.setBackgroundResource(R.color.black)
                            holder.add_btn.text = "Remove"
                            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
                        }


                    }catch (e:Exception){ }
                }, 200)

            } else {
                //add
                Handler().postDelayed(Runnable {
                    try {
                        val ett = Cart_Items(
                            data.id.toInt(),
                            data.name.toString(),
                            data.cost_for_one.toString(),
                            res_name,data.restaurant_id
                        )
                        val ready = CartData(context, ett, "delete").execute().get()
                        if (ready as Boolean) {
                            if(CartData(context,dummy_ett,"getall").execute().get().toString() != "[]"){
                                btn.setText("Proceed To Payment")
                                btn.setBackgroundResource(R.color.colorPrimary)
                            }else{btn.setBackgroundResource(R.color.grey)
                            btn.setText("No items Selected")}
                            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
                            holder.add_btn.setBackgroundResource(R.color.colorPrimary)
                            holder.add_btn.text = "Add"
                        }
                    } finally {
                    }
                }, 200)
            }
        }

    }
}
