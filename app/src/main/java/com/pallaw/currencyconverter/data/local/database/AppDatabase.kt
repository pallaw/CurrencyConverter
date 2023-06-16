package com.pallaw.currencyconverter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pallaw.currencyconverter.data.local.database.dao.CurrencyDao
import com.pallaw.currencyconverter.data.local.database.entity.Currency

@Database(entities = [Currency::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
