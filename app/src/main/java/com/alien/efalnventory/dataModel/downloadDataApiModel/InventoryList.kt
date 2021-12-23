package com.alien.efaInventory.dataModel.downloadDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InventoryList(
    @SerialName("deptId") var deptId: String?,
    @SerialName("deptName") var deptName: String?,
    @SerialName("deptEnName") var deptEnName: String?,
    @SerialName("deptContact") val deptContact: List<DeptContact>?,
    @SerialName("detailList") val detailList: List<DetailList>?,
)
