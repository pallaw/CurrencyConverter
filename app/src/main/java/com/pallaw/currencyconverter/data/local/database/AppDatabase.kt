package com.pallaw.currencyconverter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pallaw.currencyconverter.data.local.database.dao.ExchangeRateDao
import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.data.local.database.typeconverter.MapTypeConverter

@Database(entities = [ExchangeRateEntity::class], version = 1)
@TypeConverters(MapTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}
