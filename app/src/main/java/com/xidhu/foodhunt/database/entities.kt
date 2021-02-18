package com.xidhu.foodhunt.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class Cart_Items(
    @PrimaryKey val item_id:Int?,
    @ColumnInfo(name = "item_name") val itemName:String,
    @ColumnInfo(name = "item_price") val itemPrice:String,
    @ColumnInfo(name = "item_res") val itemRes:String,
    @ColumnInfo(name = "item_res_id") val itemResId:String
)

@Entity(tableName = "fav_res")
data class Fav_res(
    @PrimaryKey val res_name:String,
    @ColumnInfo(name = "fav_img") val img:String,
    @ColumnInfo(name = "fav_id") val id:String

)