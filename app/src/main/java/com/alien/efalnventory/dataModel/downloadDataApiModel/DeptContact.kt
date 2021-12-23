package com.alien.efaInventory.dataModel.downloadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DeptContact (
    @SerialName("workId") var workId: String?,
    @SerialName("loginId") var loginId: String?,
    @SerialName("name") var name:String?,
    @SerialName("tel") val tel: String?,

    )
