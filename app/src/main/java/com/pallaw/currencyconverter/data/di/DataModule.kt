package com.pallaw.currencyconverter.data.di

import com.pallaw.currencyconverter.data.DefaultCurrencyRepository
import com.pallaw.currencyconverter.data.local.database.dao.CurrencyDao
import com.pallaw.currencyconverter.data.remote.CurrencyConverterAPI
import com.pallaw.currencyconverter.domain.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsCurrencyRepository(
        currencyRepository: DefaultCurrencyRepository
    ): CurrencyRepository

//    //    TODO find difference between bind and dispatcher
//    @Singleton
//    @Provides
//    fun provideMainRepository(
//        dao: CurrencyDao,
//        api: CurrencyConverterAPI
//    ): CurrencyRepository = DefaultCurrencyRepository(dao, api)

}

//class FakeCurrencyRepository @Inject constructor() : CurrencyRepository {
//    override val currencys: Flow<List<String>> = flowOf(fakeCurrencys)
//
//    override suspend fun add(name: String) {
//        throw NotImplementedError()
//    }
//}

val fakeCurrencys = listOf("One", "Two", "Three")
