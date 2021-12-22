package com.alien.efaInventory.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alien.efaInventory.dataModel.localDataProcessModel.DownloadData
import com.alien.efaInventory.R
import com.alien.efaInventory.databinding.UserdownloaditemLayoutBinding
import com.alien.efaInventory.interfaces.DataDownloadInterface


class UserDownloadAdapter(
    private val onClickListener: DataDownloadInterface,
    private val viewModel: List<DownloadData>
) :
    RecyclerView.Adapter<UserDownloadAdapter.UserDownloadViewHolder>() {
    class UserDownloadViewHolder (private val binding: UserdownloaditemLayoutBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind (model: DownloadData, importedFunction: DataDownloadInterface) {
            binding.download = model
            binding.onclick = importedFunction
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): UserDownloadViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: UserdownloaditemLayoutBinding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.userdownloaditem_layout,
                        parent, false)
                return UserDownloadViewHolder(binding)
            }
        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDownloadViewHolder {
            return UserDownloadViewHolder.from(parent)
        }
    override fun onBindViewHolder(holder:UserDownloadViewHolder, position: Int) {
        println("============== ${viewModel[position]}")
        holder.bind(viewModel[position], onClickListener)
    }



    override fun getItemCount(): Int {
        return viewModel.size

    }
}
