package com.alien.efaInventory.dataModel.companyDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyInfoResponse(

    @SerialName("success") val success: String?,
    @SerialName("errMsg") val errMsg: String?,
    @SerialName("companyList") val companyList: List<CompanyCodeModel>
)
