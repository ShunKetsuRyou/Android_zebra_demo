package com.alien.efaInventory.dataModel.downloadDataApiModel

import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable

@Serializable

data class DownloadDataResponse(
    @SerialName("success") val success: String?,
    @SerialName("errMsg") val errMsg: String?,
    @SerialName("inventoryList") val inventoryList: InventoryList
)
