package com.alien.efaInventory.apiSetting.comapnySortAPI

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType


class RetrofitManager {
    @ExperimentalSerializationApi
    private fun  retrofitManager(): Retrofit.Builder {

        val retrofit = Retrofit.Builder()
        val contentType = "application/json".toMediaType()

        return retrofit
            .baseUrl("http://ptwsvcdev.pegatroncorp.com")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @ExperimentalSerializationApi
    fun getClient(): Retrofit {

        return retrofitManager().build()
    }






}
