package com.alien.efaInventory.dataModel.loginApiModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdData  (
    @SerialName("User") var User: User,

    )
