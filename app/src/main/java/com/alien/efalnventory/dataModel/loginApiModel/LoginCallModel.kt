package com.alien.efaInventory.dataModel.loginApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginCallModel(

    @SerialName("LoginId")
    val LoginId: String?,
    @SerialName("Password")
    val Password: String?,
    @SerialName("UserKey")
    val UserKey: String?
)
