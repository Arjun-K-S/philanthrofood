package com.xidhu.foodhunt.data

import java.util.ArrayList

data class order_list(
    val order_id:String,
    val res_name:String,
    val total_cost:String,
    val order_placed_at:String,
    val food_items:ArrayList<food_items>
)
data class food_items(
    val food_item_id:String,
    val food_name:String,
    val price:String
)