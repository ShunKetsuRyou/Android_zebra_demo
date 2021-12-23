package com.alien.efaInventory.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.alien.efaInventory.R
import com.alien.efaInventory.entity.InventoryDatabaseModel
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.ui.DetailInformationActivity


//  Adapter to map Scan objects to the list in the MainActivity
class InventoryAdapter(private var activity: Activity, private var items: ArrayList<InventoryDatabaseModel>, private var spinnerItem1: List<String>, private var spinnerItem2: List<String>, private var passPhase: String, private var passDeptName: String, private var passScope: String): BaseAdapter()
{
    class ViewHolder(row: View?)
    {
        var txtScan: TextView? = null
        var txtProductNameInsert: TextView? = null
        var txtSpecInsert: TextView? = null
        var txtNameInsert: TextView? = null
        var txtInventoryStatus: TextView? = null
        var txtInventoryLabel: TextView? = null
        var txtLabelSpinner: Spinner? = null
        var txtStatusSpinner: Spinner? = null
        var btnChange: Button?  = null
        var countInsert:TextView? = null
        var passPhase:TextView? = null
        var note:EditText? = null

        init {
            this.txtScan = row?.findViewById(R.id.scanData)
            this.btnChange = row?.findViewById(R.id.updateBtn)
            this.txtProductNameInsert = row?.findViewById(R.id.productNameShow)
            this.txtSpecInsert = row?.findViewById(R.id.specInsert)
            this.txtNameInsert = row?.findViewById(R.id.nameInsert)
            this.txtInventoryStatus = row?.findViewById(R.id.inventoryStatusShow)
            this.txtInventoryLabel = row?.findViewById(R.id.inventoryLabelShow)
            this.txtLabelSpinner = row?.findViewById(R.id.inventoryLabelSpinner)
            this.txtStatusSpinner = row?.findViewById(R.id.inventoryStatusSpinner)
            this.countInsert = row?.findViewById(R.id.countInsert)
            this.passPhase = row?.findViewById(R.id.passPhase)
            this.note= row?.findViewById(R.id.note)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.inventoryitem_layout, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinnerItem1)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        viewHolder.txtStatusSpinner!!.adapter = arrayAdapter

        val arrayAdapter2: ArrayAdapter<String> =
            ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinnerItem2)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewHolder.txtLabelSpinner!!.adapter = arrayAdapter2



        val currentScan = items[position]
        viewHolder.txtScan?.text =currentScan.assetId
        viewHolder.txtProductNameInsert?.text = currentScan.productName
        viewHolder.txtSpecInsert?.text = currentScan.spec
        viewHolder.txtNameInsert?.text = currentScan.keeperName
        viewHolder.passPhase?.text = passPhase
        if(passScope == "ADMINISTRATION"){
            if(DataBase.getInstance(activity)?.getInventoryStatusAdminDao()?.getValue(currentScan.primaryPhaseInventoryStatus)?.statusValue != null
            )
        viewHolder.txtInventoryStatus?.text =
            DataBase.getInstance(activity)?.getInventoryStatusAdminDao()?.getValue(currentScan.primaryPhaseInventoryStatus)?.statusValue
            else
                viewHolder.txtInventoryStatus?.text = activity.getString(R.string.無資料)
        }else{
            if(DataBase.getInstance(activity)?.getInventoryStatusAdminDao()?.getValue(currentScan.primaryPhaseInventoryStatus)?.statusValue != null
            )
                viewHolder.txtInventoryStatus?.text =
                    DataBase.getInstance(activity)?.getInventoryStatusAdminDao()?.getValue(currentScan.primaryPhaseInventoryStatus)?.statusValue
            else
                viewHolder.txtInventoryStatus?.text = activity.getString(R.string.無資料)
            }
        if(DataBase.getInstance(activity)?.getInventoryLabelDao()?.getValue( currentScan.primaryPhaseInventoryLabelStatus)?.labelValue != null)
        { viewHolder.txtInventoryLabel?.text =DataBase.getInstance(activity)?.getInventoryLabelDao()?.getValue( currentScan.primaryPhaseInventoryLabelStatus)?.labelValue}
        else
        {viewHolder.txtInventoryLabel?.text = activity.getString(R.string.無資料)}

        viewHolder.countInsert?.text = currentScan.quantity.toString()

        viewHolder.note!!.onEditorAction(EditorInfo.IME_ACTION_DONE)

