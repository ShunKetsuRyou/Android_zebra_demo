package com.alien.efaInventory.dataModel.loginApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User  (
@SerialName("LoginId") var loginId: String?,
@SerialName("WorkId") var workId: String?,
@SerialName("DisplayName") var displayName: String?,
)
