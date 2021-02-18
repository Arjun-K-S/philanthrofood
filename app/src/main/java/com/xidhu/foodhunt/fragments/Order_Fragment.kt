package com.xidhu.foodhunt.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
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
import com.xidhu.foodhunt.adapters.His_Recl_Adapter
import com.xidhu.foodhunt.adapters.Main_Recl_Adapter
import com.xidhu.foodhunt.data.data_list
import com.xidhu.foodhunt.data.food_items
import com.xidhu.foodhunt.data.order_list
import org.json.JSONArray
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap


class Order_Fragment : Fragment() {
    lateinit var no_net:ImageView
    lateinit var progress_bar:RelativeLayout
    lateinit var details: SharedPreferences
    lateinit var recl_view:RecyclerView
 override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_order_, container, false)
        no_net = view.findViewById(R.id.his_frg_no_net)
        recl_view = view.findViewById(R.id.his_recl_view)
        details = this.activity!!.getSharedPreferences("details",Context.MODE_PRIVATE)
     progress_bar = view.findViewById(R.id.his_prgs)
     if(Connection().checkConnectivity(activity as Context)) {
         no_net.visibility = View.GONE
         val q = Volley.newRequestQueue(activity as Context)
         val url = "http://13.235.250.119/v2/orders/fetch_result/"+details.getString("user_id","")
         try{
             progress_bar.visibility = View.VISIBLE
             val jsonreq = object : JsonObjectRequest(
                 Request.Method.GET,url,null,
                 Response.Listener {
                     if(it.getJSONObject("data").getBoolean("success")){
                         val list = arrayListOf<order_list>()
                         val data = it.getJSONObject("data").getJSONArray("data")
                         if(data.length() == 0){
                             val dialog = AlertDialog.Builder(activity as Context,R.style.AlertDialogCustom)
                             dialog.setTitle("No Order History").setMessage("You have No Order History").setPositiveButton("OK",
                                 DialogInterface.OnClickListener { dialogInterface, i ->
                                     startActivity(
                                         Intent(activity as Context,
                                             MainActivity::class.java)
                                     )

                                 }).show()
                         }
                         fun getFoodItems(data:JSONArray):ArrayList<food_items>{
                             var list = arrayListOf<food_items>()
                             for(i in 0 until data.length()){
                                 list.add(
                                     food_items(
                                         data.getJSONObject(i).getString("food_item_id"),
                                         data.getJSONObject(i).getString("name"),
                                         data.getJSONObject(i).getString("cost")

                                     )
                                 )
                             }
                             return list
                         }
                         for(i in 0 until data.length()){
                             list.add(
                                 order_list(
                                     data.getJSONObject(i).getString("order_id"),
                                     data.getJSONObject(i).getString("restaurant_name"),
                                     data.getJSONObject(i).getString("total_cost"),
                                     data.getJSONObject(i).getString("order_placed_at"),
                                     getFoodItems(data.getJSONObject(i).getJSONArray("food_items"))
                                 )
                             )

                         }
                         recl_view.layoutManager = LinearLayoutManager(activity)
                         recl_view.adapter = His_Recl_Adapter(activity as Context,list)
                         progress_bar.visibility = View.GONE
                     }
                 },
                 Response.ErrorListener {
                     progress_bar.visibility = View.GONE
                     Toast.makeText(activity as Context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
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
             Toast.makeText(activity as Context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
         }
     }
     else{

         Toast.makeText(activity as Context,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
         no_net.visibility = View.VISIBLE
     }


        return view
    }



}