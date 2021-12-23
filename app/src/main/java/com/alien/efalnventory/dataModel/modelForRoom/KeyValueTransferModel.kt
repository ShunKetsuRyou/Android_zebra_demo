package com.alien.efaInventory.dataModel.modelForRoom
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "KeyValueTable")

class KeyValueTransferModel(var storeType: String, var itemKey: String, var value: String) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0


}
