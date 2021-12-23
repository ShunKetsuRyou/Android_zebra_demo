package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
@Serializable
data class InventoryMISStatus (
    @SerialName("key") val key: String?,
    @SerialName("value") val value: String?,
        )
