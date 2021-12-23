package com.alien.efaInventory.dataModel.companyDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyCodeModel(

    @SerialName("company") val code: String?,
    @SerialName("name") val name: String?,
)
