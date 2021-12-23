package com.alien.efaInventory.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.R
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.entity.InventoryDatabaseModel


class DatabaseAdapter(private val activity:Activity, val context: Context, private val passPhase:String) : RecyclerView.Adapter<DatabaseAdapter.ViewHolder>() {
    private var inventoryArrayList:MutableList<InventoryDatabaseModel> = ArrayList()
    lateinit var onItemClick: OnItemClickListener
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val assetId: TextView = itemView.findViewById(R.id.assetId)
        val spec: TextView = itemView.findViewById(R.id.specShow)
        val nameShow: TextView = itemView.findViewById(R.id.nameShow)
        val productName: TextView = itemView.findViewById(R.id.productNameShow)
        val inventoryLabel: TextView = itemView.findViewById(R.id.inventoryLabelShow)
        val inventoryStatus: TextView = itemView.findViewById(R.id.inventoryStatusShow)
        val phase: TextView = itemView.findViewById(R.id.passPhase)
        val count: TextView = itemView.findViewById(R.id.countInsert)
    }
    //顯示
    fun showDeptData(deptId:String){
        Thread{
            inventoryArrayList = DataBase.getInstance(activity)!!.getDataUao().displayDept(deptId)!!
            activity.runOnUiThread { notifyDataSetChanged() }
        }.start()
    }
    fun updateDataSpinner(assetId: String,
                          inventoryStatus: String,
                          note: String,
                          inventoryLabelStatus: String,
                          inventoryStage: Boolean,
                          deptId:String
                   ){
        Thread{
            DataBase.getInstance(activity)!!.getDataUao().updateDataSpinner(assetId,inventoryStatus,note,inventoryLabelStatus,inventoryStage)
            activity.runOnUiThread {
                showDeptData(deptId)            }
        }.start()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.inventorydatabase_layout,parent,false))
    }
    override fun getItemCount(): Int {
        return if (inventoryArrayList.size > 10){
            10
        }else{
            inventoryArrayList.size
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.assetId.text = inventoryArrayList[position].assetId
        holder.spec.text = inventoryArrayList[position].spec
        holder.nameShow.text = inventoryArrayList[position].keeperName
        holder.productName.text = inventoryArrayList[position].productName
        if(inventoryArrayList[position].primaryPhaseInventoryLabelStatus == "")
        { holder.inventoryLabel.text = context.getString(R.string.無資料)}else{
        holder.inventoryLabel.text = inventoryArrayList[position].primaryPhaseInventoryLabelStatus}
        if(inventoryArrayList[position].primaryPhaseInventoryStatus == "")
        {holder.inventoryStatus.text = context.getString(R.string.無資料)}else{
        holder.inventoryStatus.text = inventoryArrayList[position].primaryPhaseInventoryStatus}
        holder.count.text= inventoryArrayList[position].quantity.toString()
        holder.phase.text = context.getString(R.string.初)
        holder.itemView.setOnClickListener{ onItemClick.onItemClick(inventoryArrayList[position]) }
    }
    interface OnItemClickListener {
        fun onItemClick(myData: InventoryDatabaseModel)
    }
}
