package com.xidhu.foodhunt.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDAO {

    @Insert
    fun insertCart(cart_ett:Cart_Items?)

    @Delete
    fun deleteCart(cart_ett: Cart_Items?)

    @Query("SELECT * FROM cart_items")
    fun getAllCart():List<Cart_Items>

    @Query("SELECT * FROM cart_items WHERE item_id = :itemId")
    fun getCartById(itemId:String):Cart_Items

    @Query("DELETE FROM cart_items")
    fun deleteAll()


}

@Dao
interface FavDAO {

    @Insert
    fun insertFav(fav_ett:Fav_res?)

    @Delete
    fun deleteFav(fav_ett: Fav_res?)

    @Query("SELECT * FROM fav_res")
    fun getAllFav():List<Fav_res>

    @Query("SELECT * FROM fav_res WHERE res_name = :itemId")
    fun getFavByName(itemId:String):Fav_res

    @Query("DELETE FROM fav_res")
    fun deleteAll()


}