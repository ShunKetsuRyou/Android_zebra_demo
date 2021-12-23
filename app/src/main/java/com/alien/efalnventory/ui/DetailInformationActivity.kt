package com.alien.efaInventory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.alien.efaInventory.R
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.util.ProgressionBar


class DetailInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailinformation)
        configUI()
    }
    private fun getTextViewById(id: Int): TextView {
        return findViewById(id)
    }
    private fun configUI(){
        val intent = intent
        val assetId = intent.getStringExtra("assetId")
        val retrievedData = DataBase.getInstance(applicationContext)!!.getDataUao().findDataByAssetId(assetId!!)
        getTextViewById(R.id.scan).text = intent.getStringExtra("assetId")
        getTextViewById(R.id.productName).text = retrievedData.productName
        getTextViewById(R.id.spec).text = retrievedData.spec
        getTextViewById(R.id.partNo).text = retrievedData.partNo
        getTextViewById(R.id.group2).text = retrievedData.group2
        getTextViewById(R.id.group3).text = retrievedData.group3
        getTextViewById(R.id.deptName).text = retrievedData.keeperName
        getTextViewById(R.id.costCorner).text = retrievedData.costCorner
    }
    private fun getPassScope(): String? {
        return intent.getStringExtra("passScopeDept")
    }
    private fun getPassDeptId(): String? {
        return intent.getStringExtra("passDeptId")
    }
    private fun getPassDeptName(): String? {
        return intent.getStringExtra("passDeptName")
    }
    private fun getPassPhase(): String? {
        return intent.getStringExtra("passPhase")
    }
    private fun passData(intent:Intent){
        intent.putExtra("passScope",getPassScope())
        intent.putExtra("passPhase",getPassPhase())
        intent.putExtra("passText",getPassDeptId())
        intent.putExtra("passText2",getPassDeptName())
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        val i = Intent(this, InventoryActivity::class.java)
        passData(i)
    }
}
