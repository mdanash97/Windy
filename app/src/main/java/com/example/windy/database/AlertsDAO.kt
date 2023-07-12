package com.example.windy.database

import android.os.Message
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

@Dao
interface AlertsDAO{

    @Query("SELECT * FROM Alerts")
    fun getAllAlerts() : Flow<List<Alerts>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alerts: Alerts):Long

    @Delete
    suspend fun deleteAlert(alerts: Alerts)
}



@Entity(tableName = "Alerts")
data class Alerts(@PrimaryKey(autoGenerate = true)val id:Int=0, val title:String, val message: String,val time:Long) :
    Serializable