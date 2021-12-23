package com.alien.efaInventory.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.R
import com.alien.efaInventory.dataModel.localDataProcessModel.AuthorityInformation
import com.google.android.material.card.MaterialCardView


class DownloadSelectAdapter(
    val context: Context, private val authorityList: MutableList<AuthorityInformation>
) :
    RecyclerView.Adapter<DownloadSelectAdapter.DownloadSelectHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadSelectHolder {

        return DownloadSelectHolder(LayoutInflater.from(context).inflate(R.layout.downloadselectitem_layout, parent, false))
    }

    override fun onBindViewHolder(holder: DownloadSelectHolder, position: Int) {
        holder.cardView.tag = position
        holder.cardView.isChecked = authorityList[position].isSelected
        holder.cardView.setBackgroundColor(if (holder.cardView.isChecked) Color.parseColor("#1C2D3F") else  Color.WHITE)
        holder.cardView.checkedIconSize = 0
        holder.cardView.setOnClickListener  {
            val pos = holder.cardView.tag as Int
            holder.cardView.isChecked = !holder.cardView.isChecked
            authorityList[pos].isSelected = !authorityList[pos].isSelected
        }

        // bind item to holder
        holder.bindItem(authorityList[position])

    }


    fun getItem() = authorityList



    override fun getItemCount(): Int = authorityList.size

    class DownloadSelectHolder(view: View) : RecyclerView.ViewHolder(view) {

        var deptName: TextView = view.findViewById(R.id.deptName)
        var deptId: TextView = view.findViewById(R.id.deptId)
        private var count: TextView = view.findViewById(R.id.count1)
        var cardView: MaterialCardView = view.findViewById(R.id.cv_download)



        fun bindItem(authorityInformation: AuthorityInformation) {
            deptName.text = authorityInformation.deptName
            deptId.text= authorityInformation.id
            count.text = authorityInformation.count.toString()
        }

    }

}
