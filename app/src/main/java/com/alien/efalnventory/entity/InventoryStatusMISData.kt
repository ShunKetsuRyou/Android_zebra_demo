package com.alien.efaInventory.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InventoryStatusMISData")

data class InventoryStatusMISData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Long?,

    @NonNull
    @ColumnInfo(name = "StatusKey") val statusKey: String,

    @NonNull
    @ColumnInfo(name = "StatusValue") val statusValue: String
)
