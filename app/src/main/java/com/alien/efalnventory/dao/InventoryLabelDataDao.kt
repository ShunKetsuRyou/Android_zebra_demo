package com.alien.efaInventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alien.efaInventory.entity.InventoryLabelData

@Dao
interface InventoryLabelDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventoryLabels(vararg  inventoryLabelData: InventoryLabelData)
    @Query("SELECT * FROM InventoryLabelData")
    fun displayLabelKey(): MutableList<InventoryLabelData>?
    @Query("SELECT * FROM InventoryLabelData WHERE labelKey = :labelKey")
    fun getValue(labelKey: String): InventoryLabelData
    @Query("SELECT * FROM InventoryLabelData WHERE labelValue = :labelValue")
    fun getKey(labelValue: String): InventoryLabelData
}
