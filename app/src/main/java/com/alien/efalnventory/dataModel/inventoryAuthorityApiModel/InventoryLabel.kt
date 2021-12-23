package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class InventoryLabel (

    @SerialName("key") val key: String?,
    @SerialName("value") val value: String?,

    )
