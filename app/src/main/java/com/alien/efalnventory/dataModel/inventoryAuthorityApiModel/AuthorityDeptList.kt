package com.alien.efaInventory.dataModel.inventoryAuthorityApiModel


import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
@Serializable
data class AuthorityDeptList(

    @SerialName("id") val id: String?,
    @SerialName("deptName") val deptName: String?,
    @SerialName("deptEnName") val deptEnName:String?,
    @SerialName("count") val count: Int,

)
