package com.pallaw.currencyconverter.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.pallaw.currencyconverter.data.local.database.AppDatabase
import com.pallaw.currencyconverter.data.local.database.dao.CurrencyDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Currency"
        ).build()
    }
}