        viewHolder.note?.setText(currentScan.note)
        viewHolder.txtScan!!.setOnClickListener{
            val i = Intent(activity, DetailInformationActivity::class.java)
            i.putExtra("assetId", currentScan.assetId)
            i.putExtra("passScopeDept",passScope)
            i.putExtra("passPhase",passPhase)
            i.putExtra("passDeptId",currentScan.deptId)
            i.putExtra("passDeptName",passDeptName)
            activity.startActivity(i)
        }
        when (DataBase.getInstance(activity)?.getInventoryLabelDao()?.getValue( currentScan.inventoryLabelStatus)?.labelValue) {
            "" -> {
                viewHolder.txtLabelSpinner!!.setSelection(0)
            }
            "OK" -> {
                viewHolder.txtLabelSpinner!!.setSelection(1)
            }
            "無財編" -> {
                viewHolder.txtLabelSpinner!!.setSelection(2)
            }
            "財編磨損" -> {
                viewHolder.txtLabelSpinner!!.setSelection(3)
            }
            "財編與實物不符" -> {
                viewHolder.txtLabelSpinner!!.setSelection(4)
            }
            "無實物" -> {
                viewHolder.txtLabelSpinner!!.setSelection(5)
            }
            "其他" -> {
                viewHolder.txtLabelSpinner!!.setSelection(6)
            }
        }
        if (passScope == "ADMINISTRATION") {
            when (DataBase.getInstance(activity)?.getInventoryStatusAdminDao()
                ?.getValue(currentScan.inventoryStatus)?.statusValue) {
                "" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(0)
                }
                "正常使用" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(1)
                }
                "閒置良品" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(2)
                }
                "閒置不良(可維修再使用)" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(3)
                }
                "不良" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(4)
                }

            }
        }else {
            when (DataBase.getInstance(activity)?.getInventoryStatusMISDao()
                ?.getValue(currentScan.inventoryStatus)?.statusValue) {
                "" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(0)
                }
                "在公司使用中" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(1)
                }
                "在外使用中(需備註地點)" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(2)
                }
                "外修中" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(3)
                }
                "報廢" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(4)
                }
                "遺失" -> {
                    viewHolder.txtStatusSpinner!!.setSelection(4)
                }
            }
        }
        viewHolder.btnChange!!.setOnClickListener{
            if((viewHolder.txtLabelSpinner!!.selectedItem.toString() == activity.getString(R.string.盤點結果))or(viewHolder.txtStatusSpinner!!.selectedItem.toString() == activity.getString(R.string.資產狀態))){

                val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                val statusAlertDialog: AlertDialog = builder.create()
                statusAlertDialog.setCancelable(true)
                builder.setTitle(activity.getString(R.string.請確實選擇資產狀態以及盤點結果))

                builder.setNegativeButton(activity.getString(R.string.確認)) { _, _ ->
                    statusAlertDialog.dismiss()
                }
                builder.setCancelable(true)
                builder.show()

            }
            else{
                val labelKey = DataBase.getInstance(activity)!!.getInventoryLabelDao().getKey(
                    viewHolder.txtLabelSpinner!!.selectedItem.toString()
                ).labelKey
                val statusKey: String = if(passScope == "ADMINISTRATION"){
                    DataBase.getInstance(activity)!!.getInventoryStatusAdminDao().getKey(
                        viewHolder.txtStatusSpinner!!.selectedItem.toString()
                    ).statusKey
                }else{
                    DataBase.getInstance(activity)!!.getInventoryStatusMISDao().getKey(
                        viewHolder.txtStatusSpinner!!.selectedItem.toString()
                    ).statusKey
                }

                updateDataSpinner(
                    viewHolder.txtScan!!.text.toString(),
                    statusKey,
                    viewHolder.note!!.text.toString(),
                    labelKey,
                    true
                )
            }
        }
        return view as View
    }
    override fun getItem(i: Int): InventoryDatabaseModel {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
    private fun updateDataSpinner(
        assetId: String,
        inventoryStatus: String,
        note: String,
        inventoryLabelStatus: String,
        inventoryStage: Boolean
    ){
        Thread{
            DataBase.getInstance(activity)!!.getDataUao().updateDataSpinner(assetId,inventoryStatus,note,inventoryLabelStatus,inventoryStage)
        }.start()

    }


    }


