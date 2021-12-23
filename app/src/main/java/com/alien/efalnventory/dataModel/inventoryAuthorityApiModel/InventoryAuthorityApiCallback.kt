package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class InventoryAuthorityApiCallback(
    @SerialName("respCode") val respCode: String?,
    @SerialName("respMsg") val respMsg: String?,
    @SerialName("respData") val respData: String?,
    @SerialName("fail") val fail: Boolean?

)
