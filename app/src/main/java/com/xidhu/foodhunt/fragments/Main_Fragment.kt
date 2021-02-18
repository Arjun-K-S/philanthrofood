package com.xidhu.foodhunt.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.util.Connection
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.xidhu.foodhunt.R
import com.xidhu.foodhunt.activities.MainActivity
import com.xidhu.foodhunt.adapters.Main_Recl_Adapter
import com.xidhu.foodhunt.data.data_list
import java.lang.Exception
import java.util.*
import kotlin.Comparator


class Main_Fragment : Fragment() {
    lateinit var recl_view:RecyclerView
    lateinit var progress_bar:RelativeLayout
    lateinit var list:ArrayList<data_list>
    lateinit var no_net:ImageView
    lateinit var refresh:SwipeRefreshLayout
    lateinit var search_bar:EditText
    lateinit var appbar:AppBarLayout
    lateinit var nav_view:NavigationView
    var asc = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_, container, false)
        setHasOptionsMenu(true)
        search_bar = view.findViewById(R.id.main_search_bar)
        refresh = view.findViewById(R.id.main_frg_swipe_refresh)
        search_bar.setHint("Search Restaurants")
        no_net = view.findViewById(R.id.main_frg_no_net)
        appbar = view.findViewById(R.id.main_frg_appbar)
        progress_bar = view.findViewById(R.id.frg_main_prgs)
        nav_view = activity!!.findViewById(R.id.main_navigation_view) as NavigationView
        nav_view.setCheckedItem(R.id.home_select)
        if(Connection().checkConnectivity(activity as Context)) {
            no_net.visibility = View.GONE
            appbar.visibility = View.VISIBLE
            val q = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            try{
                progress_bar.visibility = View.VISIBLE
                val jsonreq = object : JsonObjectRequest(
                    Request.Method.GET,url,null,
                    Response.Listener {
                        if(it.getJSONObject("data").getBoolean("success")){
                            list = arrayListOf<data_list>()
                            val data = it.getJSONObject("data").getJSONArray("data")
                            for(i in 0 until data.length()){
                                    list.add(
                                        data_list(
                                            data.getJSONObject(i).getString("id"),
                                            data.getJSONObject(i).getString("name"),
                                            data.getJSONObject(i).getString("rating"),
                                            "Free",
                                            data.getJSONObject(i).getString("image_url")
                                        )
                                    )
                            }
                            recl_view = view.findViewById(R.id.frg_main_rec_view)
                            recl_view.layoutManager = LinearLayoutManager(activity)
                            recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                            progress_bar.visibility = View.GONE
                        }
                    },
                    Response.ErrorListener {
                        appbar.visibility = View.GONE
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
                appbar.visibility = View.GONE
                progress_bar.visibility = View.GONE
                Toast.makeText(activity as Context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            appbar.visibility = View.GONE
            Toast.makeText(activity as Context,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
            no_net.visibility = View.VISIBLE
        }
        search_bar.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    val list_search = arrayListOf<data_list>()
                    for (i in 0 until list.size) {
                        if (list[i].name.toLowerCase()
                                .contains(search_bar.text.toString().toLowerCase()) ||
                            list[i].cost_for_one.contains(search_bar.text.toString()) ||
                            list[i].rating.contains(search_bar.text.toString())
                        ) {
                            list_search.add(list[i])
                        }
                    }
                    recl_view.adapter = Main_Recl_Adapter(activity as Context, list_search)
                    (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
                      }


        }
        )
        refresh.setOnRefreshListener {
            getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()

        }
        return view

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var ratingCompName = Comparator<data_list>{item1, item2 ->
            if(item1.name.compareTo(item2.name,true)==0){
                item1.rating.compareTo(item2.rating,true)
            }else{
                item1.name.compareTo(item2.name,true)
            }
        }
        var ratingComprating = Comparator<data_list>{item1, item2 ->
            if(item1.rating.compareTo(item2.rating,true)==0){
                item1.name.compareTo(item2.name,true)
            }else{
                item1.rating.compareTo(item2.rating,true)
            }
        }
        var ratingCompPrice = Comparator<data_list>{item1, item2 ->
            if(item1.cost_for_one.compareTo(item2.cost_for_one,true)==0){
                item1.rating.compareTo(item2.rating,true)
            }else{
                item1.cost_for_one.compareTo(item2.cost_for_one,true)
            }
        }
        val id = item.itemId
        when(id){
            R.id.by_name->{
                Collections.sort(list,ratingCompName)
                recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
            }
            R.id.by_rating->{
                Collections.sort(list,ratingComprating)
                recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
            }
            R.id.by_price->{
                Collections.sort(list,ratingCompPrice)
                recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
            }
            R.id.asc->{
                if(!asc) {
                    list.reverse()
                    asc = true
                    recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                    (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
                }
            }
            R.id.dsc->{
                if(asc) {
                    list.reverse()
                    asc = false
                    recl_view.adapter = Main_Recl_Adapter(activity as Context,list)
                    (recl_view.adapter as Main_Recl_Adapter).notifyDataSetChanged()
                }
            }
            R.id.refresh ->{
                getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(Connection().checkConnectivity(activity as Context)) {
            MenuInflater(activity as Context).inflate(R.menu.app_bar_menu, menu)
            return super.onCreateOptionsMenu(menu, inflater)
        }

    }

}