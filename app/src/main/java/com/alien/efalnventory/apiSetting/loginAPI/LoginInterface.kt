package com.alien.efaInventory.apiSetting.loginAPI

import com.alien.efaInventory.dataModel.loginApiModel.LoginApICallback
import com.alien.efaInventory.dataModel.loginApiModel.LoginCallModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface LoginInterface {
    @POST("/api/AuthenticationApi/Check")
    fun getLogin(@Body credential: LoginCallModel): Call<LoginApICallback>
}

