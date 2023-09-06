package uz.mobile.tracker.domain.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.mobile.tracker.data.local.room.database.RunningDataBase
import uz.mobile.tracker.util.Constance.RUNNING_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RunningDataBase::class.java,
        RUNNING_DATABASE_NAME
    ).build()



}