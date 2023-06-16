package com.pallaw.currencyconverter.data.remote.di

import com.pallaw.currencyconverter.data.remote.CurrencyConverterAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://openexchangerates.org/api"

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyConverterAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyConverterAPI::class.java)

}