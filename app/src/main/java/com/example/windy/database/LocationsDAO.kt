package com.example.windy.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDAO{

    @Query("SELECT * FROM locations")
    fun getAllLocations() : Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: Location):Long

    @Delete
    suspend fun deleteLocation(location: Location)
}




@Entity("locations")
data class Location(@PrimaryKey val name:String,val longitude:Double,val latitude:Double)