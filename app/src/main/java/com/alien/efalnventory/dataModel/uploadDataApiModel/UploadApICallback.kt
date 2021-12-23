package com.alien.efaInventory.dataModel.uploadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UploadApICallback(
    @SerialName("respCode") val respCode: String?,
    @SerialName("respMsg") val respMsg: String?,
    @SerialName("respData") val respData: String?,
    @SerialName("fail") val fail: Boolean?

)
