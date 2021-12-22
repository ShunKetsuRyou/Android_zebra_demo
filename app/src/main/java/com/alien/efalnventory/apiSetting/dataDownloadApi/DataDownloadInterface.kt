package com.alien.efaInventory.apiSetting.dataDownloadApi

import com.alien.efaInventory.dataModel.downloadDataApiModel.DownloadDataApiCallback
import com.alien.efaInventory.dataModel.downloadDataApiModel.DownloadDataCallModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface DataDownloadInterface {
    @POST("/windapi/api/efa/allocate/inventoryByDept/get")
    fun getDownloadData(@Body credential: DownloadDataCallModel): Call<DownloadDataApiCallback>
}

