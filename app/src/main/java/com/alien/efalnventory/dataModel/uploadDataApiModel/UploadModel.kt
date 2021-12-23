package com.alien.efaInventory.dataModel.uploadDataApiModel



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadModel  (
    @SerialName("id") var taskId: Int = 0,
    @SerialName("labelStatus") var labelStatus: String?,
    @SerialName("inventoryStatus") var uploadInventoryStatus:String?,
    @SerialName("note") var note: String?,
    @SerialName("updateBy") var updateBy: String?,


    )
