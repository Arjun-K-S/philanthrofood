package com.xidhu.foodhunt.database


import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.lang.Exception

@Database(entities = [Cart_Items::class],version = 1)
abstract class CartDatabase: RoomDatabase() {

    abstract fun cartDao():CartDAO
}

@Database(entities = [Fav_res::class],version = 1)
abstract class FavDatabase: RoomDatabase() {

    abstract fun FavDao():FavDAO
}

class CartData(val context: Context, val cart_ett:Cart_Items, val mode:String):
    AsyncTask<Void, Void, Any>(){
    val db = Room.databaseBuilder(context,CartDatabase::class.java,"cart").build()
    override fun doInBackground(vararg p0: Void?) :Any{
        when(mode){
            "insert" ->{
                db.cartDao().insertCart(cart_ett)
                db.close()
                return true
            }
            "delete" ->{
                db.cartDao().deleteCart(cart_ett)
                db.close()
                return true
            }
            "getall"->{
                val a = db.cartDao().getAllCart()
                db.close()
                return a

            }
            "getid"->{
                val a = db.cartDao().getCartById(cart_ett.item_id.toString())
                db.close()
                return  a
            }
            "deleteall"->{
                db.cartDao().deleteAll()
                db.close()
                return  true
            }
            else ->{
                return  false
            }

        }

    }

}

class FavData(val context: Context, val fav_ett:Fav_res, val mode:String):
    AsyncTask<Void, Void, Any>(){
    val db = Room.databaseBuilder(context,FavDatabase::class.java,"fav").build()
    override fun doInBackground(vararg p0: Void?) :Any{
        when(mode){
            "insert" ->{
                db.FavDao().insertFav(fav_ett)
                db.close()
                return true
            }
            "delete" ->{
                db.FavDao().deleteFav(fav_ett)
                db.close()
                return true
            }
            "getall"->{
                val a = db.FavDao().getAllFav()
                db.close()
                return a

            }
            "getid"->{
                val a = db.FavDao().getFavByName(fav_ett.res_name)
                db.close()
                return  a
            }
            "deleteall"->{
                db.FavDao().deleteAll()
                db.close()
                return  true
            }
            else ->{
                return  false
            }

        }

    }

}
