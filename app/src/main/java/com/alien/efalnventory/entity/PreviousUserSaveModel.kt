package com.alien.efaInventory.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "PreviousUser")

class PreviousUserSaveModel : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var previousUserInventorName: String = ""



    constructor(
        id: Int,
        previousUserInventorName: String,

        ) {
        this.id = id
        this.previousUserInventorName = previousUserInventorName
    }
}
