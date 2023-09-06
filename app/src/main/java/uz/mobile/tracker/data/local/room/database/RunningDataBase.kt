package uz.mobile.tracker.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.mobile.tracker.data.local.room.dao.RunDao
import uz.mobile.tracker.data.local.room.entity.Run
import uz.mobile.tracker.util.Converter

@TypeConverters(Converter::class)
@Database(entities = [Run::class], version = 1)
abstract class RunningDataBase : RoomDatabase() {

    abstract fun getRunDao(): RunDao

}