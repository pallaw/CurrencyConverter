package com.pallaw.currencyconverter.data


import com.pallaw.currencyconverter.data.local.database.dao.CurrencyDao
import com.pallaw.currencyconverter.data.remote.CurrencyConverterAPI
import com.pallaw.currencyconverter.data.remote.model.CurrencyRatesResponse
import com.pallaw.currencyconverter.domain.CurrencyRepository
import com.pallaw.currencyconverter.util.Resource
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val currencyConverterAPI: CurrencyConverterAPI
) : CurrencyRepository {

//    override val currencys: Flow<List<String>> =
//        currencyDao.getCurrencys().map { items -> items.map { it.name } }
//
//    override suspend fun add(name: String) {
//        currencyDao.insertCurrency(Currency(name = name))
//    }

    override suspend fun getLatestRates(): Resource<CurrencyRatesResponse> {
        val response = currencyConverterAPI.getLatestRates()
        val result = response.body()

        return if (response.isSuccessful && result != null) {
            Resource.Success(result)
        } else {
            Resource.Error(response.message())
        }
    }
}
