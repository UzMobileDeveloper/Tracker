package uz.mobile.tracker.data.repository

import uz.mobile.tracker.data.local.room.dao.RunDao
import uz.mobile.tracker.data.local.room.entity.Run
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {

    suspend fun insertRun(run: Run) = runDao.insert(run)
    suspend fun deleteRun(run: Run) = runDao.delete(run)
    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()
    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()
    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()
    fun getAllRunsSortedByDistanceInMetres() = runDao.getAllRunsSortedByDistanceInMetres()
    fun getAllRunsSortedByAvgSpeedInKMH() = runDao.getAllRunsSortedByAvgSpeedInKMH()


    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
    fun getTotalDistanceInMetres() = runDao.getTotalDistanceInMetres()
    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()
    fun getTotalAvgSpeedInKMH() = runDao.getTotalAvgSpeedInKMH()



}