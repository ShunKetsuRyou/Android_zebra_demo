package com.alien.efaInventory.apiSetting.uploadDataApi

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType


class UploadDataApiService {
    @ExperimentalSerializationApi
    fun  uploadDataRetrofitManager(): Retrofit.Builder {

        val retrofit = Retrofit.Builder()
        val contentType = "application/json".toMediaType()

        return retrofit
            .baseUrl("http://ptwsvcdev.pCCCCCCCCCp.com")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @ExperimentalSerializationApi
    fun uploadDataAuthorityClient(): Retrofit {

        return uploadDataRetrofitManager().build()
    }






}
