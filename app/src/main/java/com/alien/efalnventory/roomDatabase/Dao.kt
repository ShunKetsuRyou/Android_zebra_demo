package com.alien.efaInventory.roomDatabase

import androidx.room.*
import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteQuery
import com.alien.efaInventory.entity.InventoryDatabaseModel
import androidx.lifecycle.LiveData
import com.alien.efaInventory.entity.InventoryDeptInfoModel
import com.alien.efaInventory.dataModel.modelForRoom.KeyValueTransferModel
import com.alien.efaInventory.entity.PreviousUserSaveModel

@Dao
interface Dao {
    @Query("SELECT * FROM DetailTable WHERE NULLIF(assetId, '') IS NULL")
     fun getList(): LiveData<InventoryDatabaseModel?>?
    @RawQuery
    fun insertDataRawFormat(query: SupportSQLiteQuery): Boolean?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
         fun insertData(myData: InventoryDatabaseModel)
    @Query("SELECT * FROM DetailTable WHERE assetId = :assetId and deptId = :deptId ")
    fun findDataByName(assetId: String,deptId: String ): InventoryDatabaseModel
    @Query("SELECT * FROM DetailTable WHERE assetId = :assetId")
    fun findDataByAssetId(assetId: String): InventoryDatabaseModel
    @Query("SELECT * FROM DetailTable WHERE id = :id")
    fun findDataByItemId(id: Int): InventoryDatabaseModel
    @Query("SELECT * FROM DetailTable WHERE deptId = :deptId")
    fun findDataByDeptId(deptId: String): InventoryDatabaseModel
    @Query("SELECT * FROM DetailTable WHERE  inventoryLabelStatus != '' and updateStatus == ''")
    fun findDataById(): MutableList<InventoryDatabaseModel>?
    @Query("SELECT * FROM DeptTable WHERE deptId = :deptId")
    fun findListByDeptId(deptId: String): MutableList<InventoryDeptInfoModel>
    @Query("INSERT INTO DetailTable (id,assetId,costCorner,storageId,storageDescription,keeperId,deptId,productName,spec,inventoryStatus,note,inventoryLabelStatus,partNo) VALUES(:id,:assetId,:costCorner,:storageId,:storageDescription,:keeperId,:deptId,:productName,:spec,:inventoryStatus,:note,:inventoryLabelStatus,:partNo)")
    fun insertData(
        id: Int,
        assetId: String,
        costCorner: String,
        storageId: String,
        storageDescription: String,
        keeperId: String,
        deptId: String,
        productName: String,
        spec: String,
        inventoryStatus: String,
        note: String,
        inventoryLabelStatus: String,
        partNo: String,
    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTableData(myData: InventoryDeptInfoModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyValueData(myData: KeyValueTransferModel)
    @Query("SELECT * FROM DetailTable ")
    fun check(): LiveData<InventoryDatabaseModel?>?
    @Query("SELECT * FROM DetailTable")
    fun displayAll(): MutableList<InventoryDatabaseModel>?
    @Query("SELECT * FROM DetailTable WHERE deptId = :deptId and inventoryLabelStatus == '' ")
    fun displayDept(deptId: String): MutableList<InventoryDatabaseModel>?
    @Query("UPDATE DetailTable SET  inventoryStatus = :inventoryStatus,note = :note,inventoryLabelStatus = :inventoryLabelStatus, inventoryStage = :inventoryStage WHERE assetId = :assetId")
    fun updateDataSpinner(
        assetId: String,
        inventoryStatus: String,
        note: String,
        inventoryLabelStatus: String,
        inventoryStage: Boolean
    )
    @Query("UPDATE DetailTable SET updateStatus = 'ok' WHERE assetId= :assetId")
    fun updateLabel(
        assetId: String,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPreviousUserData(myData: PreviousUserSaveModel)
    @Query("SELECT * FROM PreviousUser WHERE id = :id")
    fun findPreviousUserById(id: Int): PreviousUserSaveModel
    @Query("DELETE FROM PreviousUser")
    fun nukePreviousUserTable()

    @Query("DELETE FROM DetailTable")
    fun nukeTable()
    @Delete
    fun deleteData(myData: InventoryDatabaseModel)
    @Query("DELETE  FROM DetailTable WHERE id = :id")
    fun deleteData(id: Int)
    @Query("DELETE  FROM DetailTable WHERE assetId = :assetId")
    fun deleteDataSpecial(assetId: String)
    @Query("SELECT COUNT(assetId) FROM DetailTable WHERE deptId = :deptId")
    fun getDataCountTotal(deptId: String):Int
    @Query("SELECT COUNT(assetId) FROM DetailTable WHERE deptId = :deptId and inventoryStage = 0")
    fun getDataCountNone(deptId: String):Int
    @Query("SELECT COUNT(assetId) FROM DetailTable WHERE  updateStatus == 'ok' and deptId = :deptId")
    fun getDataCountUpdated(deptId: String):Int
    @Query("SELECT COUNT(assetId) FROM DetailTable WHERE  updateStatus == '' and deptId = :deptId and inventoryLabelStatus != '' and inventoryStage = 1 ")
    fun getDataInventoryCount(deptId: String):Int
    @Query("SELECT COUNT(assetId) FROM DetailTable WHERE  updateStatus == ''  and inventoryLabelStatus != '' and inventoryStage = 1 ")
    fun getDataNotUpdateCount():Int


}
