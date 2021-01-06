package com.ytech.knowledge.knowledgedetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ytech.model.DatasBean
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.TreeDetailListItemBinding


class TreeDetailListAdapter(context: Context) : PagedListAdapter<com.ytech.model.DatasBean, TreeDetailListAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<com.ytech.model.DatasBean>() {
            override fun areItemsTheSame(
                oldItem: com.ytech.model.DatasBean,
                newItem: com.ytech.model.DatasBean
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: com.ytech.model.DatasBean,
                newItem: com.ytech.model.DatasBean
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }

    ) {

    private val mContext = context
    private val inflater = LayoutInflater.from(mContext)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<TreeDetailListItemBinding>(
            inflater,
            R.layout.tree_detail_list_item,
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
    }

    inner class ViewHolder(
        itemView: View,
        binding: TreeDetailListItemBinding
    ) : RecyclerView.ViewHolder(itemView) {

        private val mBinding = binding

        fun setData(item: com.ytech.model.DatasBean) {
            mBinding.item = item

            mBinding.itemParent.setOnClickListener {
         //       WebViewWarpService.instance.start(mContext, item.title, item.link)
            }

        }

    }
}