package com.pallaw.currencyconverter.data.di

import com.pallaw.currencyconverter.data.ExchangeRateRepository
import com.pallaw.currencyconverter.domain.ExchangeRateRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindExchangeRateRepository(
        currencyRepository: ExchangeRateRepository
    ): ExchangeRateRepositoryInterface

}

val fakeCurrencys = listOf("One", "Two", "Three")
