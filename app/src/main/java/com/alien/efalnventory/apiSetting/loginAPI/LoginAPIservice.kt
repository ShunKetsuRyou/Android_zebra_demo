package com.alien.efaInventory.apiSetting.loginAPI

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType


class LoginRetrofitManager {
    @ExperimentalSerializationApi
    private fun  loginRetrofitManager(): Retrofit.Builder {

        val loginRetrofit = Retrofit.Builder()
        val contentType = "application/json".toMediaType()

        return loginRetrofit
            .baseUrl("http://adportalqc.pegatroncorp.com")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @ExperimentalSerializationApi
    fun getLoginClient(): Retrofit {

        return loginRetrofitManager().build()
    }






}
