package com.alien.efaInventory.dataModel.loginApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginApICallback(
    @SerialName("Data") val Data: IdData,
    @SerialName("Message") val Message: String?,
    @SerialName("Succeeded") val Succeeded: Boolean

)
