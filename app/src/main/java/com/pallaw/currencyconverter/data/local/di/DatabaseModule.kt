package com.pallaw.currencyconverter.data.local.di

import android.content.Context
import androidx.room.Room
import com.pallaw.currencyconverter.data.local.database.AppDatabase
import com.pallaw.currencyconverter.data.local.database.dao.ExchangeRateDao
import com.pallaw.currencyconverter.data.remote.model.ExchangeRateDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase): ExchangeRateDao {
        return appDatabase.exchangeRateDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ExchangeRate"
        ).build()
    }
}
