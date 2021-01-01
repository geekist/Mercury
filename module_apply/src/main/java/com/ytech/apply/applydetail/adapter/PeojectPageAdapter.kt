package com.ytech.apply.applydetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ytech.apply.applydetail.model.ProjectItemSub
import com.ytech.apply.databinding.NavigationPageLayoutBinding
import com.ytech.core.GlobalConfig

class PeojectPageAdapter constructor(context: Context) :
    PagedListAdapter<ProjectItemSub, PeojectPageAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<ProjectItemSub>() {
            override fun areItemsTheSame(
                oldItem: ProjectItemSub,
                newItem: ProjectItemSub
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProjectItemSub,
                newItem: ProjectItemSub
            ): Boolean {
                return oldItem == oldItem
            }

        }
    ) {

    private val mContext = context
    private val mInflater = LayoutInflater.from(mContext)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NavigationPageLayoutBinding.inflate(mInflater, parent, false)

        return ViewHolder(binding.root, binding, mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.setData(item)
    }


    class ViewHolder(
        itemView: View,
        binding: NavigationPageLayoutBinding,
        context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        private val mBinding = binding
        private val mContext = context

        fun setData(item: ProjectItemSub?) {
            mBinding.itemData = item
            mBinding.context = mContext

            Glide.with(GlobalConfig.getApplicationContext())
                .asBitmap()
                .load(item!!.envelopePic)
                .into(mBinding.pic)
        }
    }
}