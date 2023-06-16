package com.pallaw.currencyconverter.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pallaw.currencyconverter.data.local.database.entity.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency ORDER BY uid DESC LIMIT 10")
    fun getCurrencys(): Flow<List<Currency>>

    @Insert
    suspend fun insertCurrency(item: Currency)
}
