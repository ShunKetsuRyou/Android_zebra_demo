package com.alien.efaInventory.apiSetting.dataDownloadApi

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType


class DataDownloadApiService {
    @ExperimentalSerializationApi
    fun  dataDownloadRetrofitManager(): Retrofit.Builder {

        val retrofit = Retrofit.Builder()
        val contentType = "application/json".toMediaType()

        return retrofit
            .baseUrl("http://ptwsvcdev.pXXXXXXXXXrp.com")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @ExperimentalSerializationApi
    fun dataDownloadClient(): Retrofit {

        return dataDownloadRetrofitManager().build()
    }






}
