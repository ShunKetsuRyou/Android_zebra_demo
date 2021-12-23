package com.alien.efaInventory.dataModel.uploadDataApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadDataCallModel(
    @SerialName("inventoryList")
    var uploadList: List<UploadModel>
)
