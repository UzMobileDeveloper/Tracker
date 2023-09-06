package uz.mobile.tracker.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mobile.tracker.data.local.room.dao.RunDao
import uz.mobile.tracker.data.local.room.entity.Run

@Database(entities = [Run::class], version = 1)
abstract class RunningDataBase : RoomDatabase() {

    abstract fun getRunDao(): RunDao

}