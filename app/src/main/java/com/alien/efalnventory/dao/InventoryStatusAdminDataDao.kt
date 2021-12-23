package com.alien.efaInventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alien.efaInventory.entity.InventoryStatusAdminData

@Dao
interface InventoryStatusAdminDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventoryStatuses(vararg  inventoryStatusAdminData: InventoryStatusAdminData)
    @Query("SELECT * FROM InventoryStatusAdminData")
    fun displayAdminKey(): MutableList<InventoryStatusAdminData>?
    @Query("SELECT * FROM InventoryStatusAdminData WHERE statusKey = :statusKey")
    fun getValue(statusKey: String): InventoryStatusAdminData
    @Query("SELECT * FROM InventoryStatusAdminData WHERE statusValue = :statusValue")
    fun getKey(statusValue: String): InventoryStatusAdminData
}
