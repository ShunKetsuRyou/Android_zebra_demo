package com.alien.efaInventory.dataModel.downloadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DownloadDataCallModel(
    @SerialName("settingId")
    val settingId: Int = 0,
    @SerialName("deptId")
    val deptId: String?
)
