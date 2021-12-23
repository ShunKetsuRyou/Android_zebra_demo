package com.alien.efaInventory.dataModel.uploadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrInventoryListModel(

    @SerialName("id") val id: Int = 0,
    @SerialName("errMsg") val errMsg: String?,
)
