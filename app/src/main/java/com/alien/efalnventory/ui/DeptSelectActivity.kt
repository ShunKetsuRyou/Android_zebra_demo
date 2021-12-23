package com.alien.efaInventory.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.R
import com.alien.efaInventory.adapters.DeptSelectAdapter
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityApiService
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityInterface
import com.alien.efaInventory.apiSetting.uploadDataApi.UploadDataApiService
import com.alien.efaInventory.apiSetting.uploadDataApi.UploadDataInterface
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.*
import com.alien.efaInventory.dataModel.localDataProcessModel.AuthorityInformation
import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadApICallback
import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadDataCallModel
import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadModel
import com.alien.efaInventory.dataModel.uploadDataApiModel.UploadResponse
import com.alien.efaInventory.roomDatabase.DataBase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.content.SharedPreferences
import android.widget.Button
import com.alien.efaInventory.util.ProgressionBar


class DeptSelectActivity : AppCompatActivity() {
        var recyclerView: RecyclerView? = null
        private var searchView: SearchView? = null
        val authorityDataSet = mutableListOf<AuthorityInformation>()
        var authorityDataList = mutableListOf(mutableListOf<AuthorityDeptList>())
        var adapter: DeptSelectAdapter? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_deptselect)
            configUI()
            supportActionBar?.hide()
            recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
            searchView = findViewById<View>(R.id.searchView) as SearchView
            recyclerView!!.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            getInventoryList()
            searchView!!.queryHint = getString(R.string.部門代碼部門名稱部門窗口)
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter!!.filter.filter(newText)
                    return false
                }
            })
        }
    fun showErrorMessage(view:View?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DeptSelectActivity)
        val adError: AlertDialog = builder.create()
        adError.setCancelable(true)
        builder.setTitle(getString(R.string.同步錯誤))
        builder.setMessage(getErrorMessage())
        builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
            adError.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }
    private fun getTxtCompany(): TextView {
        return getTextViewById(R.id.textview_company)
    }
    private fun getUserNameFromPreference(): String {
        return getDataFromPreference("name")
    }
    private fun getDisplayNameFromPreference(): String {
        return getDataFromPreference("displayName")
    }
    private fun getCompanyFromPreference(): String {
        return getDataFromPreference("company")
    }
    private fun getPreferences(): SharedPreferences {
        return this.getSharedPreferences("Value", Context.MODE_PRIVATE)
    }
    private fun configUI() {
        getTxtUsername().text = getDisplayNameFromPreference()
        getTxtCompany().text = getCompanyFromPreference()
    }
    private fun getDataFromPreference(key: String): String {
        return getPreferences().getString(key, String()).toString()
    }
    private fun getTxtUsername(): TextView {
        return getTextViewById(R.id.textview_name)
    }
    private fun getButton(): Button {
        return getButtonViewById(R.id.buttonError)
    }
    private fun getButtonViewById(id: Int): Button {
        return findViewById(id)
    }
    private fun getTextViewById(id: Int): TextView {
        return findViewById(id)
    }
    private fun getErrorMessage(): String? {
        return intent.getStringExtra("errorMessage")
    }
    private fun getInventorID(): String? {
        return getDataFromPreference("inventorId")
    }
    private fun getPassScope(): String? {
        return intent.getStringExtra("passScopeDept")
    }
    private fun  getPassPhase(): String? {
        return intent.getStringExtra("passPhase")
    }
    private fun saveDataToReference(key: String, value: String) {
        val sharedPreferencesEditor: SharedPreferences.Editor = getPreferences().edit()
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }
    private fun deleteDataToReference(key: String) {
        val sharedPreferencesEditor: SharedPreferences.Editor = getPreferences().edit()
        sharedPreferencesEditor.remove(key)
        sharedPreferencesEditor.apply()
    }
    private fun saveErrorMessage(input:String) {
        saveDataToReference("errorMessage",input)
    }

    private fun getInventoryList() {
       val company = getCompanyFromPreference().take(4)
        val inventoryListRequest = InventoryAuthorityCallModel(getUserNameFromPreference(),company)
        val client: InventoryAuthorityInterface =
            InventoryAuthorityApiService().inventoryAuthorityClient().create(
                InventoryAuthorityInterface::class.java)
        val call: retrofit2.Call<InventoryAuthorityApiCallback> = client.getInventoryAuthority(inventoryListRequest)
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
                for (i in 0 until settingList.size) {
                    val settingListInput = settingList[i]
                    if(settingListInput.scope == getPassScope()) {
                        authorityDataList.add(settingListInput.authorityDeptList.toMutableList())
                    }
                }
                for (i in 0 until authorityDataList.size) {
                    val authorityDeptListInput = authorityDataList[i]
                    for (q in 0 until authorityDeptListInput.size) {
                        val authorityDataInput = AuthorityInformation()
                        authorityDataInput.count = authorityDeptListInput[q].count
                        authorityDataInput.id = authorityDeptListInput[q].id.toString()
                        authorityDataInput.deptEnName =
                            authorityDeptListInput[q].deptEnName.toString()
                        authorityDataInput.deptName = authorityDeptListInput[q].deptName.toString()
                        val checkData  = DataBase.getInstance(applicationContext)!!.getDataUao().findDataByDeptId(authorityDataInput.id)
                       if(checkData != null){
                           authorityDataSet.add(authorityDataInput)
                       }else{
                           println("add nothing")
                       }
                    }
                }
                adapter = DeptSelectAdapter(applicationContext,this@DeptSelectActivity ,
                    authorityDataSet, getPassScope().toString(), getPassPhase().toString()
                )
                recyclerView!!.layoutManager = LinearLayoutManager(this@DeptSelectActivity)
                recyclerView!!.adapter = adapter
            }
            override fun onFailure(
                call: retrofit2.Call<InventoryAuthorityApiCallback>?,
                t: Throwable?
            ) {
                println("------------------- ${t.toString()}")
            }
        })
    }
    fun upLoadData(view:View?) {
        if (getErrorMessage() != null){
            deleteDataToReference("errorMessage")
        }
        val uploadList = mutableListOf<UploadModel>()
        val dataList = DataBase.getInstance(this@DeptSelectActivity)!!.getDataUao().findDataById()
        dataList!!.forEach {
            val uploadData = UploadModel(
                it.id,
                it.inventoryLabelStatus,
                it.inventoryStatus,
                it.note,
                getInventorID()
            )
            uploadList.add(uploadData)
            DataBase.getInstance(this@DeptSelectActivity)!!.getDataUao().updateLabel(it.assetId)
        }
        val uploadDataRequest = UploadDataCallModel(uploadList)
        val client: UploadDataInterface =
            UploadDataApiService().uploadDataAuthorityClient().create(
                UploadDataInterface::class.java)
        val call: retrofit2.Call<UploadApICallback> = client.getUploadData(uploadDataRequest)
        call.enqueue(object : retrofit2.Callback<UploadApICallback> {
            override fun onResponse(
                call: retrofit2.Call<UploadApICallback>,
                response: retrofit2.Response<UploadApICallback>,
            ) {
                val uploadResponse: UploadResponse? =
                    response.body()!!.respData?.let {
                        Json.decodeFromString(it)
                    }
                val errorList = mutableListOf<String>()
                val errInventoryList = uploadResponse!!.errInventoryList.toMutableList()
                if (errInventoryList.isEmpty()){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@DeptSelectActivity)
                  val alertDialogSuccess: AlertDialog = builder.create()
                  alertDialogSuccess.setCancelable(true)
                  builder.setTitle("")
                  builder.setMessage(getString(R.string.檔案上傳成功))
                  builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
                      alertDialogSuccess.dismiss()
                  }
                  builder.setCancelable(true)
                  builder.show()
                }else{
                    errInventoryList.forEach {
                        errorList.add("${DataBase.getInstance(this@DeptSelectActivity)!!.getDataUao().findDataByItemId(it.id).assetId}\n${it.errMsg}")
                    }
                    saveErrorMessage(errorList.toString())
                    getButton().text = "${errorList.size}\n${"ERROR"}"
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@DeptSelectActivity)
                    val alertDialogError: AlertDialog = builder.create()
                    alertDialogError.setCancelable(true)
                    builder.setTitle(getString(R.string.以下為未能成功上傳的資料))
                    builder.setItems(errorList.toTypedArray()) { _, which ->
                        val nameOfError = errorList[which]
                        Toast.makeText(this@DeptSelectActivity, nameOfError, Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
                        alertDialogError.dismiss()
                    }
                    builder.setCancelable(true)
                    builder.show()
                }
                adapter!!.notifyDataSetChanged()
            }
            override fun onFailure(
                call: retrofit2.Call<UploadApICallback>?,
                t: Throwable?
            ) {
                println("------------------- ${t.toString()}")
            }
        })
    }
    override fun onBackPressed() {
        val loading = ProgressionBar(this@DeptSelectActivity)
        loading.startLoading()
        val i = Intent(this, UserDownloadActivity::class.java)
        startActivity(i)
        finish()

    }
}
