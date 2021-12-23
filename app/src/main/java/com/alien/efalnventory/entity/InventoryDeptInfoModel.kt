package com.alien.efaInventory.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "DeptTable")

class InventoryDeptInfoModel : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var deptId: String = ""
    var loginId: String = ""
    var inventorName: String = ""
    var workId: String = ""
    var tel: String = ""



    constructor(
        deptId: String,
        loginId: String,
        inventorName: String,
        workId: String,
        tel: String,

        ) {
        this.deptId = deptId
        this.loginId = loginId
        this.inventorName = inventorName
        this.workId = workId
        this.tel = tel
    }
}
