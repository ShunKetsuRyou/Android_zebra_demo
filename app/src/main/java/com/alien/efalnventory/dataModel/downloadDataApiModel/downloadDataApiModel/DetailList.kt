package com.alien.efaInventory.dataModel.downloadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DetailList (
    @SerialName("id") var id: Int = 0,
    @SerialName("assetId") var assetId: String?,
    @SerialName("quantity") var quantity: Int = 0,
    @SerialName("costCenter") var costCenter:String?,
    @SerialName("storageId") var storageId: String?,
    @SerialName("storageDescription") var storageDescription: String?,
    @SerialName("keeperId") var keeperId: String?,
    @SerialName("keeperName") var keeperName: String?,
    @SerialName("partNo") var partNo:String?,
    @SerialName("productName") var productName: String?,
    @SerialName("group2") var group2: String?,
    @SerialName("group3") var group3: String?,
    @SerialName("spec") var spec: String?,
    @SerialName("primaryPhaseInventoryLabelStatus") var primaryPhaseInventoryLabelStatus: String?,
    @SerialName("primaryPhaseInventoryStatus") var primaryPhaseInventoryStatus:String?,


    )
