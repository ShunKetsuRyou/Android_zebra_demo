package com.alien.efaInventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alien.efaInventory.entity.InventoryStatusMISData

@Dao
interface InventoryStatusMISDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventoryStatuses(vararg  inventoryStatusMISData: InventoryStatusMISData)
    @Query("SELECT * FROM InventoryStatusMISData")
    fun displayMISKey(): MutableList<InventoryStatusMISData>?
    @Query("SELECT * FROM InventoryStatusMISData WHERE statusKey = :statusKey")
    fun getValue(statusKey: String):InventoryStatusMISData
    @Query("SELECT * FROM InventoryStatusMISData WHERE statusValue = :statusValue")
    fun getKey(statusValue: String): InventoryStatusMISData
}
