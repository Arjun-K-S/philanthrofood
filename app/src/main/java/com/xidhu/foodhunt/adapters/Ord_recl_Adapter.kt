package com.xidhu.foodhunt.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.util.Connection
import com.google.android.material.appbar.AppBarLayout
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.activities.MainActivity
import com.xidhu.foodhunt.activities.Order_Place

import com.xidhu.foodhunt.data.res_item_list
import com.xidhu.foodhunt.database.CartData

import com.xidhu.foodhunt.database.Cart_Items
import com.xidhu.foodhunt.database.FavData
import com.xidhu.foodhunt.database.Fav_res
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap

class Ord_Recl_Adapter(val context:Context,val btn:Button,val res_name:TextView,val progress_bar:RelativeLayout,val order_placed:ConstraintLayout,val appbar:AppBarLayout,val user_id:String):RecyclerView.Adapter<Ord_Recl_Adapter.View_Holder>() {
    var total = 0
    val jsonArr = JSONArray()
    var list:List<Cart_Items> = CartData(context,Cart_Items(0,"","","",""),"getall").execute().get() as List<Cart_Items>
    class View_Holder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.ord_item_item_name)
        val price = view.findViewById<TextView>(R.id.ord_item_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ord_single_item, parent, false)
        res_name.setText("Ordering From "+list[0].itemRes)
        return View_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.itemName
        holder.price.text = "₹ " + (data.itemPrice.toInt()/10).toString() + "/-"
        total+=(data.itemPrice.toInt()/10)
        btn.setText("Place Order ( Total : "+total+")")
        val dataOBj = JSONObject()
        dataOBj.put("food_item_id",data.item_id)
        jsonArr.put(dataOBj)
        btn.setOnClickListener {
            btn.setBackgroundResource(R.color.grey)
            Handler().postDelayed(Runnable {
                if(Connection().checkConnectivity(context)) {
                    val q = Volley.newRequestQueue(context)
                    val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                    try{
                        progress_bar.visibility = View.VISIBLE
                        val jsonObj = JSONObject()
                        jsonObj.put("user_id",user_id)
                        jsonObj.put("restaurant_id",data.itemResId)
                        jsonObj.put("total_cost",(total.toInt()/10).toString())
                        jsonObj.put("food",jsonArr)
                        val jsonreq = object : JsonObjectRequest(
                            Request.Method.POST,url,jsonObj,
                            Response.Listener {
                                if(it.getJSONObject("data").getBoolean("success")){
                                    appbar.visibility = View.GONE
                                    order_placed.visibility = View.VISIBLE
                                    order_placed.findViewById<Button>(R.id.ord_ok).setOnClickListener {
                                        order_placed.findViewById<Button>(R.id.ord_ok).setBackgroundResource(R.drawable.btn_clkd)
                                        Handler().postDelayed(Runnable {
                                            if(CartData(context,Cart_Items(0,"","","",""),"deleteall").execute().get() as Boolean){
                                                order_placed.visibility = View.GONE
                                                appbar.visibility = View.VISIBLE
                                                order_placed.findViewById<Button>(R.id.ord_ok).setBackgroundResource(R.drawable.btn)
                                                context.startActivity(Intent(context,MainActivity::class.java))
                                            }
                                        },200)
                                    }
                                }
                            },
                            Response.ErrorListener {
                                progress_bar.visibility = View.GONE
                                context.startActivity(Intent(context,Order_Place::class.java))
                                Toast.makeText(context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                            }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "c3acf1e14c21f9"
                                return headers
                            }
                        }
                        q.add(jsonreq)
                    }catch (e: Exception){
                        progress_bar.visibility = View.GONE
                        context.startActivity(Intent(context,Order_Place::class.java))
                        Toast.makeText(context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(context,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context,Order_Place::class.java))
                }
                btn.setBackgroundResource(R.color.colorPrimary)
            }
                ,200)

        }
    }
}
