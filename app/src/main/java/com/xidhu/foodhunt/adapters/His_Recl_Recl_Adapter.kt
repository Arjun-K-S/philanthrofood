package com.xidhu.foodhunt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.data.food_items
import com.xidhu.foodhunt.data.order_list
import java.util.ArrayList

class His_Recl_Recl_Adapter(val context: Context,val list:ArrayList<food_items>):RecyclerView.Adapter<His_Recl_Recl_Adapter.View_Holder>() {

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.ord_item_item_name)
        val price = view.findViewById<TextView>(R.id.ord_item_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ord_single_item, parent, false)
        return View_Holder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.food_name
        holder.price.text = (data.price.toInt()/10).toString()

    }
}