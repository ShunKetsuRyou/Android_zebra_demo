package com.alien.efaInventory.interfaces
import com.alien.efaInventory.dataModel.localDataProcessModel.DownloadData

interface DataDownloadInterface{
    fun downloadInventoryData(download:DownloadData)
    fun takeInventory(download: DownloadData)
}
