package com.alien.efaInventory.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.util.*

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.entity.InventoryDatabaseModel
import com.alien.efaInventory.R
import com.alien.efaInventory.adapters.DatabaseAdapter
import com.alien.efaInventory.adapters.InventoryAdapter
import com.alien.efaInventory.roomDatabase.DataBase
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_detailinformation.*
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.activity_login.*
import android.content.IntentFilter
import android.content.BroadcastReceiver
import android.content.Context
import com.alien.efaInventory.util.ProgressionBar
import kotlinx.android.synthetic.main.inventorydatabase_layout.*


class InventoryActivity : AppCompatActivity(){

    private var inventoryDataArray: ArrayList<InventoryDatabaseModel> = arrayListOf()
    var adapter = InventoryAdapter(this, inventoryDataArray,arrayListOf(),arrayListOf(), String(),String(),String())
    private var dataBaseAdapter: DatabaseAdapter? = null
    var nowSelectedData: InventoryDatabaseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        Stetho.initializeWithDefaults(this)//設置資料庫監視
        closeListView()
        listView?.adapter = adapter
        configUI()
        adapter = InventoryAdapter(this, inventoryDataArray, statusSpinnerDataInsertion(),labelSpinnerDataInsertion(),
            getPassPhase().toString(),getPassDeptName().toString(),getPassScope().toString())
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()
        //設置顯示data的recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dataBaseAdapter = DatabaseAdapter(this,applicationContext, getPassPhase().toString())
        recyclerView.adapter = dataBaseAdapter
        if (DataBase.getInstance(this)!!.getDataUao().displayDept(getPassDeptId().toString())!!.isEmpty()){
            closeListView()
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val ad: AlertDialog = builder.create()
            ad.setCancelable(true)
            builder.setTitle(getString(R.string.已盤點完成))
                .setMessage(getString(R.string.本資料組已盤點完成))
            builder.setPositiveButton(getString(R.string.重新確認)+ "\n" +getString(R.string.盤點資料)) { _, _ ->
                ad.dismiss()}
            builder.setNegativeButton(getString(R.string.返回)+"\n"+getString(R.string.部門選擇)) { _, _ ->
                ad.dismiss()
                val intent = Intent(this, DeptSelectActivity::class.java)
                passData(intent)
            }
            builder.setCancelable(true)
            builder.show()
        }
        //顯示目前的資料
        dataBaseAdapter!!.showDeptData(getPassDeptId().toString())
        //設置點擊
        val i = Intent(this, DetailInformationActivity::class.java)
        dataBaseAdapter!!.onItemClick = object : DatabaseAdapter.OnItemClickListener {
            override fun onItemClick(myData: InventoryDatabaseModel) {
                nowSelectedData = myData
                i.putExtra("assetId", nowSelectedData!!.assetId)
                passData(i)
            }
        }
        val searchView = findViewById<View>(R.id.searchView) as SearchView
        searchView.queryHint= getString(R.string.資產編號)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val retrievedDataBySearch = DataBase.getInstance(applicationContext)!!.getDataUao().findDataByName(
                    query!!,getPassDeptId()!!)
                if(retrievedDataBySearch != null){
                    if(retrievedDataBySearch.primaryPhaseInventoryStatus == ""){
                        showListView()
                    val searchInventoryLabelStatus = "OK"
                    val searchInventoryStatus: String = if( getPassScope() == "ADMINISTRATION" ){
                        "ADMINISTRATION_GOOD"
                    }else{
                        "MIS_IN_USE_WITHIN_COMPANY"
                    }
                        getDataByQuery(query,retrievedDataBySearch,searchInventoryLabelStatus,searchInventoryStatus)
                    }else{
                        showListView()
                        val searchInventoryStatus = retrievedDataBySearch.inventoryStatus
                        val searchInventoryLabelStatus = retrievedDataBySearch.inventoryLabelStatus
                        getDataByQuery(query,retrievedDataBySearch,searchInventoryLabelStatus,searchInventoryStatus)
                    }
                }else{
                    dataUnfoundedDialog()
                }
                adapter.notifyDataSetChanged()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        val filter = IntentFilter()
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        filter.addAction("com.kotlin.SCAN")
        registerReceiver(myBroadcastReceiver, filter)
    }
    private val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action =="com.kotlin.SCAN") {
                displayScanResult(intent)
            }
        }
    }

    private fun labelSpinnerDataInsertion(): MutableList<String> {
        val spinnerOptions: MutableList<String> = mutableListOf()
        spinnerOptions.add(getString(R.string.盤點結果))
        val labelKeyValueData = DataBase.getInstance(this@InventoryActivity)!!.getInventoryLabelDao().displayLabelKey()
        labelKeyValueData!!.forEach {
                spinnerOptions.add(it.labelValue)
            while(spinnerOptions.size > 50){
                break
            }
        }
        val spinnerOptionsUnique = spinnerOptions.distinct()
        return spinnerOptionsUnique.toMutableList()
    }

    private fun statusSpinnerDataInsertion(): MutableList<String> {
        val spinnerOptions: MutableList<String> = mutableListOf()
        spinnerOptions.add(getString(R.string.資產狀態))
        val keyValueMISData =
            DataBase.getInstance(this@InventoryActivity)!!.getInventoryStatusMISDao()
                .displayMISKey()
        val keyValueAdminData =
            DataBase.getInstance(this@InventoryActivity)!!.getInventoryStatusAdminDao()
                .displayAdminKey()
         if (getPassScope() == "ADMINISTRATION") {
            keyValueAdminData!!.forEach {
                spinnerOptions.add(it.statusValue)
                while(spinnerOptions.size > 50){
                    break
                }
            }
             val spinnerOptionsUnique = spinnerOptions.distinct()
             return spinnerOptionsUnique.toMutableList()
        } else {
            keyValueMISData!!.forEach {
                spinnerOptions.add(it.statusValue)
                while(spinnerOptions.size > 50){
                    break
                }
            }
             val spinnerOptionsUnique = spinnerOptions.distinct()
             return spinnerOptionsUnique.toMutableList()
        }
    }

    private fun configUI() {
        getTxtDeptId().text = getPassDeptId()
        getTxtDeptName().text = getPassDeptName()
    }
    private fun closeListView(){
        listView.visibility = View.GONE
        inventorydata.visibility = View.GONE
    }
    private fun showListView(){
        listView.visibility = View.VISIBLE
        inventorydata.visibility = View.VISIBLE
    }
    private fun closeRecyclerView(){
        noninventory.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }
    private fun getTxtDeptId(): TextView {
        return getTextViewById(R.id.deptnamenumber)
    }
    private fun getTxtDeptName(): TextView {
        return getTextViewById(R.id.deptname)
    }
    private fun getTextViewById(id: Int): TextView {
        return findViewById(id)
    }
    private fun getPassScope(): String? {
        return intent.getStringExtra("passScope")
    }
    private fun getPassDeptId(): String? {
        return intent.getStringExtra("passText")
    }
    private fun getPassDeptName(): String? {
        return intent.getStringExtra("passText2")
    }
    private fun getPassPhase(): String? {
        return intent.getStringExtra("passPhase")
    }
    private fun passData(intent:Intent){
        intent.putExtra("passScopeDept",getPassScope())
        intent.putExtra("passPhase",getPassPhase())
        intent.putExtra("passDeptId",getPassDeptId())
        intent.putExtra("passDeptName",getPassDeptName())
        startActivity(intent)
        finish()
    }
    private fun dataUnfoundedDialog(){
        inventoryDataArray.clear()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@InventoryActivity)
        val adUF: AlertDialog = builder.create()
        adUF.setCancelable(true)
        builder.setTitle(getString(R.string.並無此筆資料))
            .setMessage(getString(R.string.並無此筆資料))
        builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
            adUF.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }
    private fun displayScanResult(initiatingIntent: Intent) {
        val scanData = initiatingIntent.getStringExtra("com.symbol.datawedge.data_string")
        val passDeptId = intent.getStringExtra("passText")
        val retrievedData = DataBase.getInstance(applicationContext)!!.getDataUao().findDataByName(scanData!!,passDeptId!!)
        if(retrievedData != null ){
            if(retrievedData.inventoryLabelStatus != ""){
                showListView()
                val scanInventoryLabelStatus = retrievedData.inventoryLabelStatus
                val scanInventoryStatus = retrievedData.inventoryStatus
                getDataByQuery(scanData,retrievedData,scanInventoryLabelStatus,scanInventoryStatus)
            }else{
                showListView()
            val scanInventoryLabelStatus = "OK"
            val scanInventoryStatus: String = if( getPassScope() == "ADMINISTRATION" ){
                "ADMINISTRATION_GOOD"
            }else{
                "MIS_IN_USE_WITHIN_COMPANY"
            }
                getDataByQuery(scanData,retrievedData,scanInventoryLabelStatus,scanInventoryStatus)
          }
        }
        else{
            dataUnfoundedDialog()
        }
        adapter.notifyDataSetChanged()
    }
    private fun getDataByQuery(query:String, retrievedData: InventoryDatabaseModel, LabelStatus: String, Status:String){
        val insertModel = InventoryDatabaseModel(
            query,
            retrievedData.costCorner,
            retrievedData.quantity,
            retrievedData.storageId,
            retrievedData.storageDescription,
            retrievedData.keeperId,
            retrievedData.group3,
            retrievedData.group2,
            retrievedData.deptId,
            retrievedData.productName,
            retrievedData.spec,
            Status,
            retrievedData.note,
            LabelStatus,
            retrievedData.partNo,
            retrievedData.updateStatus,
            retrievedData.keeperName,
            retrievedData.primaryPhaseInventoryLabelStatus,
            retrievedData.primaryPhaseInventoryStatus,
            true
        )
        inventoryDataArray.clear()
        inventoryDataArray.add(0, insertModel)
        dataBaseAdapter!!.updateDataSpinner(
            query,
            Status,
            retrievedData.note,
            LabelStatus,
            true,
            retrievedData.deptId
        )
        Stetho.initializeWithDefaults(this@InventoryActivity)
        val dataCheck =DataBase.getInstance(this@InventoryActivity)!!.getDataUao()
            .displayDept(retrievedData.deptId)
        if (dataCheck!!.isEmpty()) {
            closeRecyclerView()
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@InventoryActivity)
            val ad2: AlertDialog = builder.create()
            ad2.setCancelable(true)
            builder.setMessage(getString(R.string.已無未盤點資料))
            builder.setNegativeButton(getString(R.string.確認)) { _, _ ->
                ad2.dismiss()
            }
            builder.setCancelable(true)
            builder.show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadcastReceiver)
    }
    override fun onBackPressed() {
        val intent = Intent(this, DeptSelectActivity::class.java)
        intent.putExtra("passScopeDept",getPassScope())
        intent.putExtra("passPhase",getPassPhase())
        val loading = ProgressionBar(this@InventoryActivity)
        loading.startLoading()
        startActivity(intent)
        finish()
    }
}

