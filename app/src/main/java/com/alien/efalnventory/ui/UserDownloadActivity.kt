package com.alien.efaInventory.ui



import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.adapters.UserDownloadAdapter
import com.alien.efaInventory.R
import com.alien.efaInventory.interfaces.DataDownloadInterface
import com.facebook.stetho.Stetho
import okhttp3.*
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityApiService
import com.alien.efaInventory.apiSetting.inventoryAuthorityApi.InventoryAuthorityInterface
import com.alien.efaInventory.dataModel.*
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.InventoryAuthorityApiCallback
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.InventoryAuthorityCallModel
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.InventoryAuthorityResponse
import com.alien.efaInventory.dataModel.inventoryAuthorityApiModel.SettingList
import com.alien.efaInventory.dataModel.localDataProcessModel.DownloadData
import com.alien.efaInventory.entity.*
import kotlinx.android.synthetic.main.userdownloaditem_layout.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.util.AlertDialogHelper
import com.alien.efaInventory.util.ProgressionBar
import kotlinx.android.synthetic.main.activity_userdownload.*
import java.util.*
import kotlin.concurrent.schedule


class UserDownloadActivity : Activity(), DataDownloadInterface {

    private var recyclerView: RecyclerView? = null
    private var adapter: UserDownloadAdapter? = null
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdownload)
        Stetho.initializeWithDefaults(this)//設置資料庫監視
            configUI()
            checkUser()
        recyclerView = findViewById<View>(R.id.recycler_view1) as RecyclerView
            val inventoryAuthorityRequest =
                InventoryAuthorityCallModel(loginId = getUserNameFromPreference(),
                    company = getCompanyCodeFromPreference())
            val client: InventoryAuthorityInterface =
                InventoryAuthorityApiService().inventoryAuthorityClient().create(
                    InventoryAuthorityInterface::class.java
                )
            val call: retrofit2.Call<InventoryAuthorityApiCallback> =
                client.getInventoryAuthority(inventoryAuthorityRequest)
            call.enqueue(object : retrofit2.Callback<InventoryAuthorityApiCallback> {
                override fun onResponse(
                    call: retrofit2.Call<InventoryAuthorityApiCallback>,
                    response: retrofit2.Response<InventoryAuthorityApiCallback>,
                ) {
                    val inventoryAuthorityResponse: InventoryAuthorityResponse? =
                        response.body()!!.respData?.let {
                            Json.decodeFromString(it)
                        }
                    inventoryAuthorityResponse?.let { handleInventoryRelatedData(it) }
                    val downloadDataList = mutableListOf<DownloadData>()
                    inventoryAuthorityResponse!!.settingList.forEach {

                        downloadDataList.add(getDownloadDataFromSettingList(it))
                    }
                    if (downloadDataList.size == 0) {

                        showUnAuthorityAlert()
                    }
                    adapter = UserDownloadAdapter(this@UserDownloadActivity, downloadDataList)
                    recyclerView!!.layoutManager = LinearLayoutManager(this@UserDownloadActivity)
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
    override fun downloadInventoryData(download:DownloadData) {
        println(download.scope)
        toDownloadSelect(download)
    }
    override fun takeInventory(download: DownloadData) {
        Stetho.initializeWithDefaults(this)
        val inventory: MutableList<InventoryDatabaseModel>? =
            DataBase.getInstance(this)?.getDataUao()?.displayAll()

        if (inventory?.isEmpty() == true) {

            showUnDownloadDataAlert()

        } else {

            val loading = ProgressionBar(this@UserDownloadActivity)
            loading.startLoading()
            toDeptSelect(download)
        }
    }
    private fun handleInventoryRelatedData(response: InventoryAuthorityResponse) {
        handleInventoryLabelData(response)
        handleInventoryStatusAdminData(response)
        handleInventoryStatusMISData(response)
    }
    private fun handleInventoryLabelData(response: InventoryAuthorityResponse) {
        val inventoryLabelList: MutableList<InventoryLabelData> = mutableListOf()
        response.inventoryLabelStatusList.forEach {

            val inventoryLabelData = InventoryLabelData(
                id = null,
                labelKey = it.key.toString(),
                labelValue = it.value.toString()
            )
            inventoryLabelList.add(inventoryLabelData)
        }

        saveInventoryLabelToDB(inventoryLabelList)
    }
    private fun saveInventoryLabelToDB(inventoryLabelDataList: List<InventoryLabelData>) {
        Thread {

            DataBase.getInstance(this)!!
                .getInventoryLabelDao()
                .insertInventoryLabels(*inventoryLabelDataList.toTypedArray())
        }.start()
    }
    private fun handleInventoryStatusAdminData(response: InventoryAuthorityResponse) {

        val inventoryStatusAdminList: MutableList<InventoryStatusAdminData> = mutableListOf()
        response.inventoryStatusAdministrationList.forEach {
            val inventoryAdminData = InventoryStatusAdminData(
                id = null,
                statusKey = it.key.toString(),
                statusValue = it.value.toString()
            )
            inventoryStatusAdminList.add(inventoryAdminData)
        }
        saveInventoryStatusAdminToDB(inventoryStatusAdminList)
    }

    private fun saveInventoryStatusAdminToDB(inventoryStatusAdminDataList: List<InventoryStatusAdminData>) {
        Thread {
            DataBase.getInstance(this)!!
                .getInventoryStatusAdminDao()
                .insertInventoryStatuses(*inventoryStatusAdminDataList.toTypedArray())
        }.start()
    }

    private fun handleInventoryStatusMISData(response: InventoryAuthorityResponse) {

        val inventoryStatusMISList: MutableList<InventoryStatusMISData> = mutableListOf()
        response.inventoryStatusMISList.forEach {

            val inventoryMISData = InventoryStatusMISData(
                id = null,
                statusKey = it.key.toString(),
                statusValue = it.value.toString()
            )

            inventoryStatusMISList.add(inventoryMISData)
        }

        saveInventoryStatusMISToDB(inventoryStatusMISList)
    }

    private fun saveInventoryStatusMISToDB(inventoryStatusMISDataList: List<InventoryStatusMISData>) {
        Thread {
            DataBase.getInstance(this)!!
                .getInventoryStatusMISDao()
                .insertInventoryStatuses(*inventoryStatusMISDataList.toTypedArray())
        }.start()
    }
    private fun getUserNameTextView(): TextView {
        return getTextViewById(R.id.txtUserName)
    }
    private fun getCompanyShowTextView(): TextView {
        return getTextViewById(R.id.txtCompanyShow)
    }
    private fun getPreferences(): SharedPreferences {
        return this.getSharedPreferences("Value", Context.MODE_PRIVATE)
    }
    private fun configUI() {
        getUserNameTextView().text = getDisplayNameFromPreference()
        getCompanyShowTextView().text = getCompanyFromPreference()
    }
    private fun getTextViewById(id: Int): TextView {
        return findViewById(id)
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
    private fun getCompanyCodeFromPreference(): String {
        return getCompanyFromPreference().take(4)
    }

    private fun getDataFromPreference(key: String): String {
        return getPreferences().getString(key, String()).toString()
    }
    override fun onBackPressed() {
        if(DataBase.getInstance(this@UserDownloadActivity)?.getDataUao()?.getDataNotUpdateCount() == 0){
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()}else{
            updateCheckAlert()
        }
    }

    private fun getDownloadDataFromSettingList(setting: SettingList): DownloadData {

        val downloadData = DownloadData()

        downloadData.id = setting.id.toString()
        downloadData.scope = setting.scope.toString()
        downloadData.phase = setting.phase.toString()
        downloadData.startDate = setting.startDate.toString()
        downloadData.endDate = setting.endDate.toString()
        downloadData.authorityList = setting.authorityDeptList

        return downloadData
    }
    private fun updateCheckAlert() {
        showAlert(getString(R.string.尚有未傳資料))
    }
    private fun showUnAuthorityAlert() {
        showAlert(getString(R.string.無此權限))
        Timer().schedule(500) {
            onBackPressed()
        }
    }

    private fun showUnDownloadDataAlert() {

        showAlert(getString(R.string.資料未下載))
    }

    private fun showAlert(title: String) {

        AlertDialogHelper(this).showAlertDialog(title)
    }


    private fun deletePreviousUser(){
        DataBase.getInstance(this@UserDownloadActivity)?.getDataUao()?.nukePreviousUserTable()
    }
    private fun insertPreviousUserData(){
        val insertion = PreviousUserSaveModel(
            id = 1,
            previousUserInventorName = getUserNameFromPreference()
        )
        DataBase.getInstance(this@UserDownloadActivity)?.getDataUao()?.insertPreviousUserData(insertion)
    }
    private fun findPreviousUserById(): String? {
        val model = DataBase.getInstance(this@UserDownloadActivity)?.getDataUao()?.findPreviousUserById(1)
        return model?.previousUserInventorName
    }
    private fun checkUser(){
        when {
            findPreviousUserById() == getUserNameFromPreference() -> { println( findPreviousUserById() )}
            findPreviousUserById() == null -> {println( findPreviousUserById() )}
            else -> {
                checkingAlertDialog()
            }
        }
        }

    private fun noneUpdateCount(): Int? {
        return DataBase.getInstance(this@UserDownloadActivity)?.getDataUao()?.getDataNotUpdateCount()

    }
    private fun resetUser(){
        deletePreviousUser()
        insertPreviousUserData()
    }


    private fun checkingAlertDialog() {
        if (noneUpdateCount() != null){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@UserDownloadActivity)
        val checkAlertDialog: AlertDialog = builder.create()
        checkAlertDialog.setCancelable(true)
        builder.setTitle(getString(R.string.提示))
        builder.setMessage(getString(R.string.前一位登入同仁) + findPreviousUserById() + getString(R.string.尚有未上傳資產) + noneUpdateCount() + getString(
                    R.string.筆如直接下載則會清空前一位同仁紀錄或通知同仁進行同步作業))
        builder.setPositiveButton(getString(R.string.直接下載)) { _, _ ->
            deleteAll()
            resetUser()

        }
        builder.setNegativeButton(getString(R.string.登出通知同仁)) { _, _ ->
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        builder.setCancelable(true)
        builder.show()}
    }
    private fun deleteAll(){
        Thread{
            DataBase.getInstance(this)!!.getDataUao().nukeTable()
        }.start()
    }






    private fun toDeptSelect(download: DownloadData) {

        val btnInventory = findViewById<View>(R.id.buttonTakeInventory) as Button
        val textScope = download.scope
        val textPhase = download.phase


        val intent = Intent(btnInventory.context, DeptSelectActivity::class.java)
        intent.putExtra("passScopeDept", textScope)
        intent.putExtra("passPhase", textPhase)
        startActivity(intent)
    }

    private fun toDownloadSelect(download: DownloadData) {

        val btnDownload = findViewById<View>(R.id.buttonDownload) as Button
        val textId = download.id
        val textScope = download.scope
        resetUser()
        val intent = Intent(btnDownload.context, DownloadSelectActivity::class.java)
        intent.putExtra("pass", textId)
        intent.putExtra("passScope", textScope)
        btnDownload.context.startActivity(intent)
    }
}
