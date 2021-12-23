package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel

import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable

@Serializable

data class InventoryAuthorityResponse(
    @SerialName("success") val success: String?,
    @SerialName("errMsg") val errMsg: String?,

    // Rename
    //@SerialName("inventoryLabelStatusList") val inventoryLabel: List<InventoryLabel>,
    @SerialName("inventoryLabelStatusList") val inventoryLabelStatusList: List<InventoryLabel>,

    @SerialName("inventoryStatusAdministrationList") val inventoryStatusAdministrationList: List<InventoryStatus>,
    @SerialName("inventoryStatusMISList") val inventoryStatusMISList: List<InventoryMISStatus>,
    @SerialName("settingList") val settingList: List<SettingList>
)
