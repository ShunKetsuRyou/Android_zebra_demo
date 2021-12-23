package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
@Serializable
data class InventoryStatus (
    @SerialName("key") val key: String?,
    @SerialName("value") val value: String?,
        )
