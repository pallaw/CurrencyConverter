package com.pallaw.currencyconverter.ui.di

import com.pallaw.currencyconverter.domain.ExchangeRateRepositoryInterface
import com.pallaw.currencyconverter.domain.usecase.GetExchangeRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetExchangeRateUseCase(exchangeRateRepository: ExchangeRateRepositoryInterface): GetExchangeRatesUseCase =
        GetExchangeRatesUseCase(exchangeRateRepository)

}