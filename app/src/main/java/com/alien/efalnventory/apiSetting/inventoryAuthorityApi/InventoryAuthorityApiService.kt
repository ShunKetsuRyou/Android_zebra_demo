package com.alien.efaInventory.apiSetting.inventoryAuthorityApi

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType


class InventoryAuthorityApiService {
    @ExperimentalSerializationApi
    fun  inventoryAuthorityRetrofitManager(): Retrofit.Builder {

        val retrofit = Retrofit.Builder()
        val contentType = "application/json".toMediaType()

        return retrofit
            .baseUrl("http://ptwsvcdev.pXXXXXXXXXXrp.com")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @ExperimentalSerializationApi
    fun inventoryAuthorityClient(): Retrofit {

        return inventoryAuthorityRetrofitManager().build()
    }






}
