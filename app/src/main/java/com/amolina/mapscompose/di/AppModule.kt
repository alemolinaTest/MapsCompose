package com.amolina.mapscompose.di

import android.app.Application
import androidx.room.Room
import com.amolina.mapscompose.data.AppDatabase
import com.amolina.mapscompose.data.CityDao
import com.amolina.mapscompose.repositories.CitiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    val dabaseName = "cities.database"

    @Provides
    @Singleton
    fun providesDatabase(context: Application): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, dabaseName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesDao(database: AppDatabase): CityDao = database.cityDao()

    @Provides
    fun providesCityRepository(dao: CityDao, context: Application): CitiesRepository =
        CitiesRepository(dao, context)

}