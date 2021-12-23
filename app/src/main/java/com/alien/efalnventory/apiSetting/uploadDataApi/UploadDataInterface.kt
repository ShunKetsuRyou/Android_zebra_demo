package com.alien.efaInventory.apiSetting.uploadDataApi


import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadApICallback
import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadDataCallModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface UploadDataInterface {
    @POST("/windapi/api/efa/allocate/inventoryStatus/update")
    fun getUploadData(@Body credential: UploadDataCallModel): Call<UploadApICallback>
}

