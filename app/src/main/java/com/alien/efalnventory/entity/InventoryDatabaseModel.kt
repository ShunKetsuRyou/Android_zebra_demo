package com.alien.efaInventory.entity
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "DetailTable")

class InventoryDatabaseModel : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var assetId: String = ""
    var quantity: Int = 0
    var costCorner: String = ""
    var storageId: String = ""
    var storageDescription: String = ""
    var keeperId: String = ""
    var deptId: String = ""
    var productName: String = ""
    var spec: String = ""
    var inventoryStatus: String = ""
    var inventoryLabelStatus: String = ""
    var note: String = ""
    var partNo: String = ""
    var group3: String = ""
    var group2: String = ""
    var updateStatus: String = ""
    var keeperName: String = ""
    var primaryPhaseInventoryLabelStatus: String = ""
    var primaryPhaseInventoryStatus: String = ""
    var inventoryStage: Boolean = false


    constructor(
        assetId: String,
        costCorner: String,
        quantity: Int,
        storageId: String,
        storageDescription: String,
        keeperId: String,
        deptId: String,
        group3: String,
        group2: String,
        productName: String,
        spec: String,
        inventoryStatus: String,
        note: String,
        inventoryLabelStatus: String,
        partNo: String,
        updateStatus: String,
        keeperName: String,
        primaryPhaseInventoryLabelStatus: String,
        primaryPhaseInventoryStatus: String,
        inventoryStage: Boolean,

        ) {
        this.assetId = assetId
        this.costCorner = costCorner
        this.quantity = quantity
        this.storageDescription = storageDescription
        this.storageId = storageId
        this.keeperId = keeperId
        this.deptId = deptId
        this.group3 = group3
        this.group2 = group2
        this.productName = productName
        this.spec = spec
        this.inventoryStatus = inventoryStatus
        this.note = note
        this.inventoryLabelStatus = inventoryLabelStatus
        this.partNo = partNo
        this.updateStatus =updateStatus
        this.keeperName = keeperName
        this.primaryPhaseInventoryLabelStatus = primaryPhaseInventoryLabelStatus
        this.primaryPhaseInventoryStatus = primaryPhaseInventoryStatus
        this.inventoryStage = inventoryStage

    }
    @Ignore //如果要使用多形的建構子，必須加入@Ignore
    constructor(
        id: Int,
        assetId: String,
        costCorner: String,
        quantity: Int,
        storageId: String,
        storageDescription: String,
        keeperId: String,
        group3: String,
        group2: String,
        deptId: String,
        productName: String,
        spec: String,
        inventoryStatus: String,
        note: String,
        inventoryLabelStatus: String,
        partNo: String,
        updateStatus: String,
        keeperName: String,
        primaryPhaseInventoryLabelStatus: String,
        primaryPhaseInventoryStatus: String,
        inventoryStage: Boolean,


        ) {
        this.id = id
        this.assetId = assetId
        this.costCorner = costCorner
        this.quantity = quantity
        this.storageDescription = storageDescription
        this.storageId = storageId
        this.keeperId = keeperId
        this.group3 = group3
        this.group2 = group2
        this.deptId = deptId
        this.productName = productName
        this.spec = spec
        this.inventoryStatus = inventoryStatus
        this.note = note
        this.inventoryLabelStatus = inventoryLabelStatus
        this.partNo = partNo
        this.updateStatus =updateStatus
        this.keeperName = keeperName
        this.primaryPhaseInventoryLabelStatus = primaryPhaseInventoryLabelStatus
        this.primaryPhaseInventoryStatus = primaryPhaseInventoryStatus
        this.inventoryStage = inventoryStage

    }

}
