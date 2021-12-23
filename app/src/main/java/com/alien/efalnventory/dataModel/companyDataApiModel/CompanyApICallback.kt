package com.alien.efaInventory.dataModel.companyDataApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CompanyApICallback(
    @SerialName("respCode") val respCode: String?,
    @SerialName("respMsg") val respMsg: String?,
    @SerialName("respData") val respData: String?,
    @SerialName("fail") val fail: Boolean?

)
