package com.xidhu.foodhunt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.data.order_list
import java.util.ArrayList

class His_Recl_Adapter(val context: Context,val list:ArrayList<order_list>):RecyclerView.Adapter<His_Recl_Adapter.View_Holder>() {

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val res_name = view.findViewById<TextView>(R.id.his_item_res_name)
        val date = view.findViewById<TextView>(R.id.his_item_date)
        val recl_view = view.findViewById<RecyclerView>(R.id.his_recl_recl_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return View_Holder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.res_name.text = data.res_name
        holder.date.text = data.order_placed_at.substringBefore(" ")
        holder.recl_view.layoutManager = LinearLayoutManager(context)
        holder.recl_view.adapter = His_Recl_Recl_Adapter(context,data.food_items)
    }
}