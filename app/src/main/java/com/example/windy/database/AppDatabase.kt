package com.example.windy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Location::class,Alerts::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocationDAo() : LocationDAO
    abstract fun getAlertsDAo(): AlertsDAO

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"product_table").build()
                INSTANCE = instance
                instance
            }
        }
    }

}