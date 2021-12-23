package com.alien.efaInventory.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InventoryLabelData")

data class InventoryLabelData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Long?,

    @NonNull
    @ColumnInfo(name = "LabelKey") val labelKey: String,

    @NonNull
    @ColumnInfo(name = "LabelValue") val labelValue: String
)
