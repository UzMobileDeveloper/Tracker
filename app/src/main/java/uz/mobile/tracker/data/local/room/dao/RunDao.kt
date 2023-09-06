package uz.mobile.tracker.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.mobile.tracker.data.local.room.entity.Run

@Dao
interface RunDao {

    @Insert
    suspend fun insert(run: Run)

    @Delete
    suspend fun delete(run: Run)


    @Query("SELECT * FROM RUNNING_TABLE ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate():LiveData<List<Run>>

    @Query("SELECT * FROM RUNNING_TABLE ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeInMillis():LiveData<List<Run>>

    @Query("SELECT * FROM RUNNING_TABLE ORDER BY avgSpeedInKMH DESC")
    fun getAllRunsSortedByAvgSpeedInKMH():LiveData<List<Run>>

    @Query("SELECT * FROM RUNNING_TABLE ORDER BY caloriesBurned DESC")
    fun getAllRunsSortedByCaloriesBurned():LiveData<List<Run>>

    @Query("SELECT * FROM RUNNING_TABLE ORDER BY distanceInMetres DESC")
    fun getAllRunsSortedByDistanceInMetres():LiveData<List<Run>>


    @Query("SELECT SUM(timeInMillis) from running_table")
    fun getTotalTimeInMillis():LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) from running_table")
    fun getTotalCaloriesBurned():LiveData<Int>

    @Query("SELECT SUM(distanceInMetres) from running_table")
    fun getTotalDistanceInMetres():LiveData<Int>

    @Query("SELECT SUM(avgSpeedInKMH) from running_table")
    fun getTotalAvgSpeedInKMH():LiveData<Float>






}