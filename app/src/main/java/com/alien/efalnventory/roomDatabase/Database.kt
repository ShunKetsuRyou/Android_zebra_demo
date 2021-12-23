package com.alien.efaInventory.roomDatabase

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alien.efaInventory.dao.InventoryLabelDataDao
import com.alien.efaInventory.dao.InventoryStatusAdminDataDao
import com.alien.efaInventory.dao.InventoryStatusMISDataDao
import com.alien.efaInventory.dataModel.modelForRoom.KeyValueTransferModel
import com.alien.efaInventory.entity.*

@Database(entities = [InventoryDatabaseModel::class, InventoryDeptInfoModel::class, KeyValueTransferModel::class, InventoryLabelData::class, InventoryStatusAdminData::class, InventoryStatusMISData::class, PreviousUserSaveModel::class],version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    companion object {
        private const val DB_NAME = "RecordData.db" //資料庫名稱
        @Volatile
        private var instance: DataBase? = null
        @Synchronized
        fun getInstance(context: Context): DataBase? {
            if (instance == null) {
                instance = create(context) //創立新的資料庫
            }
            return instance
        }
        private fun create(context: Context): DataBase{
            return Room.databaseBuilder(context, DataBase::class.java, DB_NAME).allowMainThreadQueries().build()
        }
    }
    abstract fun getDataUao():Dao

    // Add for InventoryLabel
    abstract fun getInventoryLabelDao(): InventoryLabelDataDao
    abstract fun getInventoryStatusAdminDao(): InventoryStatusAdminDataDao
    abstract fun getInventoryStatusMISDao(): InventoryStatusMISDataDao
}
