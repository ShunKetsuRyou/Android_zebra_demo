package com.alien.efaInventory.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.R
import com.alien.efaInventory.dataModel.localDataProcessModel.AuthorityInformation
import com.alien.efaInventory.roomDatabase.DataBase
import com.alien.efaInventory.ui.DeptSelectActivity
import com.alien.efaInventory.ui.InventoryActivity
import com.alien.efaInventory.util.ProgressionBar
import java.util.*
import kotlin.collections.ArrayList



class DeptSelectAdapter(var context: Context, private val activity: DeptSelectActivity, authorityInformationList: MutableList<AuthorityInformation>, var passScope: String, var passPhase: String) :
    RecyclerView.Adapter<DeptSelectAdapter.DeptViewHolder>(), Filterable {
    var authorityInformationListFiltered: MutableList<AuthorityInformation>


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeptViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.deptselectitem_layout, viewGroup, false)
        return DeptViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: DeptViewHolder, position: Int) {

        viewHolder.deptName.text = authorityInformationListFiltered[position].deptName
        viewHolder.deptId.text = authorityInformationListFiltered[position].id
        viewHolder.totalCount.text = getDataCountTotal(authorityInformationListFiltered[position].id).toString()

        viewHolder.noneInventoryCount.text = getDataCountNone(authorityInformationListFiltered[position].id).toString()
        viewHolder.inventoryCount.text = getDataInventoryCount(authorityInformationListFiltered[position].id).toString()
        viewHolder.uploadCount.text = getDataCountUpdated(authorityInformationListFiltered[position].id).toString()

        val textDeptIdInput = viewHolder.deptId.text
        val custodianList =DataBase.getInstance(activity)!!.getDataUao().findListByDeptId(textDeptIdInput.toString())
        val custodianDemoList = mutableListOf<String>()
        custodianList.forEach {
            custodianDemoList.add("${it.loginId}(${it.inventorName})\n# ${it.tel}")
        }
        val custodianDemoListUnique = custodianDemoList.distinct()
        if(custodianDemoListUnique.isNotEmpty()){
        viewHolder.custodianDemo.text = custodianDemoListUnique[0]}else{ viewHolder.custodianDemo.text=""}


    }
    private fun getDataCountTotal(deptId: String): Int {
        return DataBase.getInstance(activity)!!.getDataUao().getDataCountTotal(deptId)
    }
    private fun getDataCountNone(deptId: String): Int {
        return DataBase.getInstance(activity)!!.getDataUao().getDataCountNone(deptId)
    }
    private fun getDataInventoryCount(deptId: String): Int {
        return DataBase.getInstance(activity)!!.getDataUao().getDataInventoryCount(deptId)
    }
    private fun getDataCountUpdated(deptId: String): Int {
        return DataBase.getInstance(activity)!!.getDataUao().getDataCountUpdated(deptId)
    }
    override fun getItemCount(): Int {
        return authorityInformationListFiltered.size
    }

    inner class DeptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var deptName: TextView = itemView.findViewById(R.id.deptName)
        var deptId: TextView = itemView.findViewById(R.id.deptId)
        var totalCount:TextView = itemView.findViewById(R.id.totalCount)
        var noneInventoryCount:TextView = itemView.findViewById(R.id.noneInventoryCount)
        var inventoryCount:TextView = itemView.findViewById(R.id.inventoryCount)
        var uploadCount:TextView = itemView.findViewById(R.id.uploadCount)
        var custodianDemo: TextView = itemView.findViewById(R.id.custodianDemo)

        private var custodian: TextView = itemView.findViewById(R.id.custodianList)
        init {

            custodian.setOnClickListener{
                val textDeptIdInput = deptId.text
                val custodianInfoList =DataBase.getInstance(activity)!!.getDataUao().findListByDeptId(textDeptIdInput.toString())
                val contactList = mutableListOf<String>()
                custodianInfoList.forEach {
                    contactList.add("${it.loginId}(${it.inventorName})\n# ${it.tel}")
                }
                val contactListUnique = contactList.distinct()
                val adBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                val contactAlertDialog: AlertDialog = adBuilder.create()
                adBuilder.setTitle(context.getString(R.string.部門窗口))
                contactAlertDialog.setCancelable(true)
                adBuilder.setItems(contactListUnique.toTypedArray()) { _, which ->
                    val name = contactListUnique[which]
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
                }
                adBuilder.setNegativeButton(context.getString(R.string.確認)) { _, _ ->
                    contactAlertDialog.dismiss()
                }
                adBuilder.setCancelable(true)
                adBuilder.show()
            }
            custodianDemo.setOnClickListener{
                val textDeptIdInput = deptId.text
                val custodianInfoList =DataBase.getInstance(activity)!!.getDataUao().findListByDeptId(textDeptIdInput.toString())
                val contactList = mutableListOf<String>()
                custodianInfoList.forEach {
                    contactList.add("${it.loginId}(${it.inventorName})\n# ${it.tel}")
                }
                val contactListUnique = contactList.distinct()
                val adBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                val contactAlertDialog: AlertDialog = adBuilder.create()
                contactAlertDialog.setCancelable(true)
                adBuilder.setTitle(context.getString(R.string.部門窗口))
                adBuilder.setItems(contactListUnique.toTypedArray()) { _, which ->
                    val name = contactListUnique[which]
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
                }
                adBuilder.setNegativeButton(context.getString(R.string.確認)) { _, _ ->
                    contactAlertDialog.dismiss()
                }
                adBuilder.setCancelable(true)
                adBuilder.show()
            }
            itemView.setOnClickListener {
                val passText = deptId.text
                val passText2 = deptName.text
                val intent = Intent(itemView.context, InventoryActivity::class.java)
                intent.putExtra("passText",passText)
                intent.putExtra("passText2",passText2)
                intent.putExtra("passScope",passScope)
                intent.putExtra("passPhase",passPhase)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val loading = ProgressionBar(activity)
                loading.startLoading()
                itemView.context.startActivity(intent)
                activity.finish()
            }

        }
    }

    private val filter: Filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val arrayListFilter: MutableList<AuthorityInformation> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    results.count = authorityInformationList.size
                    results.values = authorityInformationList
                } else {
                    for (itemModel in authorityInformationList) {
                        val custodianInfoList =DataBase.getInstance(activity)!!.getDataUao().findListByDeptId(itemModel.id)
                        val inventorNameList = mutableListOf<String>()
                        val loginIdList = mutableListOf<String>()
                        val telList = mutableListOf<String>()
                        custodianInfoList.forEach{
                            inventorNameList.add(it.inventorName)
                            telList.add(it.tel)
                            loginIdList.add(it.loginId)
                        }
                        if (itemModel.id.lowercase(Locale.getDefault())
                            .contains(constraint.toString().lowercase(Locale.getDefault())) or itemModel.deptName.lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault())) or inventorNameList.toString().lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault())) or telList.toString().lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault())) or loginIdList.toString().lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) {
                            arrayListFilter.add(itemModel)
                        }
                    }
                    results.count = arrayListFilter.size
                    results.values = arrayListFilter
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                authorityInformationListFiltered = results.values as MutableList<AuthorityInformation>
                notifyDataSetChanged()
                if (authorityInformationListFiltered.size == 0) {
                    Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun getFilter(): Filter {
        return filter
    }
    init {
        authorityInformationListFiltered = authorityInformationList
    }
}
