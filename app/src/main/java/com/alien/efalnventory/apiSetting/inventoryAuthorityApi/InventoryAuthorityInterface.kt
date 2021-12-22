package com.alien.efaInventory.apiSetting.inventoryAuthorityApi

import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.InventoryAuthorityApiCallback
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.InventoryAuthorityCallModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface InventoryAuthorityInterface {
    @POST("/windapi/api/efa/allocate/inventoryByCompany/get")
    fun getInventoryAuthority(@Body credential: InventoryAuthorityCallModel): Call<InventoryAuthorityApiCallback>
}

