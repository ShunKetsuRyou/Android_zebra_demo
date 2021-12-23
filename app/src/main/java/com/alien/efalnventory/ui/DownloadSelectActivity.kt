package com.alien.efaInventory.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.R
import com.alien.efaInventory.adapters.DownloadSelectAdapter
import com.alien.efaInventory.apiSetting.dataDownloadApi.DataDownloadApiService
import com.alien.efaInventory.apiSetting.dataDownloadApi.DataDownloadInterface
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityApiService
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityInterface
import com.alien.efaInventory.dataModel.localDataProcessModel.AuthorityInformation
import com.alien.efaInventory.entity.InventoryDatabaseModel
import com.alien.efaInventory.entity.InventoryDeptInfoModel
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.*
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.util.ProgressionBar
import com.facebook.stetho.Stetho
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.Int as Int
import android.widget.CheckBox
import com.alien.efaInventory.dataModel.downloadDataApiModel.*
import kotlinx.android.synthetic.main.activity_downloadselect.*


class DownloadSelectActivity :  Activity() {
    private var recyclerViewForDataList: RecyclerView? = null
    private var adapter: DownloadSelectAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloadselect)
        recyclerViewForDataList = findViewById(R.id.recyclerViewForDataList)
        getDownloadList()
        val selectAll: CheckBox = findViewById(R.id.selectAll)
        selectAll.setOnCheckedChangeListener { _, _ ->
            if (selectAll.isChecked) {
            for (i in 0 until adapter!!.itemCount) {
                val totalInformation = adapter!!.getItem()
                val authorityList = mutableListOf<AuthorityInformation>()
                totalInformation.forEach {
                    if (it.isSelected) {
                        authorityList.add(it)
                    }
                }
                if (authorityList.size < adapter!!.itemCount || authorityList.size != 0) {
                    totalInformation.forEach {
                        it.isSelected = false
                        it.isSelected = !it.isSelected
                    }
                } else {
                    totalInformation.forEach {
                        //沒的勾到的選，有勾到的不選
                        it.isSelected = false
                        it.isSelected = true
                    }
                }
                recyclerViewForDataList!!.layoutManager =
                    LinearLayoutManager(this@DownloadSelectActivity)
                recyclerViewForDataList!!.adapter = adapter
            }
        }else{
                val totalInformation = adapter!!.getItem()
                totalInformation.forEach {
                 it.isSelected = false
                }
                recyclerViewForDataList!!.layoutManager =
                    LinearLayoutManager(this@DownloadSelectActivity)
                recyclerViewForDataList!!.adapter = adapter
        }
        }
    }
    fun downloadData (view: View?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DownloadSelectActivity)
        val downloadAlertDialog: AlertDialog = builder.create()
        val selectedId = intent.getStringExtra("pass")
        downloadAlertDialog.setCancelable(true)
        builder.setTitle(getString(R.string.提醒))
            .setMessage(getString(R.string.下載資料庫會重置資料))
        builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
            Stetho.initializeWithDefaults(this@DownloadSelectActivity)//設置資料庫監視
            deleteAll()
            val dataInput = adapter!!.getItem()
            var deptId: String?
            val loading = ProgressionBar(this@DownloadSelectActivity)
            loading.startLoading()

            Thread{
                dataInput.forEach {

                    if ("${it.isSelected}" == "true") {
                        deptId = it.id
                        getDataFromApi(selectedId!!.toInt(), deptId!!)

                    }

                }
                downloadAlertDialog.dismiss()
            }.start()
        }
        builder.setCancelable(true)
        builder.show()
    }

    private fun getPreferences(): SharedPreferences {
        return this.getSharedPreferences("Value", Context.MODE_PRIVATE)
    }
    private fun getUserNameFromPreference(): String {
        return getDataFromPreference("name")
    }
    private fun getCompanyFromPreference(): String {
        return getDataFromPreference("company")
    }
    private fun getCompanyCodeFromPreference(): String {
        return getCompanyFromPreference().take(4)
    }
    private fun getDataFromPreference(key: String): String {
        return getPreferences().getString(key, String()).toString()
    }
    private fun getDownloadList(){
        val getDownloadListRequest = InventoryAuthorityCallModel(getUserNameFromPreference(),getCompanyCodeFromPreference())
        val client: InventoryAuthorityInterface =
            InventoryAuthorityApiService().inventoryAuthorityClient().create(
                InventoryAuthorityInterface::class.java)
        val call: retrofit2.Call<InventoryAuthorityApiCallback> = client.getInventoryAuthority(getDownloadListRequest)
        val authorityDataSet = mutableListOf<AuthorityInformation>()
        val authorityList = mutableListOf(mutableListOf<AuthorityDeptList>())
        call.enqueue(object : retrofit2.Callback<InventoryAuthorityApiCallback> {
            override fun onResponse(
                call: retrofit2.Call<InventoryAuthorityApiCallback>,
                response: retrofit2.Response<InventoryAuthorityApiCallback>,
            ) {
                val inventoryAuthorityResponse: InventoryAuthorityResponse? =
                    response.body()!!.respData?.let {
                        Json.decodeFromString(it)
                    }
                val settingList = inventoryAuthorityResponse!!.settingList.toMutableList()

                settingList.forEach {

                    val passScope = intent.getStringExtra("passScope")
                    println(passScope)
                    if(it.scope == passScope) {
                        authorityList.add(it.authorityDeptList.toMutableList())
                    }
                }
                authorityList.forEach { authorities ->

                    authorities.forEach {

                        authorityDataSet.add(getAuthorityInformationByDeptList(it))
                    }
                }

                adapter = DownloadSelectAdapter(
                    this@DownloadSelectActivity,
                    authorityDataSet
                )
                recyclerViewForDataList!!.layoutManager = LinearLayoutManager(this@DownloadSelectActivity)
                recyclerViewForDataList!!.adapter = adapter
                    }
            override fun onFailure(
                call: retrofit2.Call<InventoryAuthorityApiCallback>?,
                t: Throwable?
            ) {
                println("------------------- ${t.toString()}")
            }
        })
    }
    private fun getDataFromApi(settingIdInput: Int = 0, deptIdIdInput:String = ""){
        val getDataRequest = DownloadDataCallModel(settingIdInput,deptIdIdInput)
        Stetho.initializeWithDefaults(this)//設置資料庫監視
        val client: DataDownloadInterface =
            DataDownloadApiService().dataDownloadClient().create(DataDownloadInterface::class.java)
        val call: retrofit2.Call<DownloadDataApiCallback> = client.getDownloadData(getDataRequest)
        call.enqueue(object : retrofit2.Callback<DownloadDataApiCallback> {
            override fun onResponse(
                call: retrofit2.Call<DownloadDataApiCallback>,
                response: retrofit2.Response<DownloadDataApiCallback>,
            ) {
                val downloadDataResponse: DownloadDataResponse? =
                    response.body()!!.respData?.let {
                        Json.decodeFromString(it)
                    }


                val downloadInventoryList = downloadDataResponse!!.inventoryList

                downloadInventoryList.detailList?.forEach { it ->

                    insertData(getInventoryDatabaseModelFromDetailList(downloadInventoryList, it))
                }


                downloadInventoryList.deptContact!!.forEach {

                    insertTableData(getInventoryDeptInfoModelFromDeptContact(downloadInventoryList, it))
                }

                val i = Intent(btnGetData!!.context, UserDownloadActivity::class.java)
                btnGetData!!.context.startActivity(i)
            }override fun onFailure(
                call: retrofit2.Call<DownloadDataApiCallback>?,
                t: Throwable?
            ) {
            }
        })
    }
    fun insertData(myData: InventoryDatabaseModel){
        Thread{
            DataBase.getInstance(this)!!.getDataUao().insertData(myData)
        }.start()
    }
    fun insertTableData(myData: InventoryDeptInfoModel){
        Thread{
            DataBase.getInstance(this)!!.getDataUao().insertTableData(myData)
        }.start()
    }
    private fun deleteAll(){
        Thread{
            DataBase.getInstance(this)!!.getDataUao().nukeTable()
        }.start()
    }

    private fun getAuthorityInformationByDeptList(authorityDeptList: AuthorityDeptList): AuthorityInformation {

        val authorityDataInput = AuthorityInformation()
        authorityDataInput.count = authorityDeptList.count
        authorityDataInput.id = authorityDeptList.id.toString()
        authorityDataInput.deptEnName =
            authorityDeptList.deptEnName.toString()
        authorityDataInput.deptName = authorityDeptList.deptName.toString()

        return authorityDataInput
    }

    private fun getInventoryDatabaseModelFromDetailList(
        downloadInventoryList: InventoryList,
        detailList: DetailList
    ): InventoryDatabaseModel {

        return InventoryDatabaseModel(
            id = detailList.id,
            assetId = detailList.assetId.toString(),
            costCorner = detailList.costCenter.toString(),
            quantity = detailList.quantity,
            storageId = detailList.storageId.toString(),
            storageDescription = detailList.storageDescription.toString(),
            keeperId = detailList.keeperId.toString(),
            group3 = detailList.group3.toString(),
            group2 = detailList.group2.toString(),
            deptId = downloadInventoryList.deptId.toString(),
            productName = detailList.productName.toString(),
            spec = detailList.spec.toString(),
            inventoryStatus = "",
            note = "",
            inventoryLabelStatus = "",
            partNo = detailList.partNo.toString(),
            updateStatus = "",
            keeperName = detailList.keeperName.toString(),
            primaryPhaseInventoryLabelStatus = detailList.primaryPhaseInventoryLabelStatus.toString(),
            primaryPhaseInventoryStatus = detailList.primaryPhaseInventoryStatus.toString(),
            inventoryStage = false
        )
    }

    private fun getInventoryDeptInfoModelFromDeptContact(
        downloadInventoryList: InventoryList,
        deptContact: DeptContact
    ): InventoryDeptInfoModel {

        return InventoryDeptInfoModel(
            deptId = downloadInventoryList.deptId.toString(),
            loginId = deptContact.loginId.toString(),
            inventorName = deptContact.name.toString(),
            workId = deptContact.workId.toString(),
            tel = deptContact.tel.toString(),
        )
    }
}


