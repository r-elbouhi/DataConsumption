package com.rami.dataConsumption

import android.content.pm.PackageManager.NameNotFoundException
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rami.dataConsumption.model.PackageData
import com.rami.dataConsumption.utils.bytesIntoHumanReadable

/**
 * Created by Rami El-bouhi on 12,December,2021
 */
class PackageAdapter : ListAdapter<PackageData, PackageDataViewHolder>(PackageDataDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageDataViewHolder {
        return PackageDataViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: PackageDataViewHolder, position: Int) {
        val item: PackageData = getItem(position)
        holder.tvName.text = item.name
        holder.tvUsage.text = item.bytes?.bytesIntoHumanReadable(holder.itemView.context)
        try {
            holder.imgIcon.setImageDrawable(
                holder.itemView.context.packageManager.getApplicationIcon(item.packageName ?: "")
            )
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
    }

}

class PackageDataViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.package_item_row, parent, false)) {
    val imgIcon: ImageView = itemView.findViewById(R.id.img_icon)
    val tvName: TextView = itemView.findViewById(R.id.tv_name)
    val tvUsage: TextView = itemView.findViewById(R.id.tv_usage)
}

object PackageDataDiffCallback : DiffUtil.ItemCallback<PackageData>() {
    override fun areItemsTheSame(oldItem: PackageData, newItem: PackageData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PackageData, newItem: PackageData): Boolean {
        return oldItem == newItem
    }
}