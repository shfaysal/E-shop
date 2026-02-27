package com.example.e_shop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_shop.data.local.dao.CartDao
import com.example.e_shop.data.local.dao.WishlistDao
import com.example.e_shop.data.local.entities.CartEntity
import com.example.e_shop.data.local.entities.WishlistEntity

@Database(entities = [CartEntity::class, WishlistEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
}
