package com.alien.efaInventory.dataModel.localDataProcessModel

import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.AuthorityDeptList

class DownloadData {
    var id: String = String()
    var scope: String = String()
    var phase: String = String()
    var startDate: String = String()
    var endDate: String = String()
    var authorityList: List<AuthorityDeptList>? = null
}
