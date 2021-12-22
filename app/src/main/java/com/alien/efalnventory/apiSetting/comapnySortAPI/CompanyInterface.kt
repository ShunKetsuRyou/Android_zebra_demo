package com.alien.efaInventory.apiSetting.comapnySortAPI

import com.alien.efaInventory.dataModel.companyDataApiModel.CompanyApICallback
import com.alien.efaInventory.dataModel.companyDataApiModel.CompanyCallModel

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST



interface CompanyInterface {
    @POST("/windapi/api/efa/allocate/inventoryCompany/get")
    fun getCompany(@Body credential: CompanyCallModel): Call<CompanyApICallback>
}

