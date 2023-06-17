package com.pallaw.currencyconverter.data


import android.util.Log
import com.pallaw.currencyconverter.data.local.database.dao.ExchangeRateDao
import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.data.remote.ExchangeRateService
import com.pallaw.currencyconverter.data.remote.model.ExchangeRateDto
import com.pallaw.currencyconverter.domain.ExchangeRateRepositoryInterface
import com.pallaw.currencyconverter.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateService: ExchangeRateService,
) : ExchangeRateRepositoryInterface {

    companion object {
        private const val FETCH_INTERVAL_MINUTES = 30
    }

    override fun getExchangeRates(): Flow<Resource<ExchangeRateEntity>> {
        return flow {

            //this will always listen to local db changes
            val localData = exchangeRateDao.getExchangeRates().firstOrNull()
            if (!localData.isNullOrEmpty()) {
                emit(Resource.Success(localData.first()))
            }

            val lastUpdateTime = localData?.firstOrNull()?.timestamp ?: 0L
            val currentTime = System.currentTimeMillis()/1000
            val elapsedTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - lastUpdateTime)

            if (elapsedTimeMinutes >= FETCH_INTERVAL_MINUTES) {
                val response = exchangeRateService.getExchangeRates()
                if (response.isSuccessful) {
                    val exchangeRateEntity = response.body()?.toExchangeRateEntity()
                    exchangeRateEntity?.let {
                        exchangeRateDao.insertExchangeRates(listOf(it))
                        emit(Resource.Success(it))
                    }
                } else {
                    // Handle network error
                    val errorBody = response.errorBody()?.string()
                    emit(Resource.Error("Failed to fetch exchange rates: $errorBody"))
                }
            }

        }
    }


    private fun ExchangeRateDto.toExchangeRateEntity(): ExchangeRateEntity {
        return ExchangeRateEntity(
            base = base,
            timestamp = timestamp,
            rates = rates
        )
    }
}
