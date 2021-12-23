package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class InventoryAuthorityCallModel(
    @SerialName("loginId")
    val loginId: String?,
    @SerialName("company")
    val company: String?
)
