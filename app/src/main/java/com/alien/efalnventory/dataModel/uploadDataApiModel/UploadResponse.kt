package com.alien.efaInventory.dataModel.uploadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(

    @SerialName("success") val success: String?,
    @SerialName("errMsg") val errMsg: String?,
    @SerialName("errInventoryList") val errInventoryList: List<ErrInventoryListModel>
)
